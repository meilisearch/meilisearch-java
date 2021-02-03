package com.meilisearch.sdk.api.documents;

public class Update {
	private String status;

	private int updateId;

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

	public void setUpdateId(int updateId) {
		this.updateId = updateId;
	}

	public int getUpdateId() {
		return this.updateId;
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
