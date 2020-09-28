package meilisearch;
import com.google.gson.Gson;

class StatusType {
	String name = "";
	int number = 0;
}

public class UpdateStatus {
	public String status = "";
	public int updateId = 0;
	public StatusType type = null;
	public float duration = 0.0f;
	public String enqueuedAt = "";
	public String processedAt = "";
	public String errorCode = "";
	public String errorType = "";
	public String errorLink = "";

	@Override
	public String toString() {
		return new com.google.gson.Gson().toJson(this);
	}
}
