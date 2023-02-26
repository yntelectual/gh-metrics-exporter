package io.ghmetrics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;


@DynamoDbBean
public class Record {
    private Long id;

    @JsonIgnore
    private Long workflowId;

    private String workflowName;

    private String status;

    private String conclusion;


    private OffsetDateTime createdAt;
    private OffsetDateTime startedAt;
    private OffsetDateTime completedAt;

    private OffsetDateTime updatedAt;
    private OffsetDateTime touchedAt;

    private Long actualExecutionTime;
    private Long queueTime;
    private Long totalTime;

    @JsonIgnore
    private String wfDebug;

    public Record complete() {
        this.queueTime = ChronoUnit.SECONDS.between(createdAt, startedAt);
        this.actualExecutionTime = ChronoUnit.SECONDS.between(startedAt, completedAt);
        this.totalTime = ChronoUnit.SECONDS.between(createdAt, completedAt);
        return this;
    }

    @DynamoDbPartitionKey
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(OffsetDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public OffsetDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(OffsetDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OffsetDateTime getTouchedAt() {
        return touchedAt;
    }

    public void setTouchedAt(OffsetDateTime touchedAt) {
        this.touchedAt = touchedAt;
    }

    public Long getActualExecutionTime() {
        return actualExecutionTime;
    }

    public void setActualExecutionTime(Long actualExecutionTime) {
        this.actualExecutionTime = actualExecutionTime;
    }

    public Long getQueueTime() {
        return queueTime;
    }

    public void setQueueTime(Long queueTime) {
        this.queueTime = queueTime;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public String getWfDebug() {
        return wfDebug;
    }

    public void setWfDebug(String wfDebug) {
        this.wfDebug = wfDebug;
    }
}
