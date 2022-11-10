package com.meilisearch.sdk.exceptions;

import lombok.Getter;
import lombok.Setter;

/** This is a generic class for Meilisearch Exception handling */
@Getter
@Setter
public class MeilisearchException extends Exception {

    String error;
    String name;

    public MeilisearchException() {}

    public MeilisearchException(String error) {
        super(error);
        this.setError(error);
        this.name = this.getClass().getName();
    }

    public MeilisearchException(Exception e) {
        super(e);
        this.error = e.toString();
        this.name = e.getClass().getName();
    }

    public MeilisearchException(Throwable cause) {
        super(cause);
        this.error = cause.toString();
        this.name = cause.getClass().getName();
    }

    @Override
    public String toString() {
        return "Meilisearch Exception: {" + this.name + ". Error=" + this.error + '}';
    }
}
