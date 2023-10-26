package com.meilisearch.sdk.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;

/**
 * Enum for Task Status
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/tasks#status">API specification</a>
 */
public enum TaskStatus {
    @SerializedName("enqueued")
    ENQUEUED("enqueued"),
    @SerializedName("processing")
    PROCESSING("processing"),
    @SerializedName("succeeded")
    SUCCEEDED("succeeded"),
    @SerializedName("failed")
    FAILED("failed"),
    @SerializedName("canceled")
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
