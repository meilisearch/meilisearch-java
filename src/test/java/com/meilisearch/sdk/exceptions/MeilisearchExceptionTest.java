package com.meilisearch.sdk.exceptions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.net.MalformedURLException;
import org.junit.jupiter.api.Test;

class MeilisearchExceptionTest {

    @Test
    void testToString() {
        MeilisearchException classToTest = new MeilisearchException("This is a Test");
        String expected =
                "Meilisearch Exception: {com.meilisearch.sdk.exceptions.MeilisearchException. Error=This is a Test}";
        assertThat(classToTest.toString(), is(equalTo(expected)));

        classToTest = new MeilisearchException(new MalformedURLException());
        expected =
                "Meilisearch Exception: {java.net.MalformedURLException. Error=java.net.MalformedURLException}";
        assertThat(classToTest.toString(), is(equalTo(expected)));
    }
}
