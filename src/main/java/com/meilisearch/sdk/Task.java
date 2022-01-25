package com.meilisearch.sdk;

import com.google.gson.Gson;

/** MeiliSearch response for a Task */
public class Task {
    protected String status = "";
    protected int uid = 0;
    protected String indexUid = "";
    protected String type = null;
    // protected HashMap<String, int> details = null;
    protected String duration = "";
    protected String enqueuedAt = "";
    protected String startedAt = "";
    protected String finishedAt = "";
    protected TaskError error = null;

    private static Gson gsonTask = new Gson();

    /**
     * Method to return the JSON String of the Task
     *
     * @return JSON string of the Task object
     */
    @Override
    public String toString() {
        return gsonTask.toJson(this);
    }

    /**
     * Method to return the status of the Task object
     *
     * @return String containing the status of the Task Object
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Method to return the uid of the Task
     *
     * @return String containing the identifier of the Task
     */
    public int getUid() {
        return this.uid;
    }

    /**
     * Method to return the index uid of the Task
     *
     * @return String containing the index identifier of the Task
     */
    public String getIndexUid() {
        return this.indexUid;
    }

    /**
     * Method to return the type of the Task
     *
     * @return String value of the type of the Task
     */
    public String getType() {
        return this.type;
    }

    /**
     * Method to return the elapsed time of the Task
     *
     * @return String value of the duration of the Task
     */
    public String getDuration() {
        return this.duration;
    }

    /**
     * Method to return the time that the Task was enqueued at
     *
     * @return String value of the date and time of the Task when it was enqueued
     */
    public String getEnqueuedAt() {
        return this.enqueuedAt;
    }

    /**
     * Method to return the time that the Task was started at
     *
     * @return String value of the date and time of the Task when it was started
     */
    public String getStartedAt() {
        return this.startedAt;
    }

    /**
     * Method to return the time that the Update was finished at
     *
     * @return String value of the date and time of the Update when it was finished
     */
    public String getFinishedAt() {
        return this.finishedAt;
    }

    /**
     * Method to return the error of the Task
     *
     * @return error Object with the code, type and link of the Task Error
     */
    public TaskError getError() {
        return this.error;
    }
}
