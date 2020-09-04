/*
 * Unofficial Java client for MeiliSearch
 */
package meilisearch;

import com.google.gson.Gson;

/**
 * Meilisearch client
 */
public class Client {
	public MeilisearchConfig config;
	public Index index;
	public Gson gson;

	/**
	 * Call instance for MeiliSearch client
	 *
	 * @param config Configuration to connect to Meilisearch instance
	 */
	public Client(MeilisearchConfig config) {
		this.config = config;
		gson = new Gson();
		this.index = new Index(config);
	}

	/**
	 * Create index
	 * Refer https://docs.meilisearch.com/references/indexes.html#create-an-index
	 *
	 * @param uid Unique identifier for the index to create
	 * @return Meilisearch API response
	 * @throws Exception If an error occurs
	 */
	public String createIndex (String uid) throws Exception {
		return this.index.create(uid);
	}

	/**
	 * Create index
	 * Refer https://docs.meilisearch.com/references/indexes.html#create-an-index
	 *
	 * @param uid Unique identifier for the index to create
	 * @param primaryKey The primary key of the documents in that index
	 * @return Meilisearch API response
	 * @throws Exception If an error occurs
	 */
	public String createIndex (String uid, String primaryKey) throws Exception {
		return this.index.create(uid, primaryKey);
	}

	/**
	 * Get all indexes
	 * Refer https://docs.meilisearch.com/references/indexes.html#list-all-indexes
	 *
	 * @return List of indexes in the Meilisearch client
	 * @throws Exception If an error occurs
	 */
	public Indexes[] getIndexList () throws Exception {
		Indexes[] indexList = gson.fromJson(this.index.getAll(), Indexes[].class);
		for (Indexes indexes: indexList) {
			indexes.setConfig(this.config);
		}
		return indexList;
	}

	/**
	 * Get single index by uid
	 * Refer https://docs.meilisearch.com/references/indexes.html#get-one-index
	 *
	 * @param uid Unique identifier of the index to get
	 * @return Meilisearch API response
	 * @throws Exception If an error occurs
	 */
	public Indexes getIndex (String uid) throws Exception {
		Indexes indexes = gson.fromJson(this.index.get(uid), Indexes.class);
		indexes.setConfig(this.config);
		return indexes;
	}

	/**
	 * Update index by uid
	 * Refer https://docs.meilisearch.com/references/indexes.html#update-an-index
	 *
	 * @param uid Unique identifier of the index to update
	 * @param primaryKey Primary key of the documents in the index
	 * @return Meilisearch API response
	 * @throws Exception If an error occurs
	 */
	public String updateIndex (String uid, String primaryKey) throws Exception {
		return this.index.update(uid, primaryKey);
	}

	/**
	 * Delete single index by uid
	 * Refer https://docs.meilisearch.com/references/indexes.html#get-one-index
	 *
	 * @param uid Unique identifier of the index to delete
	 * @return Meilisearch API response
	 * @throws Exception If an error occurs
	 */
	public String deleteIndex (String uid) throws Exception {
		return this.index.delete(uid);
	}
}
