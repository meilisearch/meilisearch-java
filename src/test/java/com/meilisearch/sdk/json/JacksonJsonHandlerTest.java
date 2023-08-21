package com.meilisearch.sdk.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.utils.Movie;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

class JacksonJsonHandlerTest {

    private final ObjectMapper mapper = spy(new ObjectMapper());
    private final JacksonJsonHandler classToTest = new JacksonJsonHandler(mapper);

    @Test
    void serialize() throws Exception {
        assertThat(classToTest.encode("test"), is(equalTo("test")));
        when(mapper.writeValueAsString(any(Movie.class)))
                .thenThrow(new RuntimeException("Oh boy!"));
        assertThrows(RuntimeException.class, () -> classToTest.encode(new Movie()));
    }

    @Test
    void deserialize() {
        assertDoesNotThrow(() -> classToTest.decode("{}", Movie.class));
        assertDoesNotThrow(() -> classToTest.decode("{}", Movie.class, (Class<?>[]) null));
        assertDoesNotThrow(() -> classToTest.decode("{}", Movie.class, new Class[0]));
    }

    @Test
    void deserializeString() throws Exception {
        String content = "{}";
        assertThat(classToTest.decode(content, String.class), is(equalTo(content)));
    }

    @Test
    void deserializeBodyNull() {
        assertThrows(Exception.class, () -> classToTest.decode(null, List.class, String.class));
    }

    @Test
    @SuppressWarnings({"RedundantArrayCreation", "ConfusingArgumentToVarargsMethod"})
    void deserializeWithParametersEmpty() throws Exception {
        assertThat(classToTest.decode("{}", Movie.class, (Class<?>[]) null), is(notNullValue()));
        assertThat(classToTest.decode("{}", Movie.class, new Class[0]), is(notNullValue()));
    }

    @Test
    void deserializeMap() throws Exception {
        String mapString =
                "{\"commitSha\":\"b46889b5f0f2f8b91438a08a358ba8f05fc09fc1\",\"commitDate\":\"2019-11-15T09:51:54.278247+00:00\",\"pkgVersion\":\"0.1.1\"}";
        HashMap<String, String> decode =
                classToTest.decode(mapString, HashMap.class, String.class, String.class);

        assertThat(decode, notNullValue());
        assertThat(decode, aMapWithSize(3));
    }
}
