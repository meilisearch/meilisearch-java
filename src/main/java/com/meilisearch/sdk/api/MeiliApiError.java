package com.meilisearch.sdk.api;

public class MeiliApiError {
	protected String errorCode;
	protected String errorType;
	protected String errorLink;
	protected String message;

	public String getErrorCode() {
		return errorCode;
	}

	public MeiliApiError setErrorCode(String errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	public String getErrorType() {
		return errorType;
	}

	public MeiliApiError setErrorType(String errorType) {
		this.errorType = errorType;
		return this;
	}

	public String getErrorLink() {
		return errorLink;
	}

	public MeiliApiError setErrorLink(String errorLink) {
		this.errorLink = errorLink;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public MeiliApiError setMessage(String message) {
		this.message = message;
		return this;
	}
}
