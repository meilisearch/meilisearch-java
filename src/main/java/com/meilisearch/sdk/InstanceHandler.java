package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.IndexStats;
import com.meilisearch.sdk.model.Stats;

public class InstanceHandler {
    HttpClient httpClient;

    /**
     * Creates and sets up an instance of InstanceHandler
     *
     * @param config Meilisearch configuration
     */
    InstanceHandler(Config config) {
        this.httpClient = config.httpClient;
    }

    /**
     * Gets the status and availability of a Meilisearch instance
     *
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    String health() throws MeilisearchException {
        return httpClient.get("/health", String.class);
    }

    /**
     * Gets the status and availability of a Meilisearch instance
     *
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    boolean isHealthy() throws MeilisearchException {
        try {
            this.health();
            return true;
        } catch (MeilisearchException e) {
            return false;
        }
    }

    /**
     * Gets extended information and metrics about indexes and the Meilisearch database
     *
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    Stats getStats() throws MeilisearchException {
        return httpClient.get("/stats", Stats.class);
    }

    /**
     * Gets extended information and metrics about indexes and the Meilisearch database
     *
     * @param uid Index identifier to the requested
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    IndexStats getIndexStats(String uid) throws MeilisearchException {
        String requestQuery = "/indexes/" + uid + "/stats";
        return httpClient.<IndexStats>get(requestQuery, IndexStats.class);
    }

    /**
     * Gets the version of Meilisearch instance
     *
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    String getVersion() throws MeilisearchException {
        return httpClient.get("/version", String.class);
    }
}
