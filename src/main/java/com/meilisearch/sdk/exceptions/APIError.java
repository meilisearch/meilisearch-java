package com.meilisearch.sdk.exceptions;

import java.io.Serializable;
import lombok.Getter;

@Getter
/** This is class wraps errors sent by Meilisearch API */
public class APIError implements Serializable {
    private static final long serialVersionUID = 900737636809105793L;

    private String message = null;
    private String code = null;
    private String type = null;
    private String link = null;

    public APIError() {}

    public APIError(String message, String code, String type, String link) {
        this.message = message;
        this.code = code;
        this.type = type;
        this.link = link;
    }
}
