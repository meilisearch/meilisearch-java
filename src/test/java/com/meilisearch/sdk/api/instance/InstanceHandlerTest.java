package com.meilisearch.sdk.api.instance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.meilisearch.sdk.GenericServiceTemplate;
import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;
import com.meilisearch.sdk.http.AbstractHttpClient;
import com.meilisearch.sdk.http.factory.BasicRequestFactory;
import com.meilisearch.sdk.http.factory.RequestFactory;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.http.response.BasicHttpResponse;
import com.meilisearch.sdk.json.JacksonJsonHandler;
import com.meilisearch.sdk.json.JsonHandler;
import java.util.Map;
import org.junit.jupiter.api.Test;

class InstanceHandlerTest {

    private final AbstractHttpClient client = mock(AbstractHttpClient.class);
    private final JsonHandler handler = new JacksonJsonHandler();
    private final RequestFactory requestFactory = new BasicRequestFactory(handler);
    private final GenericServiceTemplate serviceTemplate =
            new GenericServiceTemplate(client, handler, requestFactory);
    InstanceHandler classToTest = new InstanceHandler(serviceTemplate, requestFactory);

    @Test
    void isHealthy() throws Exception {
        when(client.get(any(HttpRequest.class)))
                .thenAnswer(
                        invocation ->
                                new BasicHttpResponse(null, 200, "{\"status\":\"available\"}"))
                .thenThrow(MeiliSearchRuntimeException.class);
        assertTrue(classToTest.isHealthy());
        assertFalse(classToTest.isHealthy());
    }

    @Test
    void health() throws Exception {
        when(client.get(any(HttpRequest.class)))
                .thenAnswer(
                        invocation ->
                                new BasicHttpResponse(null, 200, "{\"status\":\"available\"}"))
                .thenThrow(MeiliSearchRuntimeException.class);
        Map<String, String> health = classToTest.health();
        assertNotNull(health);
        assertTrue(health.containsKey("status"));
        assertEquals("available", health.get("status"));
    }

    @Test
    void version() throws Exception {
        when(client.get(any(HttpRequest.class)))
                .thenAnswer(
                        invocation ->
                                new BasicHttpResponse(
                                        null,
                                        200,
                                        "{\"commitSha\":\"b46889b5f0f2f8b91438a08a358ba8f05fc09fc1\",\"commitDate\":\"2019-11-15T09:51:54.278247+00:00\",\"pkgVersion\":\"0.1.1\"}"))
                .thenThrow(MeiliSearchRuntimeException.class);
        Map<String, String> version = classToTest.getVersion();
        assertNotNull(version);
        assertTrue(version.containsKey("commitSha"));
        assertEquals("b46889b5f0f2f8b91438a08a358ba8f05fc09fc1", version.get("commitSha"));
        assertTrue(version.containsKey("commitDate"));
        assertEquals("2019-11-15T09:51:54.278247+00:00", version.get("commitDate"));
        assertTrue(version.containsKey("pkgVersion"));
        assertEquals("0.1.1", version.get("pkgVersion"));
        version = classToTest.getVersion();
        assertNotNull(version);
        assertEquals(0, version.size());
    }

    @Test
    void fullStats() throws Exception {
        when(client.get(any(HttpRequest.class)))
                .thenAnswer(
                        invocation ->
                                new BasicHttpResponse(
                                        null,
                                        200,
                                        "{\"databaseSize\": 7701901318,\"lastUpdate\": \"2019-11-15T11:15:22.092896Z\",\"indexes\": {\"movies\": {\"numberOfDocuments\": 19654,\"isIndexing\": false,\"fieldDistribution\": {\"poster\": 19654,\"overview\": 19654,\"title\": 19654,\"id\": 19654,\"release_date\": 19654}},\"rangemovies\": {\"numberOfDocuments\": 19654,\"isIndexing\": false,\"fieldDistribution\": {\"overview\": 19654,\"id\": 19654,\"title\": 19654}}}}"))
                .thenThrow(MeiliSearchRuntimeException.class);
        Stats stats = classToTest.getStats();
        assertThat(stats.getDatabaseSize(), is(7701901318L));
        assertThat(stats.getLastUpdate().toInstant().toEpochMilli(), is(1573816522092L));
        assertThat(stats.getIndexes().keySet(), hasItems("movies", "rangemovies"));
        IndexStats movies = stats.getIndexes().get("movies");
        assertThat(movies.getNumberOfDocuments(), is(19654L));
        assertThat(movies.isIndexing(), is(false));
        assertThat(movies.getFieldDistribution().keySet(), hasItems("overview", "id", "title"));
        assertThat(movies.getFieldDistribution().get("overview"), is(19654));
        assertThat(movies.getFieldDistribution().get("id"), is(19654));
        assertThat(movies.getFieldDistribution().get("title"), is(19654));
    }

    @Test
    void singleStats() throws Exception {
        when(client.get(any(HttpRequest.class)))
                .thenAnswer(
                        invocation ->
                                new BasicHttpResponse(
                                        null,
                                        200,
                                        "{\"numberOfDocuments\": 19654,\"isIndexing\": false,\"fieldDistribution\": {\"poster\": 19654,\"release_date\": 19654,\"title\": 19654,\"id\": 19654,\"overview\": 19654}}"))
                .thenThrow(MeiliSearchRuntimeException.class);
        IndexStats stats = classToTest.getStats("index");
        assertThat(stats.getNumberOfDocuments(), is(19654L));
        assertThat(stats.isIndexing(), is(false));
        assertThat(
                stats.getFieldDistribution().keySet(),
                hasItems("overview", "id", "title", "release_date", "poster"));
        assertThat(stats.getFieldDistribution().get("overview"), is(19654));
        assertThat(stats.getFieldDistribution().get("id"), is(19654));
        assertThat(stats.getFieldDistribution().get("title"), is(19654));
    }
}
