package com.meilisearch.sdk.http.request;

import com.meilisearch.sdk.json.JsonHandler;
import java.util.Map;

public class BasicRequest {
    private final JsonHandler jsonHandler;

    public BasicRequest(JsonHandler jsonHandler) {
        this.jsonHandler = jsonHandler;
    }

    public <T> HttpRequest create(
            HttpMethod method, String path, Map<String, String> headers, T content) {
        try {
            return new HttpRequest(
                    method,
                    path,
                    headers,
                    content == null ? null : this.jsonHandler.encode(content));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
