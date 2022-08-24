package com.meilisearch.sdk.exceptions;

public class JsonEncodingException extends Exception {
    public JsonEncodingException(Exception e) {
        super(e);
    }
}
