package com.meilisearch.sdk.http.request;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BasicHttpRequest implements HttpRequest<String> {
    private HttpMethod method;
    private String path;
    private Map<String, String> headers;
    private String content;

    public BasicHttpRequest() {}

    public BasicHttpRequest(
            HttpMethod method, String path, Map<String, String> headers, String content) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.content = content;
    }

    @Override
    public HttpMethod getMethod() {
        return this.method;
    }

    @Override
    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
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
        return content.getBytes(StandardCharsets.UTF_8);
    }
}
