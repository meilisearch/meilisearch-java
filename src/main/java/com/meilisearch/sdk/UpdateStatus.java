package com.meilisearch.sdk;

import com.google.gson.Gson;

/** The name and count of the update status type */
class StatusType {
    String name = "";
    int number = 0;
}

/** The code, type and error of the update status error */
public class Error {
    protected String code = "";
    protected String type = "";
    protected String link = "";

    public String getCode() {
        return this.code;
    }
    public String getType() {
        return this.type;
    }
    public String getLink() {
        return this.link;
    }
}

/** MeiliSearch response for an Update Status */
public class UpdateStatus {
    protected String status = "";
    protected int updateId = 0;
    protected StatusType type = null;
    protected float duration = 0.0f;
    protected String enqueuedAt = "";
    protected String processedAt = "";
    protected Error error = null;

    private static Gson gsonUpdate = new Gson();

    /**
     * Method to return the JSON String of the Update Status
     *
     * @return JSON string of the Update Status object
     */
    @Override
    public String toString() {
        return gsonUpdate.toJson(this);
    }

    /**
     * Method to return the status of the Update Status object
     *
     * @return String containing the status of the Update Status Object
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Method to return the id of the Update
     *
     * @return String containing the identifier of the Update
     */
    public int getUpdateId() {
        return this.updateId;
    }

    /**
     * Method to return the name and quantity of the Update Status
     *
     * @return statusType Object of the name and quantity of the Update Status
     */
    public StatusType getStatusType() {
        return this.type;
    }

    /**
     * Method to return the elapsed time of the Update Status
     *
     * @return float value of the duration of the Update Status
     */
    public float getDuration() {
        return this.duration;
    }

    /**
     * Method to return the time that the Update was enqueued at
     *
     * @return String value of the date and time of the Update when it was enqueued
     */
    public String getEnqueuedAt() {
        return this.enqueuedAt;
    }

    /**
     * Method to return the time that the Update was processed at
     *
     * @return String value of the date and time of the Update when it was processed
     */
    public String getProcessedAt() {
        return this.processedAt;
    }

    /**
     * Method to return the error of the Update Status
     *
     * @return error Object with the code, type and link of the Update Status Error
     */
    public Error getError() {
        return this.error;
    }
}
