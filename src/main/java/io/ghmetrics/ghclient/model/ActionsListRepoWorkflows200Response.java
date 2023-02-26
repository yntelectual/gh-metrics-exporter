package io.ghmetrics.ghclient.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@JsonTypeName("actions_list_repo_workflows_200_response")
@Data
public class ActionsListRepoWorkflows200Response {
    private Long totalCount;
    private List<Workflow> workflows = new ArrayList<>();
}

