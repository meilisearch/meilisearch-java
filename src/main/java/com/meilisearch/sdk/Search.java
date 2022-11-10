package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.SearchResult;

/**
 * Wrapper around HttpClient class to use for Meilisearch search
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
        SearchRequest sr = new SearchRequest(q);
        return httpClient.post(requestQuery, sr, String.class);
    }

    /**
     * Performs a search on a given index with a given query
     *
     * @param q Query string
     * @param offset Number of documents to skip
     * @param limit Maximum number of documents returned
     * @param attributesToRetrieve Attributes to display in the returned documents
     * @param attributesToCrop Attributes whose values have been cropped
     * @param cropLength Length used to crop field values
     * @param cropMarker String to add before and/or after the cropped text, default value: …
     * @param highlightPreTag String to customize highlight tag before every highlighted query
     *     terms, default value: <em>
     * @param highlightPostTag String to customize highlight tag after every highlighted query
     *     terms, default value: </em>
     * @param attributesToHighlight Attributes whose values will contain highlighted matching terms
     * @param filter Filter queries by an attribute value
     * @param matches Defines whether an object that contains information about the matches should
     *     be returned or not
     * @param facetsDistribution Facets for which to retrieve the matching count
     * @param sort Sort queries by an attribute value
     * @return search results, as raw data
     * @throws MeilisearchException Search Exception or Client Error
     */
    String rawSearch(
            String uid,
            String q,
            int offset,
            int limit,
            String[] attributesToRetrieve,
            String[] attributesToCrop,
            int cropLength,
            String cropMarker,
            String highlightPreTag,
            String highlightPostTag,
            String[] attributesToHighlight,
            String[] filter,
            boolean matches,
            String[] facetsDistribution,
            String[] sort)
            throws MeilisearchException {
        String requestQuery = "/indexes/" + uid + "/search";
        SearchRequest sr =
                new SearchRequest(
                        q,
                        offset,
                        limit,
                        attributesToRetrieve,
                        attributesToCrop,
                        cropLength,
                        cropMarker,
                        highlightPreTag,
                        highlightPostTag,
                        attributesToHighlight,
                        filter,
                        matches,
                        facetsDistribution,
                        sort);
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
        return httpClient.post(requestQuery, sr, String.class);
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
     * @param q Query string
     * @param offset Number of documents to skip
     * @param limit Maximum number of documents returned
     * @param attributesToRetrieve Attributes to display in the returned documents
     * @param attributesToCrop Attributes whose values have been cropped
     * @param cropLength Length used to crop field values
     * @param cropMarker String to customize default crop marker, default value: …
     * @param highlightPreTag String to customize highlight tag before every highlighted query
     *     terms, default value: <em>
     * @param highlightPostTag String to customize highlight tag after every highlighted query
     *     terms, default value: </em>
     * @param attributesToHighlight Attributes whose values will contain highlighted matching terms
     * @param filter Filter queries by an attribute value
     * @param matches Defines whether an object that contains information about the matches should
     *     be returned or not
     * @param facetsDistribution Facets for which to retrieve the matching count
     * @param sort Sort queries by an attribute value
     * @return search results
     * @throws MeilisearchException Search Exception or Client Error
     */
    SearchResult search(
            String uid,
            String q,
            int offset,
            int limit,
            String[] attributesToRetrieve,
            String[] attributesToCrop,
            int cropLength,
            String cropMarker,
            String highlightPreTag,
            String highlightPostTag,
            String[] attributesToHighlight,
            String[] filter,
            boolean matches,
            String[] facetsDistribution,
            String[] sort)
            throws MeilisearchException {
        return httpClient.jsonHandler.decode(
                rawSearch(
                        uid,
                        q,
                        offset,
                        limit,
                        attributesToRetrieve,
                        attributesToCrop,
                        cropLength,
                        cropMarker,
                        highlightPreTag,
                        highlightPostTag,
                        attributesToHighlight,
                        filter,
                        matches,
                        facetsDistribution,
                        sort),
                SearchResult.class);
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
