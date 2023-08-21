package com.meilisearch.sdk.exceptions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

class MeilisearchCommunicationExceptionTest {

    @Test
    void testToString() {
        MeilisearchCommunicationException classToTest =
                new MeilisearchCommunicationException("This is a Test");
        assertThat(
                classToTest.toString(),
                is(equalTo("Meilisearch CommunicationException: {Error=This is a Test}")));
    }
}
