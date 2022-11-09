package com.meilisearch.sdk.exceptions;

/** This class wraps MeilisearchExceptions dealing with json decoding errors */
public class JsonDecodingException extends MeilisearchException {
    public JsonDecodingException(Exception e) {
        super(e);
    }

    public JsonDecodingException(String message) {
        super(message);
    }
}
