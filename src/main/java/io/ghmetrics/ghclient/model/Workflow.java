package io.ghmetrics.ghclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Objects;



@Data
@JsonTypeName("Workflow")
public class Workflow   {
  @JsonProperty("created_at")
  private OffsetDateTime createdAt;
  private Long id;
  private String name;
  private String path;
  private String state;
  @JsonProperty("updated_at")
  private OffsetDateTime updatedAt;

}

