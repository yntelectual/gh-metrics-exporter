package io.ghmetrics.ghclient.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

/**
 * A self hosted runner
 **/

@JsonTypeName("runner")
@Data
public class Runner {
    private Long id;
    private String name;
    private String os;
    private String status;
    private Boolean busy;
//    private List<RunnerLabel> labels = new ArrayList<>();

}

