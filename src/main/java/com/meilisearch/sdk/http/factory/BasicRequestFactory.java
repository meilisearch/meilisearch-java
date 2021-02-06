package com.meilisearch.sdk.http.factory;

import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;
import com.meilisearch.sdk.http.request.BasicHttpRequest;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.json.JsonHandler;

import java.util.Map;

public class BasicRequestFactory implements RequestFactory {
	private final JsonHandler jsonHandler;
	private final Config config;

	public BasicRequestFactory(JsonHandler jsonHandler, Config config) {
		this.jsonHandler = jsonHandler;
		this.config = config;
	}

	@Override
	public <T> HttpRequest<?> create(HttpMethod method, String path, Map<String, String> headers, T content) {
		try {
			return new BasicHttpRequest(method, config.getHostUrl() + path, headers, this.jsonHandler.encode(content));
		} catch (Exception e) {
			throw new MeiliSearchRuntimeException(e);
		}
	}
}
