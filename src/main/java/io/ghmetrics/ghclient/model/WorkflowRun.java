package io.ghmetrics.ghclient.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Objects;


@Data
@JsonTypeName("Workflow_Run")
@ToString
public class WorkflowRun {
    @JsonProperty("actor")
    private User actor;
    @JsonProperty("conclusion")
    private ConclusionEnum conclusion;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    @JsonProperty("event")
    private String event;
    @JsonProperty("head_branch")
    private String headBranch;

    @JsonProperty("head_repository")
    private RepositoryLite headRepository;
    @JsonProperty("head_sha")
    private String headSha;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;
    @JsonProperty("node_id")
    private String nodeId;
    @JsonProperty("path")
    private String path;

    @JsonProperty("repository")
    private RepositoryLite repository;

    @JsonProperty("run_attempt")
    private Long runAttempt;
    @JsonProperty("run_number")
    private Long runNumber;
    @JsonProperty("run_started_at")
    private OffsetDateTime runStartedAt;
    @JsonProperty("status")
    private StatusEnum status;
    @JsonProperty("triggering_actor")
    private User triggeringActor;
    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;

    @JsonProperty("workflow_id")
    private Long workflowId;



    public enum ConclusionEnum {

        SUCCESS(String.valueOf("success")), FAILURE(String.valueOf("failure")), NEUTRAL(
                String.valueOf("neutral")), CANCELLED(String.valueOf("cancelled")), TIMED_OUT(
                String.valueOf("timed_out")), ACTION_REQUIRED(String.valueOf("action_required")), STALE(
                String.valueOf("stale")), NULL(String.valueOf("null")), SKIPPED(String.valueOf("skipped"));


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

    public enum StatusEnum {

        REQUESTED(String.valueOf("requested")), IN_PROGRESS(String.valueOf("in_progress")), COMPLETED(
                String.valueOf("completed")), QUEUED(String.valueOf("queued")), PENDING(
                String.valueOf("pending")), WAITING(String.valueOf("waiting"));


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


}

