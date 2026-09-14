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

    @BeforeEach
    void setup() throws Exception {
        server = new MockWebServer();
        server.start();

        Client client = new Client(new Config(server.url("/").toString(), "masterKey"));
        index = client.index("movies");
    }

    @AfterEach
    void teardown() throws Exception {
        server.shutdown();
    }

    @Test
    void addDocumentsIncludesSkipCreation() throws Exception {
        server.enqueue(new MockResponse().setBody("{\"taskUid\": 1}").setResponseCode(202));

        index.addDocuments("[]", true);

        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod(), equalTo("POST"));
        assertThat(request.getPath(), equalTo("//indexes/movies/documents?skipCreation=true"));
    }

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

    @Test
    void existingDocumentMethodsOmitSkipCreation() throws Exception {
        server.enqueue(new MockResponse().setBody("{\"taskUid\": 3}").setResponseCode(202));

        index.addDocuments("[]");

        RecordedRequest request = server.takeRequest();
        assertThat(request.getPath(), equalTo("//indexes/movies/documents"));
    }
}
