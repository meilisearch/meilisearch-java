package com.meilisearch.sdk.api.index;

import com.meilisearch.sdk.api.MeiliApiError;

public class UpdateStatus extends MeiliApiError {
	protected String status = "";
	protected int updateId = 0;
	protected StatusType type = null;
	protected float duration = 0.0f;
	protected String enqueuedAt = "";
	protected String processedAt = "";


	public String getStatus() {
		return this.status;
	}

	public int getUpdateId() {
		return this.updateId;
	}

	public StatusType getStatusType() {
		return this.type;
	}

	public float getDuration() {
		return this.duration;
	}

	public String getEnqueuedAt() {
		return this.enqueuedAt;
	}

	public String getProcessedAt() {
		return this.processedAt;
	}
}
