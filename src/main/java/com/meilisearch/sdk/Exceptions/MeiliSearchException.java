package com.meilisearch.sdk;

public class MeiliSearchException extends Exception {

	/**
	 * This is a generic class for MeiliSearch Exception handling
	 */

	String message;
	String errorCode;
	String errorLink;

	public String getMessage () {
		return this.message;
	}

	public String getErrorCode () {
		return this.errorCode;
	}

	public String getErrorLink () {
		return this.errorLink;
	}

	public void setMessage (String message) {
		this.message = message;
	}

	public void setErrorCode (String errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorLink (String errorLink) {
		this.errorLink = errorLink;
	}
}
