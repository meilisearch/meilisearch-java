package com.meilisearch.sdk.http.factory;

import com.meilisearch.sdk.http.request.BasicHttpRequest;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.request.HttpRequest;

import java.util.Map;

public class BasicRequestFactory implements RequestFactory {
	@Override
	public <T> HttpRequest<?> create(HttpMethod method, String path, Map<String, String> headers, T content) {
		//todo integrate serializer for content
		return new BasicHttpRequest(method, path, headers, (String) content);
	}
}
