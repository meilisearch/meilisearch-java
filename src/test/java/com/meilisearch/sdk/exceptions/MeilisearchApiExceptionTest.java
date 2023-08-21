package com.meilisearch.sdk.exceptions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

class MeilisearchApiExceptionTest {

    @Test
    void testToString() {
        MeilisearchApiException classToTest = new MeilisearchApiException(new APIError());
        String expected =
                "Meilisearch ApiException: {Error=APIError: {message='null', code='null', type='null', link='null'}}";
        assertThat(classToTest.toString(), is(equalTo(expected)));

        classToTest =
                new MeilisearchApiException(
                        new APIError()
                                .setMessage("Index `movies` not found.")
                                .setCode("index_not_found")
                                .setLink(
                                        "https://www.meilisearch.com/docs/reference/errors/error_codes#index_not_found")
                                .setType("invalid_request"));
        expected =
                "Meilisearch ApiException: {Error=APIError: {message='Index `movies` not found.', code='index_not_found', type='invalid_request', link='https://www.meilisearch.com/docs/reference/errors/error_codes#index_not_found'}}";
        assertThat(classToTest.toString(), is(equalTo(expected)));
    }
}
