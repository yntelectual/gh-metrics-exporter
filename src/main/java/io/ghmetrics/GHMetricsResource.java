package io.ghmetrics;

import io.ghmetrics.ghclient.GHReposAPI;
import io.ghmetrics.ghclient.model.ActionsListRequiredWorkflowRuns200Response;
import io.ghmetrics.ghclient.model.WorkflowRun;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Path("/dump")
@ApplicationScoped
@Slf4j
public class GHMetricsResource {
    private final Map<Long, String> wfMap = new ConcurrentHashMap<>();
    private final Map<String, Integer> wfStatusCountMap = new HashMap<>();
    private AtomicReference<OffsetDateTime> lastInProgressJob;
    @ConfigProperty(name = "github.org")
    String owner;
    @ConfigProperty(name = "github.repo")
    String githubRepo;

    @Inject
    @RestClient
    GHReposAPI api;
    @Inject
    MeterRegistry registry;
    @Inject
    Instance<RepoService> repo;

    @PostConstruct
    public void init() {
        repo.get().init().subscribe().with(unused -> {
            log.info("Repo initialized");
        });

        Gauge.builder("wf.status", 0d, value -> wfStatusCountMap.getOrDefault(WorkflowRun.StatusEnum.QUEUED.name(),0))
                .tags(Tags.of(Tag.of("repo", githubRepo), Tag.of("status", WorkflowRun.StatusEnum.QUEUED.name())))
                .strongReference(true).register(registry);
        Gauge.builder("wf.status", 0d, value -> wfStatusCountMap.getOrDefault(WorkflowRun.StatusEnum.COMPLETED.name(),0))
                .tags(Tags.of(Tag.of("repo", githubRepo), Tag.of("status", WorkflowRun.StatusEnum.COMPLETED.name())))
                .strongReference(true).register(registry);
        Gauge.builder("wf.status", 0d,
                        value -> wfStatusCountMap.getOrDefault(WorkflowRun.StatusEnum.IN_PROGRESS.name(), 0))
                .tags(Tags.of(Tag.of("repo", githubRepo), Tag.of("status", WorkflowRun.StatusEnum.IN_PROGRESS.name())))
                .strongReference(true).register(registry);
    }

    @GET
    public Uni<List<Record>> dump() {
        return repo.get().list();
    }

    @GET
    @Path("/{id}")
    public WorkflowRun getRun(@PathParam("id") Long id) {
        return api.actionsGetWorkflowRun(owner, githubRepo, id, false);
    }

    @Scheduled(concurrentExecution = Scheduled.ConcurrentExecution.SKIP, every = "20s", delayed = "10s")
    public void fetch() {
        // if this is the first time we run this, fetch last 100 items regardless of creation time
        // otherwise we only fetch item created >= oldest non-complete job of previous run
        String createdParam = lastInProgressJob != null ? ">=" + lastInProgressJob.get()
                .format(DateTimeFormatter.ISO_DATE_TIME) : null;

        log.info("Timer tick, fetching Github runs created with 'created_at'='{}'}", createdParam);
        ActionsListRequiredWorkflowRuns200Response runs = api.actionsListWorkflowRunsForRepo(owner, githubRepo, null, null,
                null, null, 100L, null, createdParam, null, null, null);
        if (lastInProgressJob == null) {
            lastInProgressJob = new AtomicReference<>(OffsetDateTime.now());
        }
        wfStatusCountMap.clear();
        runs.getWorkflowRuns().stream().forEach(workflowRun -> {
            // find oldest non-complete workflow run in the results
            if (!workflowRun.getStatus().equals(WorkflowRun.StatusEnum.COMPLETED) && workflowRun.getCreatedAt()
                    .isBefore(lastInProgressJob.get())) {
                lastInProgressJob.set(workflowRun.getCreatedAt());
            }
            // track number of runs per status
            wfStatusCountMap.compute(workflowRun.getStatus().name(), (s, integer) -> integer != null ? integer + 1 : 1);

            process(workflowRun).onItem().ifNotNull().transform(Record::complete).subscribe().with(record -> {
                // if the run has not changed status to COMPLETED since previous check, the `process` uni returns null
                if (record == null) {
                    return;
                }
                // for every completed run, record 3 timers - queue time, exec time and total time
                log.info("Workflow completed: {} {} - created {} started {}(queue {}s) completed {}(execution {}s)", record.getId(), record.getWorkflowName(),
                        record.getCreatedAt(),record.getStartedAt(),record.getQueueTime(), record.getCompletedAt(), record.getActualExecutionTime());
                registry.timer("wf.run.total", Tags.of(Tag.of("workflow", record.getWorkflowName()),
                                Tag.of("conclusion", record.getConclusion())))
                        .record(record.getTotalTime(), TimeUnit.SECONDS);
                registry.timer("wf.run.execution", Tags.of(Tag.of("workflow", record.getWorkflowName()),
                                Tag.of("conclusion", record.getConclusion())))
                        .record(record.getActualExecutionTime(), TimeUnit.SECONDS);
                registry.timer("wf.run.queue", Tags.of(Tag.of("workflow", record.getWorkflowName()),
                                Tag.of("conclusion", record.getConclusion())))
                        .record(record.getQueueTime(), TimeUnit.SECONDS);
            });
        });
        log.info("Results by status: {}", wfStatusCountMap);
    }

