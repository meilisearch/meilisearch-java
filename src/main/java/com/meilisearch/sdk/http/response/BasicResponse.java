package com.meilisearch.sdk.http.response;

import com.meilisearch.sdk.json.JsonHandler;

public class BasicResponse {
    private final JsonHandler jsonHandler;

    public BasicResponse(JsonHandler jsonHandler) {
        this.jsonHandler = jsonHandler;
    }

    public <T> HttpResponse create(HttpResponse response, Class<?> targetClass) {
        try {
            return new HttpResponse(
                    response.getHeaders(),
                    response.getStatusCode(),
                    response.getContent() == null
                            ? null
                            : this.jsonHandler.decode(response.getContent(), targetClass));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
