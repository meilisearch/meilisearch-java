package com.meilisearch.sdk;

import com.google.gson.JsonObject;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;

/** Wrapper around the MeilisearchHttpRequest class to ease usage for MeiliSearch indexes */
class IndexesHandler {
    MeiliSearchHttpRequest meilisearchHttpRequest;

    /**
     * Creates and sets up an instance of IndexesHandler to simplify MeiliSearch API calls to manage
     * indexes
     *
     * @param config MeiliSearch configuration
     */
    IndexesHandler(Config config) {
        this.meilisearchHttpRequest = new MeiliSearchHttpRequest(config);
    }

    /**
     * Creates an index with a unique identifier
     *
     * @param uid Unique identifier to create the index with
     * @return MeiliSearch API response
     * @throws Exception if an error occurs
     */
    String create(String uid) throws Exception, MeiliSearchApiException {
        return this.create(uid, null);
    }

    /**
     * Creates an index with a unique identifier
     *
     * @param uid Unique identifier to create the index with
     * @param primaryKey Field to use as the primary key for documents in that index
     * @return MeiliSearch API response
     * @throws Exception if an error occurs
     */
    String create(String uid, String primaryKey) throws Exception, MeiliSearchApiException {
        JsonObject params = new JsonObject();
        params.addProperty("uid", uid);
        if (primaryKey != null) {
            params.addProperty("primaryKey", primaryKey);
        }
        return meilisearchHttpRequest.post("/indexes", params.toString());
    }

    /**
     * Gets an index from its unique identifier
     *
     * @param uid Unique identifier of the index to get
     * @return MeiliSearch API response
     * @throws Exception if an error occurs
     */
    String get(String uid) throws Exception {
        String requestQuery = "/indexes/" + uid;
        return meilisearchHttpRequest.get(requestQuery);
    }

    /**
     * Gets all indexes in the current MeiliSearch instance
     *
     * @return MeiliSearch API response
     * @throws Exception if an error occurs
     */
    String getAll() throws Exception {
        return meilisearchHttpRequest.get("/indexes");
    }

    /**
     * Updates the primary key of an index in the MeiliSearch instance
     *
     * @param uid Unique identifier of the index to update
     * @param primaryKey New primary key field to use for documents in that index
     * @return MeiliSearch API response
     * @throws Exception if an error occurs
     */
    String updatePrimaryKey(String uid, String primaryKey) throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("primaryKey", primaryKey);

        String requestQuery = "/indexes/" + uid;
        return meilisearchHttpRequest.put(requestQuery, jsonObject.toString());
    }

    /**
     * Deletes an index in the MeiliSearch instance
     *
     * @param uid Unique identifier of the index to delete
     * @return MeiliSearch API response
     * @throws Exception if an error occurs
     */
    String delete(String uid) throws Exception {
        String requestQuery = "/indexes/" + uid;
        return meilisearchHttpRequest.delete(requestQuery);
    }
}
