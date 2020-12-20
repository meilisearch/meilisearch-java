package com.meilisearch.sdk.exceptions;

public class MeiliSearchRuntimeException extends RuntimeException {
	public MeiliSearchRuntimeException(Exception e) {
		super(e);
	}
}
