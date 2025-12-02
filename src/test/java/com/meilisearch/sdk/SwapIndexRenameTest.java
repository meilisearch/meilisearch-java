package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.meilisearch.sdk.model.SwapIndexesParams;
import com.meilisearch.sdk.model.TaskInfo;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SwapIndexRenameTest {

    private MockWebServer server;
    private Client client;

    @BeforeEach
    void setup() throws Exception {
        server = new MockWebServer();
        server.start();

        client = new Client(new Config(server.url("/").toString(), "masterKey"));
    }

    @AfterEach
    void teardown() throws Exception {
        server.shutdown();
    }

    @Test
    void testSwapIndexesWithRename() throws Exception {
        String jsonResponse = "{ \"taskUid\": 555 }";

        server.enqueue(new MockResponse().setBody(jsonResponse).setResponseCode(202));

        SwapIndexesParams params =
                new SwapIndexesParams()
                        .setIndexes(new String[] {"indexA", "indexB"})
                        .setRename(true);

        TaskInfo task = client.swapIndexes(new SwapIndexesParams[] {params});

        assertThat(task, notNullValue());
        assertThat(task.getTaskUid(), equalTo(555));

        RecordedRequest req = server.takeRequest();

        assertThat(req.getMethod(), equalTo("POST"));
        // FIX: Actual path contains double slash
        assertThat(req.getPath(), equalTo("//swap-indexes"));

        String body = req.getBody().readUtf8();
        assertThat(body, containsString("\"indexes\":[\"indexA\",\"indexB\"]"));
        assertThat(body, containsString("\"rename\":true"));
    }
}
