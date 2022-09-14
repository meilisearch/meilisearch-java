package com.meilisearch.sdk.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/** This is a generic class for Meilisearch Exception handling */
public class MeilisearchException extends Exception {

    String message;

    String type;
    String code;
    String link;

    public MeilisearchException() {}

    public MeilisearchException(String message) {
        super(message);
        this.setMessage(message);
    }

    public MeilisearchException(Exception e) {
        super(e);
    }

    public MeilisearchException(Throwable cause) {
        super(cause);
    }

    public String toString() {
        return "Meilisearch Exception: {"
                + this.getClass().getName()
                + ". Error message: "
                + this.message
                + '}';
    }
}
