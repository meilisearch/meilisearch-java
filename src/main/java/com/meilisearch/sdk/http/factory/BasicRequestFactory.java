package com.meilisearch.sdk.http.factory;

import com.meilisearch.sdk.http.request.BasicHttpRequest;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.request.HttpRequest;

import java.util.Map;

public class BasicRequestFactory implements RequestFactory<String> {
	@Override
	public HttpRequest<?> create(HttpMethod method, String path, Map<String, String> headers, String content) {
		return new BasicHttpRequest(method,path,headers,content);
	}
}
