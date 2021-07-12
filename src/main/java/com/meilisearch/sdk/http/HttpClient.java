package com.meilisearch.sdk.http;

import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.http.response.HttpResponse;

public interface HttpClient<T extends HttpRequest<?>, R extends HttpResponse<?>> {
    R get(T request) throws Exception;

    R post(T request) throws Exception;

    R put(T request) throws Exception;

    R delete(T request) throws Exception;
}
