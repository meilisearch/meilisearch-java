package com.meilisearch.sdk.exceptions;

public class MeiliSearchApiException extends MeiliSearchRuntimeException {

	private final APIError error;

	public MeiliSearchApiException(APIError error) {
		super();
		this.error = error;
	}

	public String getMessage() {
		return error.getMessage();
	}

	public String getErrorCode() {
		return error.getErrorCode();
	}

	public String getErrorType() {
		return error.getErrorType();
	}

	public String getErrorLink() {
		return error.getErrorLink();
	}

	@Override
	public String toString() {
		return "MeiliSearchApiException{" +
			"error=" + error +
			'}';
	}
}
