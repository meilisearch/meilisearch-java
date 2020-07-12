package meilisearch;

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
    String processAt = "";

    @Override
    public String toString() {
        // TODO: update format
        return "Update status:" + status + " / id: " + updateId + " / process-at: " + processAt;
    }
}
