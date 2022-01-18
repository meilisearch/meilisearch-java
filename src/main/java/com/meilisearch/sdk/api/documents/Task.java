package com.meilisearch.sdk.api.documents;

public class Task {
    private String status;

    private int uid;

    private String indexUid;

    private String type;

    private String duration;

    private String enqueuedAt;

    private String startedAt;

    private String finishedAt;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return this.uid;
    }

    public void setIndexUid(String indexUid) {
        this.indexUid = indexUid;
    }

    public String getIndexUid() {
        return this.indexUid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setEnqueuedAt(String enqueuedAt) {
        this.enqueuedAt = enqueuedAt;
    }

    public String getEnqueuedAt() {
        return this.enqueuedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getStartedAt() {
        return this.startedAt;
    }

    public void setFinishedAt(String finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getFinishedAt() {
        return this.finishedAt;
    }
}
