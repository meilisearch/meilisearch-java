package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.FacetSearchResult;
import com.meilisearch.sdk.model.FacetSearchable;

/**
 * Class used for performing facet searching on Meilisearch indexes
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/facet_search">API specification</a>
 */
public class FacetSearch {
    private final HttpClient httpClient;

    /**
     * Constructor for the Meilisearch Facet Search object
     *
     * @param config Meilisearch configuration
     */
    protected FacetSearch(Config config) {
        httpClient = config.httpClient;
    }

    /**
     * Performs a facet search on a given index with a given query
     *
     * @param uid Index identifier
     * @param fsr FacetSearchRequest to search on index
     * @return search results, as raw data
     * @throws MeilisearchException Search Exception or Client Error
     */
    String rawSearch(String uid, FacetSearchRequest fsr) throws MeilisearchException {
        String requestQuery = "/indexes/" + uid + "/facet-search";
        if (fsr.getFacetName() == null) {
            throw new MeilisearchException("Facet name is required for a facet search");
        }
        return httpClient.post(requestQuery, fsr.toString(), String.class);
    }

    FacetSearchable facetSearch(String uid, FacetSearchRequest fsr) throws MeilisearchException {
        return httpClient.jsonHandler.decode(rawSearch(uid, fsr), FacetSearchResult.class);
    }
}
