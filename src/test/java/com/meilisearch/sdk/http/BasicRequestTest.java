package com.meilisearch.sdk.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

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

        assertThat(httpRequest.hasContent(), is(equalTo(false)));
        assertThat(httpRequest.getPath(), is(equalTo("/")));
        assertThat(httpRequest.getMethod(), is(equalTo(HttpMethod.GET)));
    }

    @Test
    void contentString() {
        HttpRequest httpRequest =
                request.create(HttpMethod.GET, "/", Collections.emptyMap(), "thisisatest");

        assertThat(httpRequest.hasContent(), is(equalTo(true)));
        assertThat(httpRequest.getContent(), is(equalTo("thisisatest")));
        assertThat(httpRequest.getPath(), is(equalTo("/")));
        assertThat(httpRequest.getMethod(), is(equalTo(HttpMethod.GET)));
    }

    @Test
    void contentClass() {
        HttpRequest httpRequest =
                request.create(
                        HttpMethod.GET,
                        "/",
                        Collections.emptyMap(),
                        new Movie("thisisanid", "thisisatitle"));

        assertThat(httpRequest.hasContent(), is(equalTo(true)));
        assertThat(
                httpRequest.getContent(),
                is(equalTo("{\"id\":\"thisisanid\",\"title\":\"thisisatitle\"}")));
        assertThat(httpRequest.getPath(), is(equalTo("/")));
        assertThat(httpRequest.getMethod(), is(equalTo(HttpMethod.GET)));
    }
}
