package com.meilisearch.sdk.exceptions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

class MeilisearchTimeoutExceptionTest {

    @Test
    void testToString() {
        MeilisearchTimeoutException classToTest = new MeilisearchTimeoutException("This is a Test");
        assertThat(
                classToTest.toString(),
                is(equalTo("Meilisearch TimeoutException: {Error=This is a Test}")));
    }
}
