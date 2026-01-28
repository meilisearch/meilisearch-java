package com.meilisearch.sdk.exceptions;

/** Signals that granular filterable attributes cannot be represented by the legacy String[] API. */
public class GranularFilterableAttributesException extends MeilisearchException {
    public GranularFilterableAttributesException(String message) {
        super(message);
    }
}
