package com.meilisearch.sdk.exceptions;

public class JsonEncodingException extends MeilisearchException {
    public JsonEncodingException(Exception e) {
        super(e);
    }
}
