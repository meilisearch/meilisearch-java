package com.meilisearch.sdk.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MeilisearchCommunicationExceptionTest {

    @Test
    void testToString() {
        MeilisearchCommunicationException classToTest =
                new MeilisearchCommunicationException("This is a Test");
        assertEquals(
                "Meilisearch CommunicationException: {Error=This is a Test}",
                classToTest.toString());
    }
}
