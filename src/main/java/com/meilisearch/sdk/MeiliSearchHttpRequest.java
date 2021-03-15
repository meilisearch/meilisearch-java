package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.APIError;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;
import com.meilisearch.sdk.http.AbstractHttpClient;
import com.meilisearch.sdk.http.DefaultHttpClient;
import com.meilisearch.sdk.http.factory.BasicRequestFactory;
import com.meilisearch.sdk.http.factory.RequestFactory;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.response.HttpResponse;
import com.meilisearch.sdk.json.GsonJsonHandler;

import java.util.Collections;

/**
 * The HTTP requests for the different functions to be done through MeiliSearch
 */
class MeiliSearchHttpRequest {
	private final AbstractHttpClient client;
	private final RequestFactory factory;
	private final GsonJsonHandler jsonHandler;

	/**
	 * Constructor for the MeiliSearchHttpRequest
	 *
	 * @param config MeiliSearch configuration
	 */
	protected MeiliSearchHttpRequest(Config config) {
		this.client = new DefaultHttpClient(config);
		this.jsonHandler = new GsonJsonHandler();
		this.factory = new BasicRequestFactory(jsonHandler);
	}

	/**
	 * Constructor for the MeiliSearchHttpRequest
	 *
	 * @param client  HttpClient for making calls to server
	 * @param factory RequestFactory for generating calls to server
	 */
	public MeiliSearchHttpRequest(AbstractHttpClient client, RequestFactory factory) {
		this.client = client;
		this.factory = factory;
		this.jsonHandler = new GsonJsonHandler();
	}


	/**
	 * Gets a document at the specified path
	 *
	 * @param api Path to document
	 * @return document that was requested
	 * @throws Exception               if the client has an error
	 * @throws MeiliSearchApiException if the response is an error
	 */
	public String get(String api) throws Exception, MeiliSearchApiException {
		return this.get(api, "");
	}

	/**
	 * Gets a document at the specified path with a given parameter
	 *
	 * @param api   Path to document
	 * @param param Parameter to be passed
	 * @return document that was requested
	 * @throws Exception               if the client has an error
	 * @throws MeiliSearchApiException if the response is an error
	 */
	String get(String api, String param) throws Exception, MeiliSearchApiException {
		HttpResponse<?> httpResponse = this.client.get(factory.create(HttpMethod.GET, api + param, Collections.emptyMap(), null));
		if (httpResponse.getStatusCode() >= 400) {
			throw new MeiliSearchApiException(jsonHandler.decode(httpResponse.getContent(), APIError.class));
		}
		return new String(httpResponse.getContentAsBytes());
	}

	/**
	 * Adds a document to the specified path
	 *
	 * @param api  Path to server
	 * @param body Query for search
	 * @return results of the search
	 * @throws Exception               if the client has an error
	 * @throws MeiliSearchApiException if the response is an error
	 */
	String post(String api, String body) throws Exception, MeiliSearchApiException {
		HttpResponse<?> httpResponse = this.client.post(factory.create(HttpMethod.POST, api, Collections.emptyMap(), body));
		if (httpResponse.getStatusCode() >= 400) {
			throw new MeiliSearchApiException(jsonHandler.decode(httpResponse.getContent(), APIError.class));
		}
		return new String(httpResponse.getContentAsBytes());
	}

	/**
	 * Replaces the requested resource with new data
	 *
	 * @param api  Path to the requested resource
	 * @param body Replacement data for the requested resource
	 * @return updated resource
	 * @throws Exception               if the client has an error
	 * @throws MeiliSearchApiException if the response is an error
	 */
	String put(String api, String body) throws Exception, MeiliSearchApiException {
		HttpResponse<?> httpResponse = this.client.put(factory.create(HttpMethod.PUT, api, Collections.emptyMap(), body));
		if (httpResponse.getStatusCode() >= 400) {
			throw new MeiliSearchApiException(jsonHandler.decode(httpResponse.getContent(), APIError.class));
		}
		return new String(httpResponse.getContentAsBytes());
	}


	/**
	 * Deletes the specified resource
	 *
	 * @param api Path to the requested resource
	 * @return deleted resource
	 * @throws Exception               if the client has an error
	 * @throws MeiliSearchApiException if the response is an error
	 */
	String delete(String api) throws Exception, MeiliSearchApiException {
		HttpResponse<?> httpResponse = this.client.put(factory.create(HttpMethod.DELETE, api, Collections.emptyMap(), null));
		if (httpResponse.getStatusCode() >= 400) {
			throw new MeiliSearchApiException(jsonHandler.decode(httpResponse.getContent(), APIError.class));
		}
		return new String(httpResponse.getContentAsBytes());
	}
}
