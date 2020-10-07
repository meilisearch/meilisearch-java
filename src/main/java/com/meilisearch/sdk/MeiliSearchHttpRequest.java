package com.meilisearch.sdk;

import com.meilisearch.sdk.http.AbstractHttpClient;
import com.meilisearch.sdk.http.DefaultHttpClient;
import com.meilisearch.sdk.http.request.BasicHttpRequest;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.response.HttpResponse;

import java.util.Arrays;
import java.util.Collections;

class MeiliSearchHttpRequest {
	private final AbstractHttpClient client;

	protected MeiliSearchHttpRequest(Config config) {
		this.client = new DefaultHttpClient(config);
	}

	public MeiliSearchHttpRequest(AbstractHttpClient client) {
		this.client = client;
	}


	public String get(String api) throws Exception {
		return this.get(api, "");
	}


	String get(String api, String param) throws Exception {
		HttpResponse<?> httpResponse = this.client.get(new BasicHttpRequest(HttpMethod.GET, api + param, Collections.emptyMap(), null));
		return Arrays.toString(httpResponse.getContentAsBytes());
	}


	String post(String api, String params) throws Exception {
		HttpResponse<?> httpResponse = this.client.post(new BasicHttpRequest(HttpMethod.POST, api + params, Collections.emptyMap(), null));
		return Arrays.toString(httpResponse.getContentAsBytes());
	}


	String put(String api, String params) throws Exception {
		HttpResponse<?> httpResponse = this.client.put(new BasicHttpRequest(HttpMethod.PUT, api + params, Collections.emptyMap(), null));
		return Arrays.toString(httpResponse.getContentAsBytes());
	}


	String delete(String api) throws Exception {
		HttpResponse<?> httpResponse = this.client.put(new BasicHttpRequest(HttpMethod.PUT, api, Collections.emptyMap(), null));
		return Arrays.toString(httpResponse.getContentAsBytes());
	}
}
