package com.meilisearch.sdk.http.request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.utils.Movie;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class BasicRequestTest {

    private final BasicRequest request = new BasicRequest(new GsonJsonHandler());

    @Test
    void basicUseCase() {
        HttpRequest httpRequest = request.create(HttpMethod.GET, "/", Collections.emptyMap(), null);
        assertThat(httpRequest.hasContent(), is(false));
        assertThat(httpRequest.getPath(), is("/"));
        assertThat(httpRequest.getMethod(), is(HttpMethod.GET));
    }

    @Test
    void contentString() {
        HttpRequest httpRequest =
                request.create(HttpMethod.GET, "/", Collections.emptyMap(), "thisisatest");
        assertThat(httpRequest.hasContent(), is(true));
        assertThat(httpRequest.getContent(), is("thisisatest"));
        assertThat(httpRequest.getPath(), is("/"));
        assertThat(httpRequest.getMethod(), is(HttpMethod.GET));
    }

    @Test
    void contentClass() {
        HttpRequest httpRequest =
                request.create(
                        HttpMethod.GET,
                        "/",
                        Collections.emptyMap(),
                        new Movie("thisisanid", "thisisatitle"));
        assertThat(httpRequest.hasContent(), is(true));
        assertThat(
                httpRequest.getContent(), is("{\"id\":\"thisisanid\",\"title\":\"thisisatitle\"}"));
        assertThat(httpRequest.getPath(), is("/"));
        assertThat(httpRequest.getMethod(), is(HttpMethod.GET));
    }
}
