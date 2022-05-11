package com.meilisearch.sdk.http.request;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BasicHttpRequest implements HttpRequest<String> {
    @Setter private HttpMethod method;
    @Setter private String path;
    @Setter private Map<String, String> headers;
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
    public boolean hasContent() {
        return content != null;
    }

    @Override
    public byte[] getContentAsBytes() {
        return content.getBytes(StandardCharsets.UTF_8);
    }
}
