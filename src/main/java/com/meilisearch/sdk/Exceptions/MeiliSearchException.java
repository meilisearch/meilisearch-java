package com.meilisearch.sdk.exceptions;

public class MeiliSearchException extends Exception {

	/**
	 * This is a generic class for MeiliSearch Exception handling
	 */

	String errorMessage;
	String errorType;
	String errorCode;
	String errorLink;

	public MeiliSearchException (String errorMessage) {
		super(errorMessage);
		this.setErrorMessage(errorMessage);
	}

	public String getMessage () {
		return this.errorMessage;
	}

	public String getErrorType () {
		return this.errorType;
	}

	public String getErrorCode () {
		return this.errorCode;
	}

	public String getErrorLink () {
		return this.errorLink;
	}

	public void setErrorMessage (String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setErrorType (String errorType) {
		this.errorType = errorType;
	}

	public void setErrorCode (String errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorLink (String errorLink) {
		this.errorLink = errorLink;
	}

	public String toString() {
		return this.getClass().getName() 
			+ ". Error message: " + this.errorMessage
			+ ".";
	}
}
