package com.meilisearch.sdk.exceptions;

public class MeiliSearchRuntimeException extends RuntimeException {
	public MeiliSearchRuntimeException() {
	}

	public MeiliSearchRuntimeException(Exception e) {
		super(e);
	}

	public MeiliSearchRuntimeException(Throwable cause) {
		super(cause);
	}
}
