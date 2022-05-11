package com.meilisearch.sdk.http.response;

import java.util.Map;
import lombok.Getter;

@Getter
public class BasicHttpResponse implements HttpResponse<String> {
    private final Map<String, String> headers;
    private final int statusCode;
    private final String content;

    public BasicHttpResponse(Map<String, String> headers, int statusCode, String content) {
        this.headers = headers;
        this.statusCode = statusCode;
        this.content = content;
    }

    @Override
    public boolean hasContent() {
        return content != null;
    }

    @Override
    public byte[] getContentAsBytes() {
        return content.getBytes();
    }
}
