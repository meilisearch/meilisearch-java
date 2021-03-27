package com.meilisearch.sdk.http.factory;

import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.utils.Movie;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class BasicRequestFactoryTest {

	private final RequestFactory factory = new BasicRequestFactory(new GsonJsonHandler(),new Config(""));

	@Test
	void basicUseCase() {
		HttpRequest<?> httpRequest = factory.create(HttpMethod.GET, "/", Collections.emptyMap(), null);
		assertThat(httpRequest.hasContent(), is(false));
		assertThat(httpRequest.getPath(), is("/"));
		assertThat(httpRequest.getMethod(), is(HttpMethod.GET));
	}

	@Test
	void contentString() {
		HttpRequest<?> httpRequest = factory.create(HttpMethod.GET, "/", Collections.emptyMap(), "thisisatest");
		assertThat(httpRequest.hasContent(), is(true));
		assertThat(httpRequest.getContent(), is("thisisatest"));
		assertThat(httpRequest.getPath(), is("/"));
		assertThat(httpRequest.getMethod(), is(HttpMethod.GET));
	}

	@Test
	void contentClass() {
		HttpRequest<?> httpRequest = factory.create(HttpMethod.GET, "/", Collections.emptyMap(), new Movie("thisisanid","thisisatitle"));
		assertThat(httpRequest.hasContent(), is(true));
		assertThat(httpRequest.getContent(), is("{\"id\":\"thisisanid\",\"title\":\"thisisatitle\"}"));
		assertThat(httpRequest.getPath(), is("/"));
		assertThat(httpRequest.getMethod(), is(HttpMethod.GET));
	}
}
