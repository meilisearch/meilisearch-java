package com.meilisearch.sdk.http.response;

import java.util.Map;
import lombok.Getter;

/** HttpResponse for Meilisearch Client */
@Getter
public class HttpResponse<T> {
    private final Map<String, String> headers;
    private final int statusCode;
    private final T content;

    public HttpResponse(Map<String, String> headers, int statusCode, T content) {
        this.headers = headers;
        this.statusCode = statusCode;
        this.content = content;
    }

    public boolean hasContent() {
        return content != null;
    }
}
