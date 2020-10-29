package com.meilisearch.sdk;

import com.google.gson.JsonObject;

import com.meilisearch.sdk.exceptions.MeiliSearchApiException;

/**
 * Wrapper around the MeilisearchHttpRequest class to ease usage for Meilisearch indexes
 */
class IndexesHandler {
	MeiliSearchHttpRequest meilisearchHttpRequest;

	/**
	 * Create and setup an instance of IndexesHandler to simplify Meilisearch API calls to manage indexes
	 *
	 * @param config Meilisearch configuration
	 */
	IndexesHandler(Config config) {
		this.meilisearchHttpRequest = new MeiliSearchHttpRequest(config);
	}

	/**
	 * Create an index with a unique identifier
	 *
	 * @param uid Unique identifier to create the index with
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	String create(String uid) throws Exception, MeiliSearchApiException {
		return this.create(uid, null);
	}

	/**
	 * Create an index with a unique identifier
	 *
	 * @param uid        Unique identifier to create the index with
	 * @param primaryKey Field to use as the primary key for documents in that index
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
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
	 * Get an index from its unique identifier
	 *
	 * @param uid Unique identifier of the index to get
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	String get(String uid) throws Exception {
		String requestQuery = "/indexes/" + uid;
		return meilisearchHttpRequest.get(requestQuery);
	}

	/**
	 * Get all indexes in the current Meilisearch instance
	 *
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	String getAll() throws Exception {
		return meilisearchHttpRequest.get("/indexes");
	}

	/**
	 * Update the primary key of an index in the Meilisearch instance
	 *
	 * @param uid        Unique identifier of the index to update
	 * @param primaryKey New primary key field to use for documents in that index
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	String updatePrimaryKey(String uid, String primaryKey) throws Exception {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("primaryKey", primaryKey);

		String requestQuery = "/indexes/" + uid;
		return meilisearchHttpRequest.put(requestQuery, jsonObject.toString());
	}

	/**
	 * Delete an index in the Meilisearch instance
	 *
	 * @param uid Unique identifier of the index to delete
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	String delete(String uid) throws Exception {
		String requestQuery = "/indexes/" + uid;
		return meilisearchHttpRequest.delete(requestQuery);
	}
}
