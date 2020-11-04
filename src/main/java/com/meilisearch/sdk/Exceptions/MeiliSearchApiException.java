package com.meilisearch.sdk.exceptions;

import com.meilisearch.sdk.json.GsonJsonHandler;

public class MeiliSearchApiException extends MeiliSearchException {

	/**
	 * This is class wraps errors sent by MeiliSearch API
	 */

	private GsonJsonHandler gson = new GsonJsonHandler();

	private class ApiError {
		public String message;
		public String errorCode;
		public String errorType;
		public String errorLink;
	}
	
	public MeiliSearchApiException (String errorMessage) throws Exception {
		super(errorMessage);
		ApiError error = this.gson.decode(
			errorMessage,
			ApiError.class
		);
		this.setErrorMessage(error.message);
		this.setErrorCode(error.errorCode);
		this.setErrorType(error.errorType);
		this.setErrorLink(error.errorLink);
	}

	public String toString() {
		return this.getClass().getName() 
			+ ". Error message: " + this.errorMessage
			+ ". Error code: " + this.errorCode
			+ ". Error documentation: " + this.errorLink
			+ ". Error type: " + this.errorType
			+ ".";
	}
}
