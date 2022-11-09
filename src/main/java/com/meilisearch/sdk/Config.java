package com.meilisearch.sdk;

import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.json.JsonHandler;
import lombok.Getter;
import lombok.Setter;

/** Meilisearch configuration */
@Getter
@Setter
public class Config {
    protected final String hostUrl;
    protected final String apiKey;
    protected final HttpClient httpClient;
    protected JsonHandler jsonHandler;

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
        this.jsonHandler = new GsonJsonHandler();
        this.httpClient = new HttpClient(this);
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
