package com.meilisearch.sdk.http.request;

import java.util.Map;

public interface HttpRequest<T> {
    HttpMethod getMethod();

    void setMethod(HttpMethod method);

    String getPath();

    void setPath(String path);

    Map<String, String> getHeaders();

    void setHeaders(Map<String, String> headers);

    boolean hasContent();

    T getContent();

    byte[] getContentAsBytes();
}
