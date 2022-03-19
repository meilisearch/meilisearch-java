package com.meilisearch.sdk.http.response;

import java.util.Map;
import java.util.concurrent.TimeoutException;

public class BasicHttpResponse implements HttpResponse<String> {
    private final Map<String, String> headers;
    private final int statusCode;
    private final String content;

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
    public String getContent() throws TimeoutException {
        return content;
    }

    @Override
    public byte[] getContentAsBytes() {
        return content.getBytes();
    }
}
