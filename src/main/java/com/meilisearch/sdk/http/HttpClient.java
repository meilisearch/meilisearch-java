package com.meilisearch.sdk.http;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.http.response.HttpResponse;

public interface HttpClient<T extends HttpRequest, R extends HttpResponse> {
    R get(T request) throws MeilisearchException;

    R post(T request) throws MeilisearchException;

    R put(T request) throws MeilisearchException;

    R delete(T request) throws MeilisearchException;
}
