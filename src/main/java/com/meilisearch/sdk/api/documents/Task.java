package com.meilisearch.sdk.api.documents;

public class Task {
    private String status;

    private int uid;

    private Type type;

    private double duration;

    private String enqueuedAt;

    private String processedAt;

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

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return this.duration;
    }

    public void setEnqueuedAt(String enqueuedAt) {
        this.enqueuedAt = enqueuedAt;
    }

    public String getEnqueuedAt() {
        return this.enqueuedAt;
    }

    public void setProcessedAt(String processedAt) {
        this.processedAt = processedAt;
    }

    public String getProcessedAt() {
        return this.processedAt;
    }
}
