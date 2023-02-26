package io.ghmetrics.ghclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.net.URI;


@Data
@JsonTypeName("User")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2023-02-22T10:36:41.379409+01:00[Europe/Bratislava]")
public class User {
    @JsonProperty("avatar_url")
    private URI avatarUrl;
    @JsonProperty("deleted")
    private Boolean deleted;
    @JsonProperty("email")
    private String email;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("login")
    private String login;
    @JsonProperty("name")
    private String name;
    @JsonProperty("node_id")
    private String nodeId;

    @JsonProperty("url")
    private URI url;


}

