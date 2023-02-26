package io.ghmetrics.ghclient.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@JsonTypeName("actions_list_labels_for_self_hosted_runner_for_org_200_response")
@Data
public class ActionsListLabelsForSelfHostedRunnerForOrg200Response {
    private Long totalCount;
    private List<RunnerLabel> labels = new ArrayList<>();

}

