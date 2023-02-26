package io.ghmetrics.ghclient;


import io.ghmetrics.ghclient.model.*;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import java.time.OffsetDateTime;


@Path("/repos")
@RegisterRestClient(configKey = "github-api", baseUri = "https://api.github.com")
@RegisterProvider(GhAuthInterceptor.class )
//@RegisterProvider(GHLoggingInterceptor.class)
public interface GHReposAPI {

//    @GET
//    @Path("/{owner}/{repo}/actions/caches")
//    @Produces({"application/json"})
//    ActionsCacheList actionsGetActionsCacheList(@PathParam("owner") String owner, @PathParam("repo") String repo, @QueryParam("per_page") @DefaultValue("30") Long perPage, @QueryParam("page") @DefaultValue("1") Long page, @QueryParam("ref") String ref, @QueryParam("key") String key, @QueryParam("sort") @DefaultValue("last_accessed_at") String sort, @QueryParam("direction") @DefaultValue("desc") String direction);
//
//    @GET
//    @Path("/{owner}/{repo}/actions/cache/usage")
//    @Produces({"application/json"})
//    ActionsCacheUsageByRepository actionsGetActionsCacheUsage(@PathParam("owner") String owner, @PathParam("repo") String repo);

    @GET
    @Path("/{owner}/{repo}/actions/jobs/{job_id}")
    @Produces({"application/json"})
    Job actionsGetJobForWorkflowRun(@PathParam("owner") String owner, @PathParam("repo") String repo, @PathParam("job_id") Long jobId);


    @GET
    @Path("/{owner}/{repo}/actions/runners/{runner_id}")
    @Produces({"application/json"})
    Runner actionsGetSelfHostedRunnerForRepo(@PathParam("owner") String owner, @PathParam("repo") String repo, @PathParam("runner_id") Long runnerId);

    @GET
    @Path("/{owner}/{repo}/actions/workflows/{workflow_id}")
    @Produces({"application/json"})
    Workflow actionsGetWorkflow(@PathParam("owner") String owner, @PathParam("repo") String repo, @PathParam("workflow_id") Long workflowId);

    @GET
    @Path("/{owner}/{repo}/actions/runs/{run_id}")
    @Produces({"application/json"})
    WorkflowRun actionsGetWorkflowRun(@PathParam("owner") String owner, @PathParam("repo") String repo, @PathParam("run_id") Long runId, @QueryParam("exclude_pull_requests") @DefaultValue("false") Boolean excludePullRequests);


//    @GET
//    @Path("/{owner}/{repo}/actions/runs/{run_id}/jobs")
//    @Produces({"application/json"})
//    ActionsListJobsForWorkflowRunAttempt200Response actionsListJobsForWorkflowRun(@PathParam("owner") String owner, @PathParam("repo") String repo, @PathParam("run_id") Long runId, @QueryParam("filter") @DefaultValue("latest") String filter, @QueryParam("per_page") @DefaultValue("30") Long perPage, @QueryParam("page") @DefaultValue("1") Long page);
//
//    @GET
//    @Path("/{owner}/{repo}/actions/runs/{run_id}/attempts/{attempt_number}/jobs")
//    @Produces({"application/json"})
//    ActionsListJobsForWorkflowRunAttempt200Response actionsListJobsForWorkflowRunAttempt(@PathParam("owner") String owner, @PathParam("repo") String repo, @PathParam("run_id") Long runId, @PathParam("attempt_number") Long attemptNumber, @QueryParam("per_page") @DefaultValue("30") Long perPage, @QueryParam("page") @DefaultValue("1") Long page);

    @GET
    @Path("/{owner}/{repo}/actions/runners/{runner_id}/labels")
    @Produces({"application/json"})
    ActionsListLabelsForSelfHostedRunnerForOrg200Response actionsListLabelsForSelfHostedRunnerForRepo(@PathParam("owner") String owner, @PathParam("repo") String repo, @PathParam("runner_id") Long runnerId);

    @GET
    @Path("/{owner}/{repo}/actions/workflows")
    @Produces({"application/json"})
    ActionsListRepoWorkflows200Response actionsListRepoWorkflows(@PathParam("owner") String owner, @PathParam("repo") String repo, @QueryParam("per_page") @DefaultValue("30") Long perPage, @QueryParam("page") @DefaultValue("1") Long page);

    @GET
    @Path("/{owner}/{repo}/actions/required_workflows/{required_workflow_id_for_repo}/runs")
    @Produces({"application/json"})
    ActionsListRequiredWorkflowRuns200Response actionsListRequiredWorkflowRuns(@PathParam("owner") String owner, @PathParam("repo") String repo, @PathParam("required_workflow_id_for_repo") Long requiredWorkflowIdForRepo, @QueryParam("actor") String actor, @QueryParam("branch") String branch, @QueryParam("event") String event, @QueryParam("status") String status, @QueryParam("per_page") @DefaultValue("30") Long perPage, @QueryParam("page") @DefaultValue("1") Long page, @QueryParam("created") OffsetDateTime created, @QueryParam("exclude_pull_requests") @DefaultValue("false") Boolean excludePullRequests, @QueryParam("check_suite_id") Long checkSuiteId, @QueryParam("head_sha") String headSha);

    @GET
    @Path("/{owner}/{repo}/actions/runners")
    @Produces({"application/json"})
    ActionsListSelfHostedRunnersForOrg200Response actionsListSelfHostedRunnersForRepo(@PathParam("owner") String owner, @PathParam("repo") String repo, @QueryParam("per_page") @DefaultValue("30") Long perPage, @QueryParam("page") @DefaultValue("1") Long page);

    @GET
    @Path("/{owner}/{repo}/actions/workflows/{workflow_id}/runs")
    @Produces({"application/json"})
    ActionsListRequiredWorkflowRuns200Response actionsListWorkflowRuns(@PathParam("owner") String owner, @PathParam("repo") String repo, @PathParam("workflow_id") Long workflowId, @QueryParam("actor") String actor, @QueryParam("branch") String branch, @QueryParam("event") String event, @QueryParam("status") String status, @QueryParam("per_page") @DefaultValue("30") Long perPage, @QueryParam("page") @DefaultValue("1") Long page, @QueryParam("created") OffsetDateTime created, @QueryParam("exclude_pull_requests") @DefaultValue("false") Boolean excludePullRequests, @QueryParam("check_suite_id") Long checkSuiteId, @QueryParam("head_sha") String headSha);

    @GET
    @Path("/{owner}/{repo}/actions/runs")
    @Produces({"application/json"})
    ActionsListRequiredWorkflowRuns200Response actionsListWorkflowRunsForRepo(@PathParam("owner") String owner, @PathParam("repo") String repo, @QueryParam("actor") String actor, @QueryParam("branch") String branch, @QueryParam("event") String event, @QueryParam("status") String status, @QueryParam("per_page") @DefaultValue("30") Long perPage, @QueryParam("page") @DefaultValue("1") Long page, @QueryParam("created") String created, @QueryParam("exclude_pull_requests") @DefaultValue("false") Boolean excludePullRequests, @QueryParam("check_suite_id") Long checkSuiteId, @QueryParam("head_sha") String headSha);
}
