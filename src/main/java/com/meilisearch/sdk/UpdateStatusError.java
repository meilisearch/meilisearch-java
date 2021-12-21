package com.meilisearch.sdk;
/** The code, type and error of the update status error */
public class UpdateStatusError {

	public UpdateStatusError(){}

    protected String UpdateStatusErrorCode = "";
    protected String UpdateStatusErrorType = "";
    protected String UpdateStatusErrorLink = "";

	public String getUpdateStatusErrorCode() {
		return UpdateStatusErrorCode;
	}

	public void setUpdateStatusErrorCode(String updateStatusErrorCode) {
		UpdateStatusErrorCode = updateStatusErrorCode;
	}

	public String getUpdateStatusErrorType() {
		return UpdateStatusErrorType;
	}

	public void setUpdateStatusErrorType(String updateStatusErrorType) {
		UpdateStatusErrorType = updateStatusErrorType;
	}

	public String getUpdateStatusErrorLink() {
		return UpdateStatusErrorLink;
	}

	public void setUpdateStatusErrorLink(String updateStatusErrorLink) {
		UpdateStatusErrorLink = updateStatusErrorLink;
	}
}
