package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.SearchResult;

/**
 * Class used for searching on Meilisearch indexes
 *
 * <p>https://docs.meilisearch.com/reference/api/search.html
 */
public class Search {
    private final HttpClient httpClient;

    /**
     * Constructor for the Meilisearch Search object
     *
     * @param config Meilisearch configuration
     */
    protected Search(Config config) {
        httpClient = config.httpClient;
    }

    /**
     * Performs a search on a given index with a given query
     *
     * @param uid Index identifier
     * @param q Query to search on index
     * @return search results, as raw data
     * @throws MeilisearchException Search Exception or Client Error
     */
    String rawSearch(String uid, String q) throws MeilisearchException {
        String requestQuery = "/indexes/" + uid + "/search";
        SearchRequest sr = SearchRequest.builder().q(q).build();
        return httpClient.post(requestQuery, sr, String.class);
    }

    /**
     * Performs a search on a given index with a given query
     *
     * @param uid Index identifier
     * @param sr SearchRequest to search on index
     * @return search results, as raw data
     * @throws MeilisearchException Search Exception or Client Error
     */
    String rawSearch(String uid, SearchRequest sr) throws MeilisearchException {
        String requestQuery = "/indexes/" + uid + "/search";
        return httpClient.post(requestQuery, sr.toString(), String.class);
    }

    /**
     * Performs a search on a given index with a given query
     *
     * @param uid Index identifier
     * @param q Query to search on index
     * @return search results
     * @throws MeilisearchException Search Exception or Client Error
     */
    SearchResult search(String uid, String q) throws MeilisearchException {
        return httpClient.jsonHandler.decode(rawSearch(uid, q), SearchResult.class);
    }

    /**
     * Performs a search on a given index with a given query
     *
     * @param uid Index identifier
     * @param sr SearchRequest to search on index
     * @return search results
     * @throws MeilisearchException Search Exception or Client Error
     */
    SearchResult search(String uid, SearchRequest sr) throws MeilisearchException {
        return httpClient.jsonHandler.decode(rawSearch(uid, sr), SearchResult.class);
    }
}
