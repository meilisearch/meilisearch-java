package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
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
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo create(String uid) throws MeilisearchException {
        return this.create(uid, null);
    }

    /**
     * Creates an index with a unique identifier
     *
     * @param uid Unique identifier to create the index with
     * @param primaryKey Field to use as the primary key for documents in that index
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo create(String uid, String primaryKey) throws MeilisearchException {
        HashMap<String, String> index = new HashMap<String, String>();
        index.put("uid", uid);
        index.put("primaryKey", primaryKey);

        return httpClient.post("/indexes", index, TaskInfo.class);
    }

    /**
     * Gets an index from its uid
     *
     * @param uid Unique identifier of the index to get
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    String get(String uid) throws MeilisearchException {
        String requestQuery = "/indexes/" + uid;
        return httpClient.get(requestQuery, String.class);
    }

    /**
     * Gets all indexes in the current Meilisearch instance
     *
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    String getAll() throws MeilisearchException {
        return httpClient.get("/indexes", String.class);
    }

    /**
     * Updates the primary key of an index in the Meilisearch instance
     *
     * @param uid Unique identifier of the index to update
     * @param primaryKey New primary key field to use for documents in that index
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updatePrimaryKey(String uid, String primaryKey) throws MeilisearchException {
        HashMap<String, String> index = new HashMap<String, String>();
        index.put("primaryKey", primaryKey);

        String requestQuery = "/indexes/" + uid;
        return httpClient.patch(requestQuery, index, TaskInfo.class);
    }

    /**
     * Deletes an index in the Meilisearch instance
     *
     * @param uid Unique identifier of the index to delete
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo delete(String uid) throws MeilisearchException {
        String requestQuery = "/indexes/" + uid;
        return httpClient.delete(requestQuery, TaskInfo.class);
    }
}
