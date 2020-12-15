package com.meilisearch.sdk;

/**
 * Meilisearch configuration
 */
public class Config {
	String hostUrl;
	String apiKey;

	/**
	 * Create a configuration without an API key
	 *
	 * @param hostUrl URL of the Meilisearch instance
	 */
	public Config(String hostUrl) {
		this(hostUrl, "");
	}

	/**
	 * Create a configuration with an API key
	 *
	 * @param hostUrl URL of the Meilisearch instance
	 * @param apiKey  API key to pass to the header of requests sent to Meilisearch
	 */
	public Config(String hostUrl, String apiKey) {
		this.hostUrl = hostUrl;
		this.apiKey = apiKey;
	}

	/**
	 * Method for returning the hostUrl
	 * @return The Host Url String of the Meilisearch Instance
	 */
	public String getHostUrl() {
		return hostUrl;
	}

	/**
	 * Method for returning the apiKey
	 * @return The API Key String
	 */
	public String getApiKey() {
		return apiKey;
	}
}
