package com.meilisearch.sdk.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeilisearchUrlException extends MeilisearchException {

    String error;

    public MeilisearchUrlException(Exception e) {
        super(e);
        this.error = e.toString();
    }

    @Override
    public String toString() {
        return "Meilisearch UrlException: {" + "Error=" + this.error + '}';
    }
}
