package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.meilisearch.sdk.model.SearchResult;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchResultQueryVectorTest {

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
    void testQueryVectorDeserialization() throws Exception {
        String json =
                """
            {
              "hits": [],
              "queryVector": [1.1, -2.5, 3.14],
              "offset": 0,
              "limit": 20,
              "estimatedTotalHits": 0,
              "query": "hello",
              "processingTimeMs": 1
            }
            """;

        server.enqueue(new MockResponse().setResponseCode(200).setBody(json));

        SearchResult res = client.index("movies").search("hello");

        assertThat(res, notNullValue());
        assertThat(res.getQueryVector(), hasSize(3));
        assertThat(res.getQueryVector().get(0), equalTo(1.1f));
        assertThat(res.getQueryVector().get(1), equalTo(-2.5f));
        assertThat(res.getQueryVector().get(2), equalTo(3.14f));
    }
}
