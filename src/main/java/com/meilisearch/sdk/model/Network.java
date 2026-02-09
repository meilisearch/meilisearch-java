package com.meilisearch.sdk.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * Meilisearch Network data structure.
 * <p>
 * <a href="https://www.meilisearch.com/docs/reference/api/network?utm_campaign=oss&utm_source=github">API Reference</a>
 */
public class Network implements Serializable {
    @Getter
    protected String self;
    @Getter
    protected String leader;
    @Getter
    protected UUID version;
    @Getter
    protected HashMap<String, NetworksRemote> remotes;
}

