package com.meilisearch.sdk;

import com.meilisearch.sdk.http.URLBuilder;
import com.meilisearch.sdk.model.Network;
import com.meilisearch.sdk.model.UpdateNetwork;

public class NetworkHandler {
    public final HttpClient httpClient;

    protected NetworkHandler(Config config) {
        this.httpClient = config.httpClient;
    }

    /**
     * Returns the current value of the instance’s network object.
     *
     * @return Network object
     */
    public Network getNetworkState() {
        return this.httpClient.get(networkPath().getURL(), Network.class);
    }

    /**
     * Update the fields of the network object. Updates to the network object are partial. Only provide the fields you intend to update.
     * Fields that are null in the payload will remain unchanged. To reset self and remotes to their original value, set them to null.
     * To remove a single remote from your network, set the value of its name to null.
     *
     * @param updatedNetwork Updated network configs
     * @return Updated Network
     */
    public Network updateNetwork(UpdateNetwork updatedNetwork) {
        return this.httpClient.patch(networkPath().getURL(), updatedNetwork, Network.class);
    }

    private URLBuilder networkPath() {
        return new URLBuilder("/network");
    }
}
