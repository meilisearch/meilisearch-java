package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentsSkipCreationTest {

    private MockWebServer server;
    private Index index;

    /**
     * Starts an isolated HTTP server so requests can be inspected without a Meilisearch instance.
     */
    @BeforeEach
    void setup() throws Exception {
        server = new MockWebServer();
        server.start();

        Client client = new Client(new Config(server.url("/").toString(), "masterKey"));
        index = client.index("movies");
    }

    /** Releases the server and its connections after each test. */
    @AfterEach
    void teardown() throws Exception {
        server.shutdown();
    }

    /** Verifies the short add/replace overload sends an explicit true value. */
    @Test
    void addDocumentsIncludesSkipCreation() throws Exception {
        server.enqueue(new MockResponse().setBody("{\"taskUid\": 1}").setResponseCode(202));

        index.addDocuments("[]", true);

        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod(), equalTo("POST"));
        assertThat(request.getPath(), equalTo("//indexes/movies/documents?skipCreation=true"));
    }

    /** Verifies false is not omitted and existing update parameters are preserved. */
    @Test
    void updateDocumentsIncludesSkipCreationAndExistingParameters() throws Exception {
        server.enqueue(new MockResponse().setBody("{\"taskUid\": 2}").setResponseCode(202));

        index.updateDocuments("[]", "id", ";", "import", false);

        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod(), equalTo("PUT"));
        assertThat(
                request.getPath(),
                equalTo(
                        "//indexes/movies/documents?primaryKey=id&csvDelimiter=;&customMetadata=import&skipCreation=false"));
    }

    /** Verifies legacy overloads leave skipCreation unspecified for backward compatibility. */
    @Test
    void existingDocumentMethodsOmitSkipCreation() throws Exception {
        server.enqueue(new MockResponse().setBody("{\"taskUid\": 3}").setResponseCode(202));

        index.addDocuments("[]");

        RecordedRequest request = server.takeRequest();
        assertThat(request.getPath(), equalTo("//indexes/movies/documents"));
    }
}
