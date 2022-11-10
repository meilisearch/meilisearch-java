package com.meilisearch.sdk.exceptions;

import lombok.Getter;
import lombok.Setter;

/** This class wraps MeilisearchExceptions dealing with Communication errors */
@Getter
@Setter
public class MeilisearchCommunicationException extends MeilisearchException {

    String error;

    public MeilisearchCommunicationException() {}

    public MeilisearchCommunicationException(String error) {
        super(error);
        this.setError(error);
    }

    public MeilisearchCommunicationException(Exception e) {
        super(e);
        this.error = e.toString();
    }

    public MeilisearchCommunicationException(Throwable cause) {
        super(cause);
        this.error = cause.toString();
    }

    @Override
    public String toString() {
        return "Meilisearch CommunicationException: {" + "Error=" + this.error + '}';
    }
}
