package com.meilisearch.sdk.http.response;

import java.util.Map;
import lombok.Getter;

public class BasicHttpResponse implements HttpResponse<String> {
    @Getter private final Map<String, String> headers;
    @Getter private final int statusCode;
    @Getter private final String content;

    public BasicHttpResponse(Map<String, String> headers, int statusCode, String content) {
        this.headers = headers;
        this.statusCode = statusCode;
        this.content = content;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public boolean hasContent() {
        return content != null;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public byte[] getContentAsBytes() {
        return content.getBytes();
    }
}
