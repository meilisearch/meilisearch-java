package com.meilisearch.sdk.http.factory;

import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;
import com.meilisearch.sdk.http.request.BasicHttpRequest;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.json.JsonHandler;

import java.util.Map;

public class BasicRequestFactory implements RequestFactory {
	private final JsonHandler jsonHandler;

	public BasicRequestFactory(JsonHandler jsonHandler) {
		this.jsonHandler = jsonHandler;
	}

	@Override
	public <T> HttpRequest<?> create(HttpMethod method, String path, Map<String, String> headers, T content) {
		try {
			return new BasicHttpRequest(method, path, headers, this.jsonHandler.encode(content));
		} catch (Exception e) {
			throw new MeiliSearchRuntimeException(e);
		}
	}
}
