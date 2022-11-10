package com.meilisearch.sdk.exceptions;

import lombok.Getter;
import lombok.Setter;

/** This class wraps MeilisearchExceptions dealing with Timeout errors */
@Getter
@Setter
public class MeilisearchTimeoutException extends MeilisearchException {

    String error;

    public MeilisearchTimeoutException() {}

    public MeilisearchTimeoutException(String error) {
        super(error);
        this.setError(error);
    }

    public MeilisearchTimeoutException(Exception e) {
        super(e);
        this.error = e.toString();
    }

    public MeilisearchTimeoutException(Throwable cause) {
        super(cause);
        this.error = cause.toString();
    }

    @Override
    public String toString() {
        return "Meilisearch TimeoutException: {" + "Error=" + this.error + '}';
    }
}
