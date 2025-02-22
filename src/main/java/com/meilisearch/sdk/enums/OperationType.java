package com.meilisearch.sdk.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;

/**
 * Enum for Operation Type
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/tasks#type">API specification</a>
 */
public enum OperationType {
    @SerializedName("indexCreation")
    INDEX_CREATION("indexCreation"),
    @SerializedName("indexUpdate")
    INDEX_UPDATE("indexUpdate"),
    @SerializedName("indexDeletion")
    INDEX_DELETE("indexDeletion"),
    @SerializedName("indexSwap")
    INDEX_SWAP("indexSwap"),
    @SerializedName("documentAdditionOrUpdate")
    DOCUMENT_UPSERT("documentAdditionOrUpdate"),
    @SerializedName("documentDeletion")
    DOCUMENT_DELETE("documentDeletion"),
    @SerializedName("settingsUpdate")
    SETTINGS_UPDATE("settingsUpdate"),
    @SerializedName("dumpCreation")
    DUMP_CREATE("dumpCreation"),
    @SerializedName("taskCancelation")
    TASK_CANCEL("taskCancelation"),
    @SerializedName("taskDeletion")
    TASK_DELETE("taskDeletion"),
    @SerializedName("snapshotCreation")
    SNAPSHOT_CREATE("snapshotCreation");

    public final String operationType;

    OperationType(String operationType) {
        this.operationType = operationType;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.operationType;
    }
}
