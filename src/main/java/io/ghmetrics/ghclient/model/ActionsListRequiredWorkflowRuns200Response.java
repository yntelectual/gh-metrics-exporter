package io.ghmetrics.ghclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@JsonTypeName("actions_list_required_workflow_runs_200_response")
@Data
public class ActionsListRequiredWorkflowRuns200Response {
    @JsonProperty("total_count")
    private Long totalCount;
    @JsonProperty("workflow_runs")
    private List<WorkflowRun> workflowRuns = new ArrayList<>();

}

