package io.ghmetrics.ghclient.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.net.URI;

@Data
@JsonTypeName("Repository_Lite")
public class RepositoryLite {

    private String description;
    private Boolean fork;
    private URI forksUrl;
    private String fullName;
    private Long id;
    private String name;
    private String nodeId;


}

