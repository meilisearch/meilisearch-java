package com.meilisearch.sdk;

import lombok.Getter;

/** Meilisearch configuration */
@Getter
public class Config {
    String hostUrl;
    String apiKey;

    /**
     * Creates a configuration without an API key
     *
     * @param hostUrl URL of the Meilisearch instance
     */
    public Config(String hostUrl) {
        this(hostUrl, "");
    }

    /**
     * Creates a configuration with an API key
     *
     * @param hostUrl URL of the Meilisearch instance
     * @param apiKey API key to pass to the header of requests sent to Meilisearch
     */
    public Config(String hostUrl, String apiKey) {
        this.hostUrl = hostUrl;
        this.apiKey = apiKey;
    }

    /**
     * Method for returning the concatenated Bearer header with apiKey
     *
     * @return Bearer API key String
     */
    public String getBearerApiKey() {
        return "Bearer " + apiKey;
    }
}
