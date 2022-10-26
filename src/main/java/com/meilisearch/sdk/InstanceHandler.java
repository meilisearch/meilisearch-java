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
     * https://docs.meilisearch.com/reference/api/health.html#health
     *
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public String health() throws MeilisearchException {
        return httpClient.get("/health", String.class);
    }

    /**
     * Gets the status and availability of a Meilisearch instance
     * https://docs.meilisearch.com/reference/api/health.html#health
     *
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public boolean isHealthy() throws MeilisearchException {
        try {
            this.health();
            return true;
        } catch (MeilisearchException e) {
            return false;
        }
    }

    /**
     * Gets extended information and metrics about indexes and the Meilisearch database
     * https://docs.meilisearch.com/reference/api/stats.html#stats-object
     *
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public Stats getStats() throws MeilisearchException {
        return httpClient.get("/stats", Stats.class);
    }

    /**
     * Gets extended information and metrics about indexes and the Meilisearch database
     * https://docs.meilisearch.com/reference/api/stats.html#stats-object
     *
     * @param uid Index identifier to the requested
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public IndexStats getIndexStats(String uid) throws MeilisearchException {
        String requestQuery = "/indexes/" + uid + "/stats";
        return httpClient.<IndexStats>get(requestQuery, IndexStats.class);
    }

    /**
     * Gets the version of Meilisearch instance
     * https://docs.meilisearch.com/reference/api/version.html#version
     *
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public String getVersion() throws MeilisearchException {
        return httpClient.get("/version", String.class);
    }
}
