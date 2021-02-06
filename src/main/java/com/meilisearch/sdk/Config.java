package com.meilisearch.sdk;

import java.util.Collections;
import java.util.Map;

/**
 * MeiliSearch configuration
 */
public class Config {
	private final String hostUrl;
	private final String apiKey;
	private Map<String, Class<?>> modelMapping;

	/**
	 * Creates a configuration without an API key
	 *
	 * @param hostUrl URL of the MeiliSearch instance
	 */
	public Config(String hostUrl) {
		this(hostUrl, "");
	}

	/**
	 * Creates a configuration with an API key
	 *
	 * @param hostUrl URL of the MeiliSearch instance
	 * @param apiKey  API key to pass to the header of requests sent to MeiliSearch
	 */
	public Config(String hostUrl, String apiKey) {
		this.hostUrl = hostUrl;
		this.apiKey = apiKey;
	}

	/**
	 * Create a configuration with an API key
	 *
	 * @param hostUrl      URL of the Meilisearch instance
	 * @param apiKey       API key to pass to the header of requests sent to Meilisearch
	 * @param modelMapping Mapping of indexname to class
	 */
	public Config(String hostUrl, String apiKey, Map<String, Class<?>> modelMapping) {
		this.hostUrl = hostUrl;
		this.apiKey = apiKey;
		this.modelMapping = Collections.unmodifiableMap(modelMapping);
	}

	/**
	 * Method for returning the hostUrl
	 *
	 * @return host URL string of the MeiliSearch instance
	 */
	public String getHostUrl() {
		return hostUrl;
	}

	/**
	 * Method for returning the apiKey
	 *
	 * @return API key String
	 */
	public String getApiKey() {
		return apiKey;
	}

	public Map<String, Class<?>> getModelMapping() {
		return modelMapping;
	}
}
