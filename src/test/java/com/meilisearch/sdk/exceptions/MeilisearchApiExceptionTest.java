package com.meilisearch.sdk.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MeilisearchApiExceptionTest {

    @Test
    void testToString() {
        MeilisearchApiException classToTest = new MeilisearchApiException(new APIError());
        assertEquals(
                "Meilisearch ApiException: {Error=APIError: {message='null', code='null', type='null', link='null'}}",
                classToTest.toString());

        classToTest =
                new MeilisearchApiException(
                        new APIError()
                                .setMessage("Index `movies` not found.")
                                .setCode("index_not_found")
                                .setLink("https://www.meilisearch.com/docs/reference/errors/error_codes#index_not_found")
                                .setType("invalid_request"));
        assertEquals(
                "Meilisearch ApiException: {Error=APIError: {message='Index `movies` not found.', code='index_not_found', type='invalid_request', link='https://www.meilisearch.com/docs/reference/errors/error_codes#index_not_found'}}",
                classToTest.toString());
    }
}
