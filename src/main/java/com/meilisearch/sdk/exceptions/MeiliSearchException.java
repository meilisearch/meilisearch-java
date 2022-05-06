package com.meilisearch.sdk.exceptions;

import lombok.Getter;
import lombok.Setter;

public class MeiliSearchException extends Exception {

    /** This is a generic class for Meilisearch Exception handling */
    @Getter @Setter String message;

    @Getter @Setter String type;
    @Getter @Setter String code;
    @Getter @Setter String link;

    public MeiliSearchException(String message) {
        super(message);
        this.setErrorMessage(message);
    }

    public String getMessage() {
        return this.message;
    }

    public String getErrorType() {
        return this.type;
    }

    public String getErrorCode() {
        return this.code;
    }

    public String getErrorLink() {
        return this.link;
    }

    public void setErrorMessage(String message) {
        this.message = message;
    }

    public void setErrorType(String type) {
        this.type = type;
    }

    public void setErrorCode(String code) {
        this.code = code;
    }

    public void setErrorLink(String link) {
        this.link = link;
    }

    public String toString() {
        return this.getClass().getName() + ". Error message: " + this.message + ".";
    }
}
