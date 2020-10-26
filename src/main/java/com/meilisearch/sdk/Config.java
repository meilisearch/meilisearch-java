package com.meilisearch.sdk;

import java.util.Collections;
import java.util.Map;

/**
 * Meilisearch configuration
 */
public class Config {
	private String hostUrl;
	private String apiKey;
	private Map<String, Class<?>> modelMapping;

	/**
	 * Create a configuration without an API key
	 *
	 * @param hostUrl URL of the Meilisearch instance
	 */
	public Config(String hostUrl) {
		this(hostUrl, "", Collections.emptyMap());
	}

	/**
	 * Create a configuration with an API key
	 *
	 * @param hostUrl URL of the Meilisearch instance
	 * @param apiKey  API key to pass to the header of requests sent to Meilisearch
	 */
	public Config(String hostUrl, String apiKey) {
		this(hostUrl, apiKey, Collections.emptyMap());
	}

	/**
	 * Create a configuration with an API key
	 *
	 * @param hostUrl URL of the Meilisearch instance
	 * @param apiKey  API key to pass to the header of requests sent to Meilisearch
	 */
	public Config(String hostUrl, String apiKey, Map<String, Class<?>> modelMapping) {
		this.hostUrl = hostUrl;
		this.apiKey = apiKey;
		this.modelMapping = modelMapping;
	}

	public String getHostUrl() {
		return hostUrl;
	}

	public String getApiKey() {
		return apiKey;
	}

	public Map<String, Class<?>> getModelMapping() {
		return modelMapping;
	}
}
