package com.meilisearch.sdk.http.response;

import java.util.Map;
import java.util.concurrent.TimeoutException;

public interface HttpResponse<B> {
    Map<String, String> getHeaders();

    int getStatusCode();

    boolean hasContent();

    B getContent() throws TimeoutException;

    byte[] getContentAsBytes();
}
