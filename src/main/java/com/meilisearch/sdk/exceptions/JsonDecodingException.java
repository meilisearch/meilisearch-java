package com.meilisearch.sdk.exceptions;

public class JsonDecodingException extends Exception {
    public JsonDecodingException(Exception e) {
        super(e);
    }

    public JsonDecodingException(String message) {
        super(message);
    }
}
