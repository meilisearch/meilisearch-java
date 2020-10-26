package com.meilisearch.sdk.http.factory;

import com.meilisearch.sdk.http.request.BasicHttpRequest;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.json.JsonHandler;

import java.util.Map;

public class BasicRequestFactory implements RequestFactory {
	private final JsonHandler handler;

	public BasicRequestFactory(JsonHandler handler) {
		this.handler = handler;
	}

	@Override
	public <T> HttpRequest<?> create(HttpMethod method, String path, Map<String, String> headers, T content) {
		String contentAsString = null;
		try {
			contentAsString = handler.encode(content);
		} catch (Exception e) {
			//todo: throw dedicated exception
			throw new RuntimeException(e);
		}
		return new BasicHttpRequest(method, path, headers, contentAsString);
	}
}
