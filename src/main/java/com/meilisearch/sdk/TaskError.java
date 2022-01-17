package com.meilisearch.sdk;
/** The code, type and error of the task error */
public class TaskError {

    public TaskError() {}

    protected String taskErrorCode = "";
    protected String taskErrorType = "";
    protected String taskErrorLink = "";

    public String getTaskErrorCode() {
        return taskErrorCode;
    }

    public void setTaskErrorCode(String taskErrorCode) {
        this.taskErrorCode = taskErrorCode;
    }

    public String getTaskErrorType() {
        return taskErrorType;
    }

    public void setTaskErrorType(String taskErrorType) {
        this.taskErrorType = taskErrorType;
    }

    public String getTaskErrorLink() {
        return taskErrorLink;
    }

    public void setTaskErrorLink(String taskErrorLink) {
        this.taskErrorLink = taskErrorLink;
    }
}
