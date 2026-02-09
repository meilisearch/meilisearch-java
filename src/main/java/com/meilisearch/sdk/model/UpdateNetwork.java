package com.meilisearch.sdk.model;


import lombok.Setter;

import java.util.HashMap;


/**
 * Data structure for updating a network instance.
 * <p>
 * <a href="https://www.meilisearch.com/docs/reference/api/network?utm_campaign=oss&utm_source=github">API Reference</a>
 */
public class UpdateNetwork {
    @Setter
    protected String self;
    @Setter
    protected String leader;
    @Setter
    protected HashMap<String, NetworksRemote> remotes;

    public UpdateNetwork() {
    }
}
