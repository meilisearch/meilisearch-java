package com.meilisearch.sdk.http.request;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpRequest {
    @Setter private HttpMethod method;
    @Setter private String path;
    @Getter private Map<String, String> headers;
    private String content;

    public HttpRequest() {}

    public HttpRequest(
            HttpMethod method, String path, Map<String, String> headers, String content) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.content = content;
    }

    public boolean hasContent() {
        return content != null;
    }

    public byte[] getContentAsBytes() {
        return content.getBytes(StandardCharsets.UTF_8);
    }
}
