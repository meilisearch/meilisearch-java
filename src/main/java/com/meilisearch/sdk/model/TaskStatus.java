package com.meilisearch.sdk.model;

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

    @Override
    public String toString() {
        return this.taskStatus;
    }
}
