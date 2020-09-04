package meilisearch;

/**
 * Meilisearch configuration
 */
public class MeilisearchConfig {
	String hostUrl;
	String apiKey;

	/**
	 * Create a configuration without an API key
	 * @param hostUrl URL of the Meilisearch instance
	 */
	public MeilisearchConfig(String hostUrl) {
		this(hostUrl, "");
	}

	/**
	 * Create a configuration with an API key
	 * @param hostUrl URL of the Meilisearch instance
	 * @param apiKey API key to pass to the header of requests sent to Meilisearch
	 */
	public MeilisearchConfig(String hostUrl, String apiKey) {
		this.hostUrl = hostUrl;
		this.apiKey = apiKey;
	}
}
