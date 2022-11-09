package com.meilisearch.sdk.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.meilisearch.sdk.http.request.BasicRequest;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.utils.Movie;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class BasicRequestTest {

    private final BasicRequest request = new BasicRequest(new GsonJsonHandler());

    @Test
    void basicUseCase() {
        HttpRequest httpRequest = request.create(HttpMethod.GET, "/", Collections.emptyMap(), null);

        assertFalse(httpRequest.hasContent());
        assertEquals(httpRequest.getPath(), "/");
        assertEquals(httpRequest.getMethod(), HttpMethod.GET);
    }

    @Test
    void contentString() {
        HttpRequest httpRequest =
                request.create(HttpMethod.GET, "/", Collections.emptyMap(), "thisisatest");

        assertTrue(httpRequest.hasContent());
        assertEquals(httpRequest.getContent(), "thisisatest");
        assertEquals(httpRequest.getPath(), "/");
        assertEquals(httpRequest.getMethod(), HttpMethod.GET);
    }

    @Test
    void contentClass() {
        HttpRequest httpRequest =
                request.create(
                        HttpMethod.GET,
                        "/",
                        Collections.emptyMap(),
                        new Movie("thisisanid", "thisisatitle"));

        assertTrue(httpRequest.hasContent());
        assertEquals(
                httpRequest.getContent(), "{\"id\":\"thisisanid\",\"title\":\"thisisatitle\"}");
        assertEquals(httpRequest.getPath(), "/");
        assertEquals(httpRequest.getMethod(), HttpMethod.GET);
    }
}
