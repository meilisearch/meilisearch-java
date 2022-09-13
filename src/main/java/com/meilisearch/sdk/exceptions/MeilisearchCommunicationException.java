package com.meilisearch.sdk.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/** This is class wraps MeilisearchExceptions dealing with Communication errors */
public class MeilisearchCommunicationException extends MeilisearchException {

    String error;

    public MeilisearchCommunicationException() {}

    public MeilisearchCommunicationException(Exception e) {
        super(e);
        this.error = e.toString();
    }

    public MeilisearchCommunicationException(String error) {
        super(error);
        this.setError(error);
    }

    @Override
    public String toString() {
        return "Meilisearch CommunicationException: {" + "error=" + this.error + '}';
    }
}
