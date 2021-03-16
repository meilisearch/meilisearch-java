package com.meilisearch.sdk.api.keys;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.GenericServiceTemplate;
import com.meilisearch.sdk.http.AbstractHttpClient;
import com.meilisearch.sdk.http.factory.BasicRequestFactory;
import com.meilisearch.sdk.http.factory.RequestFactory;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.http.response.BasicHttpResponse;
import com.meilisearch.sdk.json.JacksonJsonHandler;
import com.meilisearch.sdk.json.JsonHandler;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;

class KeysHandlerTest {
	private final AbstractHttpClient client = mock(AbstractHttpClient.class);
	private final JsonHandler processor = new JacksonJsonHandler(new ObjectMapper());
	private final RequestFactory requestFactory = new BasicRequestFactory(processor);
	private final GenericServiceTemplate serviceTemplate = new GenericServiceTemplate(client, processor, requestFactory);
	private final KeysHandler classToTest = new KeysHandler(serviceTemplate, requestFactory);


	@Test
	void get() throws Exception {
		when(client.get(any(HttpRequest.class))).thenAnswer(invocation -> new BasicHttpResponse(null, 200, "{\"private\":\"8c222193c4dff5a19689d637416820bc623375f2ad4c31a2e3a76e8f4c70440d\",\"public\":\"948413b6667024a0704c2023916c21eaf0a13485a586c43e4d2df520852a4fb8\"}"));
		Map<String, String> keys = classToTest.get();
		assertIterableEquals(Stream.of("private", "public").collect(Collectors.toList()), keys.keySet());
	}
}
