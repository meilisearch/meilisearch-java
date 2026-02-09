package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Meilisearch Network's remote instance's data structure.
 * <p>
 * <a href="https://www.meilisearch.com/docs/reference/api/network?utm_campaign=oss&utm_source=github">API Reference</a>
 */
public class NetworksRemote implements Serializable {
    @Getter
    @Setter
    protected String url;
    @Getter
    @Setter
    protected String searchApiKey;
    @Getter
    @Setter
    protected String writeApiKey;

    public NetworksRemote(String url, String searchApiKey, String writeApiKey) {
        this.url = url;
        this.searchApiKey = searchApiKey;
        this.writeApiKey = writeApiKey;
    }
}
