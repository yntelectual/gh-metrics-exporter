package io.ghmetrics.ghclient.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@JsonTypeName("actions_list_self_hosted_runners_for_org_200_response")
@Data
public class ActionsListSelfHostedRunnersForOrg200Response {
    private Long totalCount;
    private List<Runner> runners = new ArrayList<>();
}

