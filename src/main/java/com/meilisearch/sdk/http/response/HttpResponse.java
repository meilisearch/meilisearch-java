package com.meilisearch.sdk.http.response;

import java.util.Map;

public interface HttpResponse<B> {
	Map<String, String> getHeaders();

	int getStatusCode();

	boolean hasContent();

	B getContent();

	byte[] getContentAsBytes();
}
