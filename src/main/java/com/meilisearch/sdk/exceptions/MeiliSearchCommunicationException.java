package com.meilisearch.sdk.exceptions;

/**
 * This is class wraps MeiliSearchExceptions dealing with Communication errors
 */
public class MeiliSearchCommunicationException  extends MeiliSearchException {

	public MeiliSearchCommunicationException (String errorMessage) {
		super(errorMessage);
	}

}
