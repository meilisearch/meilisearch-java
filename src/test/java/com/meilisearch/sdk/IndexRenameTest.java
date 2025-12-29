package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.meilisearch.sdk.model.TaskInfo;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IndexRenameTest {

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
    void testRenameIndex() throws Exception {
        String response = "{ \"taskUid\": 123 }";

        server.enqueue(new MockResponse().setBody(response).setResponseCode(202));

        TaskInfo task = client.updateIndex("oldIndex", null, "newIndex");

        assertThat(task, notNullValue());
        assertThat(task.getTaskUid(), equalTo(123));

        RecordedRequest req = server.takeRequest();

        assertThat(req.getMethod(), equalTo("PATCH"));
        assertThat(req.getPath(), equalTo("//indexes/oldIndex"));

        String body = req.getBody().readUtf8();
        assertThat(body, containsString("\"uid\":\"newIndex\""));
        assertThat(req.getHeader("Authorization"), equalTo("Bearer masterKey"));
    }
}
