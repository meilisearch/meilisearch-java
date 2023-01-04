package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.http.URLBuilder;
import com.meilisearch.sdk.model.IndexesQuery;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.TaskInfo;
import java.util.HashMap;

/**
 * Class covering the Meilisearch Index API.
 *
 * <p>https://docs.meilisearch.com/reference/api/indexes.html
 */
class IndexesHandler {
    HttpClient httpClient;

    /**
     * Creates and sets up an instance of IndexesHandler to simplify Meilisearch API calls to manage
     * indexes
     *
     * @param config Meilisearch configuration
     */
    IndexesHandler(Config config) {
        this.httpClient = config.httpClient;
    }

    /**
     * Creates an index with a unique identifier
     *
     * @param uid Unique identifier to create the index with
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo createIndex(String uid) throws MeilisearchException {
        return this.createIndex(uid, null);
    }

    /**
     * Creates an index with a unique identifier
     *
     * @param uid Unique identifier of the index
     * @param primaryKey Field to use as the primary key for documents in that index
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo createIndex(String uid, String primaryKey) throws MeilisearchException {
        HashMap<String, String> index = new HashMap<String, String>();
        index.put("uid", uid);
        index.put("primaryKey", primaryKey);

        return httpClient.post("/indexes", index, TaskInfo.class);
    }

    /**
     * Gets an index from its uid
     *
     * @param uid Unique identifier of the index to get
     * @return Meilisearch API response as Index instance
     * @throws MeilisearchException if an error occurs
     */
    Index getIndex(String uid) throws MeilisearchException {
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("indexes").addSubroute(uid);
        String urlPath = urlb.getURL();
        return httpClient.get(urlPath, Index.class);
    }

    /**
     * Gets all indexes in the current Meilisearch instance
     *
     * @return Results containing a list of indexes
     * @throws MeilisearchException if an error occurs
     */
    Results<Index> getIndexes() throws MeilisearchException {
        return httpClient.get("/indexes", Results.class, Index.class);
    }

    /**
     * Gets indexes in the current Meilisearch instance
     *
     * @param query parameters accepted by the indexes route
     * @return Results containing a list of indexes
     * @throws MeilisearchException if an error occurs
     */
    Results<Index> getIndexes(IndexesQuery param) throws MeilisearchException {
        return httpClient.get(param.toQuery(param), Results.class, Index.class);
    }

    /**
     * Gets all indexes in the current Meilisearch instance
     *
     * @return List of indexes as String
     * @throws MeilisearchException if an error occurs
     */
    String getRawIndexes() throws MeilisearchException {
        return httpClient.get("/indexes", String.class);
    }

    /**
     * Updates the primary key of an index in the Meilisearch instance
     *
     * @param uid Unique identifier of the index to update
     * @param primaryKey New primary key field to use for documents in that index
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updatePrimaryKey(String uid, String primaryKey) throws MeilisearchException {
        HashMap<String, String> index = new HashMap<String, String>();
        index.put("primaryKey", primaryKey);

        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("indexes").addSubroute(uid);
        String urlPath = urlb.getURL();
        return httpClient.patch(urlPath, index, TaskInfo.class);
    }

    /**
     * Deletes an index in the Meilisearch instance
     *
     * @param uid Unique identifier of the index to delete
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo deleteIndex(String uid) throws MeilisearchException {
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("indexes").addSubroute(uid);
        String urlPath = urlb.getURL();
        return httpClient.delete(urlPath, TaskInfo.class);
    }
}
