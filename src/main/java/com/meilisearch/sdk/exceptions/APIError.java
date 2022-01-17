package com.meilisearch.sdk.exceptions;

import java.io.Serializable;

/** This is class wraps errors sent by MeiliSearch API */
public class APIError implements Serializable {
    private static final long serialVersionUID = 900737636809105793L;

    private final String message;
    private final String code;
    private final String type;
    private final String link;

    public APIError(String message, String code, String type, String link) {
        this.message = message;
        this.code = code;
        this.type = type;
        this.link = link;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return code;
    }

    public String getErrorType() {
        return type;
    }

    public String getErrorLink() {
        return link;
    }

    @Override
    public String toString() {
        return "APIError{"
                + "message='"
                + message
                + '\''
                + ", code='"
                + code
                + '\''
                + ", type='"
                + type
                + '\''
                + ", link='"
                + link
                + '\''
                + '}';
    }
}
