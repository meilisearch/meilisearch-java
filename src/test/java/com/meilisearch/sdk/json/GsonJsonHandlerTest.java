package com.meilisearch.sdk.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonElement;
import com.meilisearch.sdk.exceptions.JsonEncodingException;
import com.meilisearch.sdk.model.FilterableAttributesConfig;
import com.meilisearch.sdk.model.Key;
import com.meilisearch.sdk.utils.Movie;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GsonJsonHandlerTest {

    private final GsonJsonHandler classToTest = new GsonJsonHandler();

    @Test
    void encodeBasicString() {
        assertThat(classToTest.encode("test"), is(equalTo("test")));
    }

    @Test
    void encodeAnObject() {
        Movie movie = new Movie();
        movie.setId("foo");
        movie.setTitle("Foo");
        movie.setLanguage("English");

        String content = classToTest.encode(movie);

        assertThat(content, matchesPattern("\\{[^}]*}"));

        Map<String, String> values =
                Arrays.stream(content.substring(1, content.length() - 1).split(","))
                        .map(String::trim)
                        .map(
                                s ->
                                        Stream.of(s.split(":"))
                                                .map(String::trim)
                                                .map(i -> i.substring(1, i.length() - 1))
                                                .collect(Collectors.toList()))
                        .collect(Collectors.toMap(i -> i.get(0), i -> i.get(1)));

        assertThat(values, is(aMapWithSize(3)));
        assertThat(values.get("id"), is(equalTo("foo")));
        assertThat(values.get("title"), is(equalTo("Foo")));
        assertThat(values.get("language"), is(equalTo("English")));
    }

    @Test
    void encodeThrowsJsonEncodingExceptionWhenGsonThrowsException() {
        JsonElement mockElement = Mockito.mock(JsonElement.class);
        // Optionally mock behavior (if needed):
        // Mockito.when(mockElement.deepCopy()).thenReturn(null);

        assertThrows(JsonEncodingException.class, () -> classToTest.encode(mockElement));
    }

    @Test
    void encodeEmptyKeyWritesExpiresAtNull() {
        Key key = new Key();

        String result = classToTest.encode(key);

        assertThat(result, is(equalTo("{\"expiresAt\":null}")));
    }

    @Test
    void encodeKeyWithNonNullValueWritesValueAndExpiresAt() {
        Key key = new Key();
        key.setKey("foo");

        String result = classToTest.encode(key);

        assertThat(result, containsString("\"key\":\"foo\""));
        assertThat(result, containsString("\"expiresAt\":null"));
    }

    @Test
    void encodeKeyWithExpiresAtSetWritesExpiresAtValue() {
        Date expiresAt = Date.from(Instant.parse("2023-11-23T18:18:45.345Z"));
        Key key = new Key();
        key.setExpiresAt(expiresAt);

        String result = classToTest.encode(key);

        assertThat(result, containsString("\"expiresAt\":\"2023-11-23T18:18:45Z\""));
    }

    @Test
    void encodeWithMultipleValuesSetWritesAllValuesIncludingExpiresAt() {
        Key key = new Key();
        key.setKey("foo");
        key.setUid("foo123");
        key.setName("Foo");
        key.setDescription("Foo bar");
        key.setActions(new String[] {"*"});
        key.setIndexes(new String[] {"*"});

        String result = classToTest.encode(key);

        assertThat(result, containsString("\"key\":\"foo\""));
        assertThat(result, containsString("\"uid\":\"foo123\""));
        assertThat(result, containsString("\"name\":\"Foo\""));
        assertThat(result, containsString("\"description\":\"Foo bar\""));
        assertThat(result, containsString("\"actions\":[\"*\"]"));
        assertThat(result, containsString("\"indexes\":[\"*\"]"));
        assertThat(result, containsString("\"expiresAt\":null"));
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
        @SuppressWarnings("unchecked")
        HashMap<String, String> decode =
                classToTest.decode(mapString, HashMap.class, String.class, String.class);

        assertThat(decode, notNullValue());
        assertThat(decode, aMapWithSize(3));
    }

    @Test
    void decodeEmptyKey() {
        Key key = classToTest.decode("{}", Key.class);

        assertThat(key, is(notNullValue()));
        assertThat(key.getKey(), is(nullValue()));
        assertThat(key.getName(), is(nullValue()));
        assertThat(key.getDescription(), is(nullValue()));
        assertThat(key.getUid(), is(nullValue()));
        assertThat(key.getIndexes(), is(nullValue()));
        assertThat(key.getActions(), is(nullValue()));
        assertThat(key.getExpiresAt(), is(nullValue()));
        assertThat(key.getCreatedAt(), is(nullValue()));
        assertThat(key.getUpdatedAt(), is(nullValue()));
    }

    @Test
    void decodeKeyWithASingleValue() {
        Key key = classToTest.decode("{\"key\":\"foo\"}", Key.class);

        assertThat(key, is(notNullValue()));
        assertThat(key.getKey(), is(equalTo("foo")));
        assertThat(key.getName(), is(nullValue()));
        assertThat(key.getDescription(), is(nullValue()));
        assertThat(key.getUid(), is(nullValue()));
        assertThat(key.getIndexes(), is(nullValue()));
        assertThat(key.getActions(), is(nullValue()));
        assertThat(key.getExpiresAt(), is(nullValue()));
        assertThat(key.getCreatedAt(), is(nullValue()));
        assertThat(key.getUpdatedAt(), is(nullValue()));
    }

    @Test
    void decodeKeyWithOnlyExpiresAtSet() {
        String timestamp = "2021-08-11T10:00:00Z";
        Date expected = Date.from(Instant.parse(timestamp));

        Key key = classToTest.decode("{\"expiresAt\":\"" + timestamp + "\"}", Key.class);

        assertThat(key, is(notNullValue()));
        assertThat(key.getExpiresAt(), is(equalTo(expected)));
        assertThat(key.getKey(), is(nullValue()));
        assertThat(key.getName(), is(nullValue()));
        assertThat(key.getDescription(), is(nullValue()));
        assertThat(key.getUid(), is(nullValue()));
        assertThat(key.getIndexes(), is(nullValue()));
        assertThat(key.getActions(), is(nullValue()));
        assertThat(key.getCreatedAt(), is(nullValue()));
        assertThat(key.getUpdatedAt(), is(nullValue()));
    }

    @Test
    void decodeKeyWithNullIndexes() {
        Key key = classToTest.decode("{\"indexes\":null}", Key.class);

        assertThat(key, is(notNullValue()));
        assertThat(key.getIndexes(), is(nullValue()));
    }

    @Test
    void decodeKeyWithEmptyArrayActions() {
        Key key = classToTest.decode("{\"actions\":[]}", Key.class);

        assertThat(key, is(notNullValue()));
        assertThat(key.getActions(), is(notNullValue()));
        assertThat(key.getActions(), is(arrayWithSize(0)));
    }

    @Test
    void decodeKeyWithAllFieldsSet() {
        String timestamp = "2023-11-23T21:29:16.123Z";
        String input =
                "{\"key\":\"foo\",\"uid\":\"foo123\",\"name\":\"Foo\",\"description\":\"Foo bar\","
                        + "\"actions\":[\"*\"],\"indexes\":[\"*\"],\"expiresAt\":null,\"createdAt\":\""
                        + timestamp
                        + "\","
                        + "\"updatedAt\":\""
                        + timestamp
                        + "\"}";

        Key key = classToTest.decode(input, Key.class);

        assertThat(key, is(instanceOf(Key.class)));
        assertThat(key.getKey(), is(equalTo("foo")));
        assertThat(key.getUid(), is(equalTo("foo123")));
        assertThat(key.getName(), is(equalTo("Foo")));
        assertThat(key.getDescription(), is(equalTo("Foo bar")));
        assertThat(key.getActions(), is(arrayWithSize(1)));
        assertThat(key.getActions(), is(arrayContaining("*")));
        assertThat(key.getIndexes(), is(arrayWithSize(1)));
        assertThat(key.getIndexes(), is(arrayContaining("*")));
        assertThat(key.getExpiresAt(), is(nullValue()));
        assertThat(key.getCreatedAt(), is(equalTo(Date.from(Instant.parse(timestamp)))));
        assertThat(key.getKey(), is(equalTo("foo")));
    }

    @Test
    void decodeNullAsKey() {
        Key result = classToTest.decode("null", Key.class);

        assertThat(result, is(nullValue()));
    }

    @Test
    void decodeNestedKeyWhereValueIsNull() {
        Container result = classToTest.decode("{\"key\":null,\"enabled\":true}", Container.class);

        assertThat(result.getKey(), is(nullValue()));
        assertThat(result.getEnabled(), is(equalTo(true)));
    }

    @Test
    void granularFilterableAttributesWithNullPatternRoundTrip() throws Exception {
        FilterableAttributesConfig config = new FilterableAttributesConfig();
        config.setAttributePatterns(new String[] {"director", null, "genres"});

        String json = classToTest.encode(new FilterableAttributesConfig[] {config});
        FilterableAttributesConfig[] decoded =
                classToTest.decode(json, FilterableAttributesConfig[].class);

        assertThat(decoded, is(notNullValue()));
        assertThat(decoded.length, is(1));
        assertThat(decoded[0].getAttributePatterns(), arrayContaining("director", null, "genres"));
    }

    @Getter
    private static class Container {
        private Key key;
        private Boolean enabled;
    }
}
