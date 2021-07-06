package com.meilisearch.sdk.json;

import com.meilisearch.sdk.utils.Movie;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonbJsonHandlerTest {

	private final Jsonb mapper = mock(Jsonb.class);
	private final JsonbJsonHandler classToTest = new JsonbJsonHandler(mapper);

	@Test
	void serialize() throws Exception {
		assertEquals("test", classToTest.encode("test"));
		when(mapper.toJson(any(Movie.class))).thenThrow(new JsonbException("Oh boy!"));
		assertThrows(RuntimeException.class, () -> classToTest.encode(new Movie()));
	}

	@Test
	void deserialize() {
		assertDoesNotThrow(() -> classToTest.decode("{}", Movie.class));
		assertDoesNotThrow(() -> classToTest.decode("{}", Movie.class, (Class<?> []) null));
		assertDoesNotThrow(() -> classToTest.decode("{}", Movie.class, new Class[0]));
	}

	@Test
	void deserializeString() throws Exception {
		String content = "{}";
		assertEquals(content, classToTest.decode(content, String.class));
	}

	@Test
	void deserializeBodyNull() {
		assertThrows(Exception.class, () -> classToTest.decode(null, List.class, String.class));
	}

	@Test
	@SuppressWarnings({"unchecked", "RedundantArrayCreation", "ConfusingArgumentToVarargsMethod"})
	void deserializeWithParametersEmpty() throws Exception {
		when(mapper.fromJson(any(String.class), any())).thenReturn(new Movie("1234", "Title"));
		assertNotNull(classToTest.decode("{}", Movie.class, (Class<?> []) null));
		assertNotNull(classToTest.decode("{}", Movie.class, new Class[0]));
	}

	@Test
	void deserializeMap() throws Exception {
		String mapString = "{\"commitSha\":\"b46889b5f0f2f8b91438a08a358ba8f05fc09fc1\",\"commitDate\":\"2019-11-15T09:51:54.278247+00:00\",\"pkgVersion\":\"0.1.1\"}";

		Jsonb jsonb = JsonbBuilder.create();

		when(mapper.fromJson(any(String.class), any())).thenAnswer(invocation -> jsonb.fromJson((String) invocation.getArgument(0), invocation.getArgument(1)));
		HashMap<String, String> decode = classToTest.decode(mapString, HashMap.class, String.class, String.class);

		assertThat(decode, notNullValue());
		assertThat(decode, aMapWithSize(3));
	}
}
