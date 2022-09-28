package com.meilisearch.sdk.http;

import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.http.response.HttpResponse;

public abstract class AbstractHttpClient implements HttpClient<HttpRequest, HttpResponse> {
    protected final Config config;

    public AbstractHttpClient(Config config) {
        this.config = config;
    }
}
