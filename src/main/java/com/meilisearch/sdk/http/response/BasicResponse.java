package com.meilisearch.sdk.http.response;

import com.meilisearch.sdk.json.JsonHandler;

public class BasicResponse {
    private final JsonHandler jsonHandler;

    public BasicResponse(JsonHandler jsonHandler) {
        this.jsonHandler = jsonHandler;
    }

    public <T> HttpResponse<T> create(
            HttpResponse<T> httpResponse, Class<T> targetClass, Class<?>... parameters) {
        try {
            return new HttpResponse<T>(
                    httpResponse.getHeaders(),
                    httpResponse.getStatusCode(),
                    httpResponse.getContent() == null
                            ? null
                            : this.jsonHandler.decode(
                                    httpResponse.getContent(), targetClass, parameters));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
