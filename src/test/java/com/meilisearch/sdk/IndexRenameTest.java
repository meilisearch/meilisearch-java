package com.meilisearch.sdk;

import com.meilisearch.sdk.model.TaskInfo;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class IndexRenameTest {

    private MockWebServer mockServer;
    private Config config;
    private IndexesHandler handler;

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();

        String baseUrl = mockServer.url("").toString();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        config = new Config(baseUrl, "masterKey");
        handler = new IndexesHandler(config);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @Test
    void testRenameIndex() throws Exception {
        String responseJson = "{\"taskUid\":123,\"indexUid\":\"indexB\",\"status\":\"enqueued\",\"type\":\"indexUpdate\",\"enqueuedAt\":\"2024-01-01T00:00:00Z\"}";
        mockServer.enqueue(new MockResponse()
            .setResponseCode(202)
            .setBody(responseJson)
            .addHeader("Content-Type", "application/json"));

        TaskInfo result = handler.updateIndexUid("indexA", "indexB");

        assertThat(result, is(notNullValue()));
        assertThat(result.getTaskUid(), is(equalTo(123)));

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod(), equalTo("PATCH"));
        assertThat(request.getPath(), equalTo("/indexes/indexA"));
        assertThat(request.getHeader("Authorization"), equalTo("Bearer masterKey"));

        String requestBody = request.getBody().readUtf8();
        assertThat(requestBody, containsString("\"indexUid\":\"indexB\""));
    }

    @Test
    void testRenameIndexWithDifferentNames() throws Exception {
        String responseJson = "{\"taskUid\":456,\"indexUid\":\"newIndex\",\"status\":\"enqueued\",\"type\":\"indexUpdate\",\"enqueuedAt\":\"2024-01-02T00:00:00Z\"}";
        mockServer.enqueue(new MockResponse()
            .setResponseCode(202)
            .setBody(responseJson)
            .addHeader("Content-Type", "application/json"));

        TaskInfo result = handler.updateIndexUid("oldIndex", "newIndex");

        assertThat(result, is(notNullValue()));
        assertThat(result.getTaskUid(), is(equalTo(456)));

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod(), equalTo("PATCH"));
        assertThat(request.getPath(), equalTo("/indexes/oldIndex"));
        assertThat(request.getHeader("Authorization"), equalTo("Bearer masterKey"));

        String requestBody = request.getBody().readUtf8();
        assertThat(requestBody, containsString("\"indexUid\":\"newIndex\""));
    }
}
