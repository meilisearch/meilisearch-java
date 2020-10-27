/*
 * Unofficial Java client for MeiliSearch
 */
package com.meilisearch.sdk;

import com.google.gson.Gson;

import com.meilisearch.sdk.exceptions.MeiliSearchApiException;

/**
 * Meilisearch client
 */
public class Client {
	public Config config;
	public IndexesHandler indexesHandler;
	public Gson gson;

	/**
	 * Call instance for MeiliSearch client
	 *
	 * @param config Configuration to connect to Meilisearch instance
	 */
	public Client(Config config) {
		this.config = config;
		this.gson = new Gson();
		this.indexesHandler = new IndexesHandler(config);
	}

	/**
	 * Create index
	 * Refer https://docs.meilisearch.com/references/indexes.html#create-an-index
	 *
	 * @param uid Unique identifier for the index to create
	 * @return Meilisearch API response
	 * @throws Exception If an error occurs
	 */
	public Index createIndex(String uid) throws Exception, MeiliSearchApiException {
		return this.createIndex(uid, null);
	}

	/**
	 * Create index
	 * Refer https://docs.meilisearch.com/references/indexes.html#create-an-index
	 *
	 * @param uid        Unique identifier for the index to create
	 * @param primaryKey The primary key of the documents in that index
	 * @return Meilisearch API response
	 * @throws Exception If an error occurs
	 */
	public Index createIndex(String uid, String primaryKey) throws Exception, MeiliSearchApiException {
		Index index = gson.fromJson(this.indexesHandler.create(uid, primaryKey), Index.class);
		index.setConfig(this.config);
		return index;
	}

	/**
	 * Get all indexes
	 * Refer https://docs.meilisearch.com/references/indexes.html#list-all-indexes
	 *
	 * @return List of indexes in the Meilisearch client
	 * @throws Exception If an error occurs
	 */
	public Index[] getIndexList() throws Exception {
		Index[] meiliSearchIndexList = gson.fromJson(this.indexesHandler.getAll(), Index[].class);
		for (Index indexes : meiliSearchIndexList) {
			indexes.setConfig(this.config);
		}
		return meiliSearchIndexList;
	}

	/**
	 * Get single index by uid
	 * Refer https://docs.meilisearch.com/references/indexes.html#get-one-index
	 *
	 * @param uid Unique identifier of the index to get
	 * @return Meilisearch API response
	 * @throws Exception If an error occurs
	 */
	public Index getIndex(String uid) throws Exception {
		Index indexes = gson.fromJson(this.indexesHandler.get(uid), Index.class);
		indexes.setConfig(this.config);
		return indexes;
	}

	/**
	 * Update index by uid
	 * Refer https://docs.meilisearch.com/references/indexes.html#update-an-index
	 *
	 * @param uid        Unique identifier of the index to update
	 * @param primaryKey Primary key of the documents in the index
	 * @return Meilisearch API response
	 * @throws Exception If an error occurs
	 */
	public String updateIndex(String uid, String primaryKey) throws Exception {
		return this.indexesHandler.updatePrimaryKey(uid, primaryKey);
	}

	/**
	 * Delete single index by uid
	 * Refer https://docs.meilisearch.com/references/indexes.html#get-one-index
	 *
	 * @param uid Unique identifier of the index to delete
	 * @return Meilisearch API response
	 * @throws Exception If an error occurs
	 */
	public String deleteIndex(String uid) throws Exception {
		return this.indexesHandler.delete(uid);
	}

	/**
	 * Get single index by uid or if it does not exists, Create index
	 *
	 * @param uid        Unique identifier for the index to create
	 * @param primaryKey The primary key of the documents in that index
	 * @return Index instance
	 * @throws Exception If an error occurss
	 */
	public Index getOrCreateIndex(String uid, String primaryKey) throws Exception {
		try {
			return this.createIndex(uid, primaryKey);
		} catch (Exception e) {
			return this.getIndex(uid);
		}
	}
}
