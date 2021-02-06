package com.meilisearch.sdk.api.instance;

import com.meilisearch.sdk.GenericServiceTemplate;
import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;
import com.meilisearch.sdk.http.AbstractHttpClient;
import com.meilisearch.sdk.http.factory.BasicRequestFactory;
import com.meilisearch.sdk.http.factory.RequestFactory;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.http.response.BasicHttpResponse;
import com.meilisearch.sdk.json.JacksonJsonHandler;
import com.meilisearch.sdk.json.JsonHandler;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstanceHandlerTest {

	private final AbstractHttpClient client = mock(AbstractHttpClient.class);
	private final JsonHandler handler = new JacksonJsonHandler();
	private final RequestFactory requestFactory = new BasicRequestFactory(handler);
	private final GenericServiceTemplate serviceTemplate = new GenericServiceTemplate(client, handler, requestFactory);
	InstanceHandler classToTest = new InstanceHandler(serviceTemplate, requestFactory);

	@Test
	void isHealthy() throws Exception {
		when(client.get(any(HttpRequest.class))).thenAnswer(invocation -> new BasicHttpResponse(null, 200, "")).thenThrow(MeiliSearchRuntimeException.class);
		assertTrue(classToTest.isHealthy());
		assertFalse(classToTest.isHealthy());
	}

	@Test
	void version() throws Exception {
		when(client.get(any(HttpRequest.class)))
			.thenAnswer(invocation -> new BasicHttpResponse(null, 200, "{\"commitSha\":\"b46889b5f0f2f8b91438a08a358ba8f05fc09fc1\",\"buildDate\":\"2019-11-15T09:51:54.278247+00:00\",\"pkgVersion\":\"0.1.1\"}"))
			.thenThrow(MeiliSearchRuntimeException.class);
		Map<String, String> version = classToTest.getVersion();
		assertNotNull(version);
		assertTrue(version.containsKey("commitSha"));
		assertEquals("b46889b5f0f2f8b91438a08a358ba8f05fc09fc1", version.get("commitSha"));
		assertTrue(version.containsKey("buildDate"));
		assertEquals("2019-11-15T09:51:54.278247+00:00", version.get("buildDate"));
		assertTrue(version.containsKey("pkgVersion"));
		assertEquals("0.1.1", version.get("pkgVersion"));
		version = classToTest.getVersion();
		assertNotNull(version);
		assertEquals(0, version.size());
	}

	@Test
	void fullStats() throws Exception {
		when(client.get(any(HttpRequest.class)))
			.thenAnswer(invocation -> new BasicHttpResponse(null, 200, "{\"databaseSize\": 447819776,\"lastUpdate\": \"2019-11-15T11:15:22.092896Z\",\"indexes\": {\"movies\": {\"numberOfDocuments\": 19654,\"isIndexing\": false,\"fieldsDistribution\": {\"poster\": 19654,\"overview\": 19654,\"title\": 19654,\"id\": 19654,\"release_date\": 19654}},\"rangemovies\": {\"numberOfDocuments\": 19654,\"isIndexing\": false,\"fieldsDistribution\": {\"overview\": 19654,\"id\": 19654,\"title\": 19654}}}}"))
			.thenThrow(MeiliSearchRuntimeException.class);
		Stats stats = classToTest.getStats();
		assertThat(stats.getDatabaseSize(), is(447819776));
		assertThat(stats.getLastUpdate().toInstant().toEpochMilli(), is(1573816522092L));
		assertThat(stats.getIndexes().keySet(), hasItems("movies", "rangemovies"));
		IndexStats movies = stats.getIndexes().get("movies");
		assertThat(movies.getNumberOfDocuments(), is(19654));
		assertThat(movies.getIsIndexing(), is(false));
		assertThat(movies.getFieldsDistribution().keySet(), hasItems("overview", "id", "title"));
		assertThat(movies.getFieldsDistribution().get("overview"), is(19654));
		assertThat(movies.getFieldsDistribution().get("id"), is(19654));
		assertThat(movies.getFieldsDistribution().get("title"), is(19654));
	}
}
