package com.meilisearch.sdk.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MeilisearchTimeoutExceptionTest {

    @Test
    void testToString() {
        MeilisearchTimeoutException classToTest = new MeilisearchTimeoutException("This is a Test");
        assertEquals(
                "Meilisearch TimeoutException: {Error=This is a Test}", classToTest.toString());
    }
}
