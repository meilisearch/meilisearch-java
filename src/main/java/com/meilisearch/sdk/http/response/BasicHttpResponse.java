package com.meilisearch.sdk.http.response;

import java.util.Map;

public class BasicHttpResponse implements HttpResponse<String> {
	private final Map<String, String> headers;
	private final int statusCode;
	private final String content;

	public BasicHttpResponse(Map<String, String> headers, int statusCode, String content) {
		this.headers = headers;
		this.statusCode = statusCode;
		this.content = content;
	}

	@Override
	public Map<String, String> getHeaders() {
		return headers;
	}

	@Override
	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public String getContent() {
		return content;
	}
}
