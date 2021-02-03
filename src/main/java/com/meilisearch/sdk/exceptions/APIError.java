package com.meilisearch.sdk.exceptions;

import java.io.Serializable;

/**
 * This is class wraps errors sent by MeiliSearch API
 */
public class APIError implements Serializable {
	private static final long serialVersionUID = 900737636809105793L;

	private final String message;
	private final String errorCode;
	private final String errorType;
	private final String errorLink;

	public APIError(String message, String errorCode, String errorType, String errorLink) {
		this.message = message;
		this.errorCode = errorCode;
		this.errorType = errorType;
		this.errorLink = errorLink;
	}

	public String getMessage() {
		return message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorType() {
		return errorType;
	}

	public String getErrorLink() {
		return errorLink;
	}

	@Override
	public String toString() {
		return "APIError{" +
			"message='" + message + '\'' +
			", errorCode='" + errorCode + '\'' +
			", errorType='" + errorType + '\'' +
			", errorLink='" + errorLink + '\'' +
			'}';
	}
}
