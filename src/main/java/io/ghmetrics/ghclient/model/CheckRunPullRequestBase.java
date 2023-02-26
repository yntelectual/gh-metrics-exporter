package io.ghmetrics.ghclient.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;


@JsonTypeName("Check_Run_Pull_Request_base")
@Data
public class CheckRunPullRequestBase {
    private String ref;
    private RepoRef repo;
    private String sha;
}