    @GET
    @Path("workflows")
    public Response getWorkflows() {
        refreshWorkflows();
        return Response.ok(wfMap).build();
    }

    @Scheduled(concurrentExecution = Scheduled.ConcurrentExecution.SKIP, every = "1m")
    public void refreshWorkflows() {
        log.info("Loading workflow definitions from Github {}/{}", owner, githubRepo);
        api.actionsListRepoWorkflows(owner, githubRepo, 100L, 0L).getWorkflows().forEach(workflow -> {
            wfMap.put(workflow.getId(), workflow.getName());
        });
        log.info("Got {} workflows", wfMap);
    }

    private Uni<Record> process(WorkflowRun wf) {
        return repo.get().get(wf.getId())
                .replaceIfNullWith(createNewReport(wf))
                .map(record -> {
                    if (wf.getStatus().name().equals(record.getStatus())) {
                        //Status has not changed
                        if (!wf.toString().equals(record.getWfDebug())) {
                            log.warn(
                                    "Status of {} has not changed but payload different\n{}\n=======vs========\n{}",
                                    record.getId(),
                                    record.getWfDebug(), wf);
                        }
                        return null;
                    }
                    record.setWfDebug(wf.toString());
                    log.info("Run {} status change {} -> {}", record.getId(), record.getStatus(), wf.getStatus());
                    switch (wf.getStatus()) {
                        case COMPLETED -> {
//                            log.info("WF completed {} previous status {} {}", record.getId(), record.getStatus(),
//                                    record.getConclusion());
                            record.setCompletedAt(wf.getUpdatedAt());
                            if (wf.getConclusion() != null) {
                                record.setConclusion(wf.getConclusion().name());
                            } else {
                                log.warn("Run completed but conclusion empty {} {} startup failure?", wf.getId(),
                                        wf.getConclusion());
                            }
                        }
                        case IN_PROGRESS -> {
                            //if the run is in progress, then last update was the start
                            record.setStartedAt(wf.getUpdatedAt());
                        }
                    }
                    record.setStatus(wf.getStatus().name());
                    return record;
                }).onItem().ifNotNull().call(rec -> repo.get().put(rec))
                .onItem().ifNotNull().transform(
                        record -> {
                            if (record.getCompletedAt() != null && null != record.getConclusion()) {
                                record.complete();
                                return record;
                            } else {
                                return null;
                            }
                        }).onFailure().recoverWithUni(throwable -> {
                    log.error("Error in mapping chain ", throwable);
                    return null;
                });
    }

    private Record createNewReport(WorkflowRun wf) {
        Record record = new Record();
        record.setId(wf.getId());
        record.setCreatedAt(wf.getCreatedAt());
        record.setUpdatedAt(wf.getUpdatedAt());
        record.setWorkflowId(wf.getWorkflowId());
        record.setWorkflowName(wfMap.getOrDefault(wf.getWorkflowId(), "" + wf.getWorkflowId()));
        switch (wf.getStatus()) {
            case COMPLETED -> record.setStartedAt(
                    wf.getCreatedAt());//if the run is already completed by now, then we dont know when it was queued
            case IN_PROGRESS ->
                    record.setStartedAt(wf.getUpdatedAt());//if the run is in progress, then last update was the start
        }
//        log.info("Created record {} status {} started {}", record.getId(), wf.getStatus(), record.getStartedAt());
        return record;
    }
}