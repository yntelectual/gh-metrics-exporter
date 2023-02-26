package io.ghmetrics.ghclient.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Information of a job execution in a workflow run
 **/

@JsonTypeName("job")
@Data
public class Job {
    private Long id;
    private Long runId;
    private String runUrl;
    private Long runAttempt;
    private String nodeId;
    private String headSha;
    private String url;
    private String htmlUrl;
    private StatusEnum status;
    private ConclusionEnum conclusion;
    private OffsetDateTime startedAt;
    private OffsetDateTime completedAt;
    private String name;
    private List<JobStepsInner> steps = null;
    private String checkRunUrl;
    private List<String> labels = new ArrayList<>();
    private Long runnerId;
    private String runnerName;
    private Long runnerGroupId;
    private String runnerGroupName;
    private String workflowName;
    private String headBranch;

    public enum StatusEnum {

        QUEUED(String.valueOf("queued")), IN_PROGRESS(String.valueOf("in_progress")), COMPLETED(
                String.valueOf("completed"));


        private String value;

        StatusEnum(String v) {
            value = v;
        }

        /**
         * Convert a String into String, as specified in the
         * <a href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS 2.0 Specification, section 3.2, p. 12</a>
         */
        public static StatusEnum fromString(String s) {
            for (StatusEnum b : StatusEnum.values()) {
                // using Objects.toString() to be safe if value type non-object type
                // because types like 'int' etc. will be auto-boxed
                if (Objects.toString(b.value).equals(s)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected string value '" + s + "'");
        }

        @JsonCreator
        public static StatusEnum fromValue(String value) {
            for (StatusEnum b : StatusEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }

        public String value() {
            return value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum ConclusionEnum {

        SUCCESS(String.valueOf("success")), FAILURE(String.valueOf("failure")), NEUTRAL(
                String.valueOf("neutral")), CANCELLED(String.valueOf("cancelled")), SKIPPED(
                String.valueOf("skipped")), TIMED_OUT(String.valueOf("timed_out")), ACTION_REQUIRED(
                String.valueOf("action_required"));


        private String value;

        ConclusionEnum(String v) {
            value = v;
        }

        /**
         * Convert a String into String, as specified in the
         * <a href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS 2.0 Specification, section 3.2, p. 12</a>
         */
        public static ConclusionEnum fromString(String s) {
            for (ConclusionEnum b : ConclusionEnum.values()) {
                // using Objects.toString() to be safe if value type non-object type
                // because types like 'int' etc. will be auto-boxed
                if (Objects.toString(b.value).equals(s)) {
                    return b;
                }
            }
            return null;
        }

        @JsonCreator
        public static ConclusionEnum fromValue(String value) {
            for (ConclusionEnum b : ConclusionEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            return null;
        }

        public String value() {
            return value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }


}

