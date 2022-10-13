package com.meilisearch.sdk.http.response;

import java.util.Map;
import lombok.Getter;

@Getter
public class HttpResponse {
    private final Map<String, String> headers;
    private final int statusCode;
    private final String content;

    public HttpResponse(Map<String, String> headers, int statusCode, String content) {
        this.headers = headers;
        this.statusCode = statusCode;
        this.content = content;
    }

    public boolean hasContent() {
        return content != null;
    }

    public byte[] getContentAsBytes() {
        return content.getBytes();
    }
}
