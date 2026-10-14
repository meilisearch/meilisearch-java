package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.http.URLBuilder;
import com.meilisearch.sdk.model.IndexStats;
import com.meilisearch.sdk.model.IndexStatsWithSizeFormat;
import com.meilisearch.sdk.model.Stats;
import com.meilisearch.sdk.model.StatsQuery;
import com.meilisearch.sdk.model.StatsWithSizeFormat;

/** Class providing information on the Meilisearch instance */
public class InstanceHandler {
    private final HttpClient httpClient;

    /**
     * Creates and sets up an instance of InstanceHandler
     *
     * @param config Meilisearch configuration
     */
    protected InstanceHandler(Config config) {
        this.httpClient = config.httpClient;
    }

    /**
     * Gets the status and availability of a Meilisearch instance
     *
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/health">API specification</a>
     */
    String health() throws MeilisearchException {
        return httpClient.get("/health", String.class);
    }

    /**
     * Gets the status and availability of a Meilisearch instance
     *
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/health">API specification</a>
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
     * @see <a href="https://www.meilisearch.com/docs/reference/api/stats">API specification</a>
     */
    Stats getStats() throws MeilisearchException {
        return httpClient.get("/stats", Stats.class);
    }

    /**
     * Gets extended information and metrics about indexes and the Meilisearch database
     *
     * @param params query parameters accepted by the stats route
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/stats">API specification</a>
     */
    StatsWithSizeFormat getStats(StatsQuery params) throws MeilisearchException {
        URLBuilder urlb = new URLBuilder("/stats");
        if (params != null) {
            urlb.addQuery(params.toQuery());
        }
        return httpClient.get(urlb.getURL(), StatsWithSizeFormat.class);
    }

    /**
     * Gets extended information and metrics about indexes and the Meilisearch database
     *
     * @param uid Index identifier to the requested
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/stats">API specification</a>
     */
    IndexStats getIndexStats(String uid) throws MeilisearchException {
        String requestQuery = "/indexes/" + uid + "/stats";
        return httpClient.<IndexStats>get(requestQuery, IndexStats.class);
    }

    /**
     * Gets extended information and metrics about an index and the Meilisearch database
     *
     * @param uid Index identifier to the requested
     * @param params query parameters accepted by the stats route
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/stats">API specification</a>
     */
    IndexStatsWithSizeFormat getIndexStats(String uid, StatsQuery params)
            throws MeilisearchException {
        URLBuilder urlb = new URLBuilder("/indexes").addSubroute(uid).addSubroute("stats");
        if (params != null) {
            urlb.addQuery(params.toQuery());
        }
        return httpClient.get(urlb.getURL(), IndexStatsWithSizeFormat.class);
    }

    /**
     * Gets the version of Meilisearch instance
     *
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/version">API specification</a>
     */
    String getVersion() throws MeilisearchException {
        return httpClient.get("/version", String.class);
    }
}
