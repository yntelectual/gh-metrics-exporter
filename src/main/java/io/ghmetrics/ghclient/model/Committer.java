package io.ghmetrics.ghclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Metaproperties for Git author/committer information.
 **/

@JsonTypeName("Committer")
@Data
public class Committer {
    private OffsetDateTime date;
    private String email;
    private String name;
    private String username;

}

