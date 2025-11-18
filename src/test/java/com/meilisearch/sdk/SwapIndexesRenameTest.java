package com.meilisearch.sdk;

import com.meilisearch.sdk.model.SwapIndexesParams;
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

public class SwapIndexesRenameTest {

    private MockWebServer mockServer;
    private Config config;
    private Client client;

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();

        String baseUrl = mockServer.url("").toString();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        config = new Config(baseUrl, "masterKey");
        client = new Client(config);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @Test
    void testSwapIndexesWithRename() throws Exception {
        String responseJson = "{\"taskUid\":789,\"status\":\"enqueued\",\"type\":\"indexSwap\",\"enqueuedAt\":\"2024-01-01T00:00:00Z\"}";
        mockServer.enqueue(new MockResponse()
            .setResponseCode(202)
            .setBody(responseJson)
            .addHeader("Content-Type", "application/json"));

        SwapIndexesParams[] params = {
            new SwapIndexesParams()
                .setIndexes(new String[]{"indexA", "indexB"})
                .setRename(true)
        };

        TaskInfo result = client.swapIndexes(params);

        assertThat(result, is(notNullValue()));
        assertThat(result.getTaskUid(), is(equalTo(789)));

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod(), equalTo("POST"));
        assertThat(request.getPath(), equalTo("/swap-indexes"));
        assertThat(request.getHeader("Authorization"), equalTo("Bearer masterKey"));

        String requestBody = request.getBody().readUtf8();
        assertThat(requestBody, containsString("\"rename\":true"));
        assertThat(requestBody, containsString("\"indexes\""));
        assertThat(requestBody, containsString("\"indexA\""));
        assertThat(requestBody, containsString("\"indexB\""));
    }

    @Test
    void testSwapIndexesWithoutRename() throws Exception {
        String responseJson = "{\"taskUid\":790,\"status\":\"enqueued\",\"type\":\"indexSwap\",\"enqueuedAt\":\"2024-01-02T00:00:00Z\"}";
        mockServer.enqueue(new MockResponse()
            .setResponseCode(202)
            .setBody(responseJson)
            .addHeader("Content-Type", "application/json"));

        SwapIndexesParams[] params = {
            new SwapIndexesParams()
                .setIndexes(new String[]{"indexC", "indexD"})
                .setRename(false)
        };

        TaskInfo result = client.swapIndexes(params);

        assertThat(result, is(notNullValue()));
        assertThat(result.getTaskUid(), is(equalTo(790)));

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod(), equalTo("POST"));
        assertThat(request.getPath(), equalTo("/swap-indexes"));

        String requestBody = request.getBody().readUtf8();
        assertThat(requestBody, containsString("\"rename\":false"));
        assertThat(requestBody, containsString("\"indexC\""));
        assertThat(requestBody, containsString("\"indexD\""));
    }

    @Test
    void testSwapMultipleIndexPairs() throws Exception {
        String responseJson = "{\"taskUid\":791,\"status\":\"enqueued\",\"type\":\"indexSwap\",\"enqueuedAt\":\"2024-01-03T00:00:00Z\"}";
        mockServer.enqueue(new MockResponse()
            .setResponseCode(202)
            .setBody(responseJson)
            .addHeader("Content-Type", "application/json"));

        SwapIndexesParams[] params = {
            new SwapIndexesParams()
                .setIndexes(new String[]{"indexA", "indexB"})
                .setRename(true),
            new SwapIndexesParams()
                .setIndexes(new String[]{"indexC", "indexD"})
                .setRename(false)
        };

        TaskInfo result = client.swapIndexes(params);

        assertThat(result, is(notNullValue()));
        assertThat(result.getTaskUid(), is(equalTo(791)));

        RecordedRequest request = mockServer.takeRequest();
        String requestBody = request.getBody().readUtf8();

        assertThat(requestBody, containsString("\"indexA\""));
        assertThat(requestBody, containsString("\"indexB\""));
        assertThat(requestBody, containsString("\"indexC\""));
        assertThat(requestBody, containsString("\"indexD\""));
        assertThat(requestBody, startsWith("["));
        assertThat(requestBody, endsWith("]"));
    }
}
