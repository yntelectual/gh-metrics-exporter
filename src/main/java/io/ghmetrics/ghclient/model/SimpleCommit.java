package io.ghmetrics.ghclient.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@JsonTypeName("SimpleCommit")
@Data
public class SimpleCommit {
    private Committer author;
    private Committer committer;
    private String id;
    private String message;
    private String timestamp;
    private String treeId;
}

