package com.meilisearch.sdk.exceptions;

public class MeiliSearchApiException extends MeiliSearchRuntimeException {

    private final APIError error;

    public MeiliSearchApiException(APIError error) {
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
}
