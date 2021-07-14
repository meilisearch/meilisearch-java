package com.meilisearch.sdk.http.factory;

import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.request.HttpRequest;
import java.util.Map;

public interface RequestFactory {
    <T> HttpRequest<?> create(
            HttpMethod method, String path, Map<String, String> headers, T content);
}
