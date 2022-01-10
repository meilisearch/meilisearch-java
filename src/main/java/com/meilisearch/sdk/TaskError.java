package com.meilisearch.sdk;
/** The code, type and error of the task error */
public class TaskError {

    public TaskError() {}

    protected String updateStatusErrorCode = "";
    protected String updateStatusErrorType = "";
    protected String updateStatusErrorLink = "";

    public String getTaskErrorCode() {
        return updateStatusErrorCode;
    }

    public void setTaskErrorCode(String updateStatusErrorCode) {
        this.updateStatusErrorCode = updateStatusErrorCode;
    }

    public String getTaskErrorType() {
        return updateStatusErrorType;
    }

    public void setTaskErrorType(String updateStatusErrorType) {
        this.updateStatusErrorType = updateStatusErrorType;
    }

    public String getTaskErrorLink() {
        return updateStatusErrorLink;
    }

    public void setTaskErrorLink(String updateStatusErrorLink) {
        this.updateStatusErrorLink = updateStatusErrorLink;
    }
}
