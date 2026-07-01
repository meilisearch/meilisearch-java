package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.meilisearch.sdk.model.IndexStatsWithSizeFormat;
import com.meilisearch.sdk.model.StatsQuery;
import com.meilisearch.sdk.model.StatsWithSizeFormat;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatsTest {

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
    void getStatsWithQueryParameters() throws Exception {
        String json =
                """
            {
              "databaseSize": "1.2 KiB",
              "usedDatabaseSize": "1 KiB",
              "lastUpdate": "2019-11-20T09:40:33.711324Z",
              "indexes": {
                "movies": {
                  "numberOfDocuments": 10,
                  "rawDocumentDbSize": "100 B",
                  "maxDocumentSize": "16 B",
                  "avgDocumentSize": "10 B",
                  "isIndexing": true,
                  "fieldDistribution": {
                    "genre": 10
                  },
                  "internalDatabaseSizes": {
                    "documents": "100 B"
                  }
                }
              }
            }
            """;
        StatsQuery query =
                new StatsQuery().setShowInternalDatabaseSizes(true).setSizeFormat("human");

        server.enqueue(new MockResponse().setResponseCode(200).setBody(json));

        StatsWithSizeFormat stats = client.getStats(query);

        assertThat(
                server.takeRequest().getPath(),
                is("//stats?showInternalDatabaseSizes=true&sizeFormat=human"));
        assertThat(stats.getDatabaseSize(), is("1.2 KiB"));
        assertThat(stats.getUsedDatabaseSize(), is("1 KiB"));
        assertThat(stats.getIndexes().get("movies").getRawDocumentDbSize(), is("100 B"));
        assertThat(
                stats.getIndexes().get("movies").getInternalDatabaseSizes().get("documents"),
                is("100 B"));
    }

    @Test
    void getIndexStatsWithQueryParameters() throws Exception {
        String json =
                """
            {
              "numberOfDocuments": 10,
              "rawDocumentDbSize": "100 B",
              "maxDocumentSize": "16 B",
              "avgDocumentSize": "10 B",
              "numberOfEmbeddings": 2,
              "numberOfEmbeddedDocuments": 1,
              "isIndexing": false,
              "fieldDistribution": {
                "genre": 10
              },
              "internalDatabaseSizes": {
                "documents": "100 B"
              }
            }
            """;
        StatsQuery query =
                new StatsQuery().setShowInternalDatabaseSizes(true).setSizeFormat("human");

        server.enqueue(new MockResponse().setResponseCode(200).setBody(json));

        IndexStatsWithSizeFormat stats = client.index("movies").getStats(query);

        assertThat(
                server.takeRequest().getPath(),
                is("//indexes/movies/stats?showInternalDatabaseSizes=true&sizeFormat=human"));
        assertThat(stats.getRawDocumentDbSize(), is("100 B"));
        assertThat(stats.getAvgDocumentSize(), is("10 B"));
        assertThat(stats.getMaxDocumentSize(), is("16 B"));
        assertThat(stats.getInternalDatabaseSizes().get("documents"), is("100 B"));
        assertThat(stats.getNumberOfEmbeddings(), is(2L));
        assertThat(stats.getNumberOfEmbeddedDocuments(), is(1L));
    }

    @Test
    void statsQuerySerializesParameters() {
        StatsQuery query =
                new StatsQuery().setShowInternalDatabaseSizes(true).setSizeFormat("raw");

        assertThat(query.toQuery(), is("?showInternalDatabaseSizes=true&sizeFormat=raw"));
    }
}
