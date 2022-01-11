package com.meilisearch.sdk;
/** The code, type and error of the update status error */
public class UpdateStatusError {

    public UpdateStatusError() {}

    protected String updateStatusErrorCode = "";
    protected String updateStatusErrorType = "";
    protected String updateStatusErrorLink = "";

    public String getUpdateStatusErrorCode() {
        return updateStatusErrorCode;
    }

    public void setUpdateStatusErrorCode(String updateStatusErrorCode) {
        this.updateStatusErrorCode = updateStatusErrorCode;
    }

    public String getUpdateStatusErrorType() {
        return updateStatusErrorType;
    }

    public void setUpdateStatusErrorType(String updateStatusErrorType) {
        this.updateStatusErrorType = updateStatusErrorType;
    }

    public String getUpdateStatusErrorLink() {
        return updateStatusErrorLink;
    }

    public void setUpdateStatusErrorLink(String updateStatusErrorLink) {
        this.updateStatusErrorLink = updateStatusErrorLink;
    }
}
