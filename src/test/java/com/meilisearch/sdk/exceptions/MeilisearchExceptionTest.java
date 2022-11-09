package com.meilisearch.sdk.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import org.junit.jupiter.api.Test;

class MeilisearchExceptionTest {

    @Test
    void testToString() {
        MeilisearchException classToTest = new MeilisearchException("This is a Test");
        assertEquals(
                "Meilisearch Exception: {com.meilisearch.sdk.exceptions.MeilisearchException. Error=This is a Test}",
                classToTest.toString());

        classToTest = new MeilisearchException(new MalformedURLException());
        assertEquals(
                "Meilisearch Exception: {java.net.MalformedURLException. Error=java.net.MalformedURLException}",
                classToTest.toString());
    }
}
