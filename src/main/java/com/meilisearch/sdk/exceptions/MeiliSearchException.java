package com.meilisearch.sdk.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeiliSearchException extends Exception {

    /** This is a generic class for Meilisearch Exception handling */
    String message;

    String type;
    String code;
    String link;

    public MeiliSearchException(String message) {
        super(message);
        this.setMessage(message);
    }
}
