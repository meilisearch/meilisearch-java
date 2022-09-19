package com.meilisearch.sdk.exceptions;

/** This class wraps MeilisearchExceptions dealing with json encoding errors */
public class JsonEncodingException extends MeilisearchException {
    public JsonEncodingException(Exception e) {
        super(e);
    }
}
