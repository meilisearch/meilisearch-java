package com.meilisearch.sdk.api.keys;

import com.meilisearch.sdk.ServiceTemplate;
import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;
import com.meilisearch.sdk.http.factory.RequestFactory;
import com.meilisearch.sdk.http.request.HttpMethod;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class KeysHandler {
	private final ServiceTemplate serviceTemplate;
	private final RequestFactory requestFactory;

	public KeysHandler(ServiceTemplate serviceTemplate, RequestFactory requestFactory) {
		this.serviceTemplate = serviceTemplate;
		this.requestFactory = requestFactory;
	}


	/**
	 * @return the public and private keys in a map
	 * @throws com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException in case some error happens
	 */
	public Map<String, String> get() throws MeiliSearchRuntimeException {
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.GET, "/keys", Collections.emptyMap(), null),
			HashMap.class, String.class, String.class
		);
	}
}
