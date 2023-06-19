package com.meilisearch.sdk.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskStatus {
    ENQUEUED("enqueued"),
    PROCESSING("processing"),
    SUCCEEDED("succeeded"),
    FAILED("failed"),
    CANCELED("canceled");

    public final String taskStatus;

    TaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.taskStatus;
    }
}
