package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.TaskError;
import com.meilisearch.sdk.exceptions.APIError;
import com.meilisearch.sdk.exceptions.MeilisearchApiException;
import com.meilisearch.sdk.exceptions.MeilisearchCommunicationException;
import org.junit.jupiter.api.*;

@Tag("integration")
public class ExceptionsTest extends AbstractIT {

    @BeforeEach
    public void initialize() {
        this.setUp();
    }

    @AfterAll
    static void cleanMeiliSearch() {
        cleanup();
    }

    @Test
    public void testMeilisearchCommunicationException() throws Exception {
        String indexUid = "MeilisearchCommunicationException";
        Client wrongClient = new Client(new Config("http://wrongurl:1234", "masterKey"));

        assertThrows(MeilisearchCommunicationException.class, () -> wrongClient.getIndex(indexUid));
    }

    /** Test MeilisearchApiException serialization and getters */
    @Test
    public void testErrorSerializeAndGetters() {
        String message = "You must have an authorization token";
        String code = "missing_authorization_header";
        String type = "authentication_error";
        String link = "https://docs.meilisearch.com/errors#missing_authorization_header";
        try {
            throw new MeilisearchApiException(new APIError(message, code, type, link));
        } catch (MeilisearchApiException e) {
            assertEquals(message, e.getMessage());
            assertEquals(code, e.getCode());
            assertEquals(type, e.getType());
            assertEquals(link, e.getLink());
        }
    }

    /** Test Task Error Getters */
    @Test
    public void testTaskErrorGetters() {
        TaskError error = new TaskError();
        error.setTaskErrorCode("wrong field");
        assertEquals("wrong field", error.getTaskErrorCode());
    }

    /** Test MeilisearchApiException is thrown on Meilisearch bad request */
    @Test
    public void testMeilisearchApiExceptionBadRequest() throws Exception {
        String indexUid = "MeilisearchApiExceptionBadRequest";
        assertThrows(MeilisearchApiException.class, () -> client.getIndex(indexUid));
        try {
            client.getIndex(indexUid);
        } catch (MeilisearchApiException e) {
            assertEquals("index_not_found", e.getCode());
        }
    }
}
