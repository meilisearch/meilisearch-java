package com.meilisearch.sdk;

import com.meilisearch.sdk.http.AbstractHttpClient;
import com.meilisearch.sdk.http.DefaultHttpClient;
import com.meilisearch.sdk.http.factory.BasicRequestFactory;
import com.meilisearch.sdk.http.factory.RequestFactory;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.response.HttpResponse;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;

import java.util.Collections;

class MeiliSearchHttpRequest {
	private final AbstractHttpClient client;
	private final RequestFactory factory;

	protected MeiliSearchHttpRequest(Config config) {
		this.client = new DefaultHttpClient(config);
		this.factory = new BasicRequestFactory();
	}

	public MeiliSearchHttpRequest(AbstractHttpClient client, RequestFactory factory) {
		this.client = client;
		this.factory = factory;
	}


	public String get(String api) throws Exception, MeiliSearchApiException {
		return this.get(api, "");
	}

	String get(String api, String param) throws Exception, MeiliSearchApiException {
		HttpResponse<?> httpResponse = this.client.get(factory.create(HttpMethod.GET, api + param, Collections.emptyMap(), null));
		if (httpResponse.getStatusCode() >= 400) {
			throw new MeiliSearchApiException(httpResponse.getContent().toString());
		}
		return new String(httpResponse.getContentAsBytes());
	}


	String post(String api, String body) throws Exception, MeiliSearchApiException {
		HttpResponse<?> httpResponse = this.client.post(factory.create(HttpMethod.POST, api, Collections.emptyMap(), body));
		if (httpResponse.getStatusCode() >= 400) {
			throw new MeiliSearchApiException(httpResponse.getContent().toString());
		}
		return new String(httpResponse.getContentAsBytes());
	}


	String put(String api, String body) throws Exception, MeiliSearchApiException {
		HttpResponse<?> httpResponse = this.client.put(factory.create(HttpMethod.PUT, api, Collections.emptyMap(), body));
		if (httpResponse.getStatusCode() >= 400) {
			throw new MeiliSearchApiException(httpResponse.getContent().toString());
		}
		return new String(httpResponse.getContentAsBytes());
	}


	String delete(String api) throws Exception, MeiliSearchApiException {
		HttpResponse<?> httpResponse = this.client.put(factory.create(HttpMethod.DELETE, api, Collections.emptyMap(), null));
		if (httpResponse.getStatusCode() >= 400) {
			throw new MeiliSearchApiException(httpResponse.getContent().toString());
		}
		return new String(httpResponse.getContentAsBytes());
	}
}
