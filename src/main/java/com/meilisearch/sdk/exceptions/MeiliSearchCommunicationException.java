package com.meilisearch.sdk.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/** This is class wraps MeiliSearchExceptions dealing with Communication errors */
public class MeiliSearchCommunicationException extends MeiliSearchException {

    String error;

    public MeiliSearchCommunicationException() {}

    public MeiliSearchCommunicationException(Exception e) {
        super(e);
        this.error = e.toString();
    }

    public MeiliSearchCommunicationException(String error) {
        super(error);
        this.setError(error);
    }

    @Override
    public String toString() {
        return "MeiliSearch CommunicationException: {" + "error=" + this.error + '}';
    }
}
