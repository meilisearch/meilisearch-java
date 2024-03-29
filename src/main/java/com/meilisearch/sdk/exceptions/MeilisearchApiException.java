package com.meilisearch.sdk.exceptions;

/** This class wraps MeilisearchExceptions dealing with Meilisearch API errors */
public class MeilisearchApiException extends MeilisearchException {

    private final APIError error;

    public MeilisearchApiException(APIError error) {
        super();
        this.error = error;
    }

    public String getMessage() {
        return error.getMessage();
    }

    public String getCode() {
        return error.getCode();
    }

    public String getType() {
        return error.getType();
    }

    public String getLink() {
        return error.getLink();
    }

    @Override
    public String toString() {
        return "Meilisearch ApiException: {" + "Error=" + error + '}';
    }
}
