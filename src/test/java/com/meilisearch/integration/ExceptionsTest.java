package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.sdk.UpdateStatusError;
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
    static void cleanMeiliSearch() {
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
            assertEquals(code, e.getErrorCode());
            assertEquals(type, e.getErrorType());
            assertEquals(link, e.getErrorLink());
        }
    }

    /** Test UpdateStatus Error Getters */
    @Test
    public void testUpdateStatusErrorGetters() {
        UpdateStatusError error = new UpdateStatusError();
        error.setUpdateStatusErrorCode("wrong field");
        assertEquals("wrong field", error.getUpdateStatusErrorCode());
    }

    /** Test MeiliSearchApiException is thrown on Meilisearch bad request */
    @Test
    public void testMeiliSearchApiExceptionBadRequest() throws Exception {
        String indexUid = "MeiliSearchApiExceptionBadRequest";
        client.createIndex(indexUid);
        assertThrows(MeiliSearchApiException.class, () -> client.createIndex(indexUid));
        try {
            client.createIndex(indexUid);
        } catch (MeiliSearchApiException e) {
            assertEquals("index_already_exists", e.getErrorCode());
        }
    }
}
