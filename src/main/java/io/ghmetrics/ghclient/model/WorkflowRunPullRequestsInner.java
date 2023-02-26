package io.ghmetrics.ghclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.math.BigDecimal;
import java.net.URI;


@Data
@JsonTypeName("Workflow_Run_pull_requests_inner")
public class WorkflowRunPullRequestsInner {
    @JsonProperty("base")
    private CheckRunPullRequestBase base;
    @JsonProperty("head")
    private CheckRunPullRequestBase head;
    @JsonProperty("id")
    private BigDecimal id;
    @JsonProperty("number")
    private BigDecimal number;
    @JsonProperty("url")
    private URI url;

}

