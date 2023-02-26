package io.ghmetrics.ghclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@JsonTypeName("actions_list_required_workflows_200_response")
public class ActionsListRequiredWorkflows200Response {
    @JsonProperty("total_count")
    private Long totalCount;
    @JsonProperty("required_workflows")
    private List<RequiredWorkflow> requiredWorkflows = new ArrayList<>();


}

