package meilisearch;
import com.google.gson.Gson;

class StatusType {
	String name = "";
	int number = 0;
}

public class UpdateStatus {
	String status = "";
	int updateId = 0;
	StatusType type = null;
	float duration = 0.0f;
	String enqueuedAt = "";
	String processedAt = "";
	String errorCode = "";
	String errorType = "";
	String errorLink = "";

	private static Gson gsonUpdate = new Gson();

	@Override
	public String toString() {
		return gsonUpdate.toJson(this);
	}

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

	public String getErrorCode() {
		return this.errorCode;
	}

	public String getErrorType() {
		return this.errorType;
	}

	public String getErrorLink() {
		return this.errorLink;
	}	
}
