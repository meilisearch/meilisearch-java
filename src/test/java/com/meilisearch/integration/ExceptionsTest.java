package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.sdk.TaskError;
import com.meilisearch.sdk.exceptions.APIError;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;
import org.junit.jupiter.api.*;

@Tag("integration")
public class ExceptionsTest extends AbstractIT {

    @BeforeEach
    public void initialize() {
        this.setUp();
    }

    @AfterAll
    static void cleanMeilisearch() {
        cleanup();
    }

    /** Test MeiliSearchApiException serialization and getters */
    @Test
    public void testErrorSerializeAndGetters() {
        String message = "You must have an authorization token";
        String code = "missing_authorization_header";
        String type = "authentication_error";
        String link = "https://docs.meilisearch.com/errors#missing_authorization_header";
        try {
            throw new MeiliSearchApiException(new APIError(message, code, type, link));
        } catch (MeiliSearchApiException e) {
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

    /** Test MeiliSearchApiException is thrown on Meilisearch bad request */
    @Test
    public void testMeiliSearchApiExceptionBadRequest() throws Exception {
        String indexUid = "MeiliSearchApiExceptionBadRequest";
        assertThrows(MeiliSearchApiException.class, () -> client.getIndex(indexUid));
        try {
            client.getIndex(indexUid);
        } catch (MeiliSearchApiException e) {
            assertEquals("index_not_found", e.getCode());
        }
    }
}
