package com.meilisearch.sdk;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * Meilisearch index
 */
@ToString
public class Index implements Serializable {
	@Getter
	String uid;

	@Getter
	String primaryKey;

	@Getter
	String createdAt;

	@Getter
	@ToString.Exclude
	String updatedAt;

	@Getter
	@ToString.Exclude
	Config config;

	@ToString.Exclude
	Documents documents;

	@ToString.Exclude
	Updates updates;

	@ToString.Exclude
	Search search;

	Gson gson = new Gson();

	/**
	 * Set the Meilisearch configuration for the index
	 *
	 * @param config Meilisearch configuration to use
	 */
	void setConfig(Config config) {
		this.config = config;
		this.documents = new Documents(config);
		this.updates = new Updates(config);
		this.search = new Search(config);
	}

	/**
	 * Get a document in the index
	 *
	 * @param identifier Identifier of the document to get
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public String getDocument(String identifier) throws Exception {
		return this.documents.getDocument(this.uid, identifier);
	}

	/**
	 * Get documents in the index
	 *
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public String getDocuments() throws Exception {
		return this.documents.getDocuments(this.uid);
	}

	/**
	 * Get documents in the index and limit the number of documents returned
	 *
	 * @param limits Maximum amount of documents to return
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public String getDocuments(int limits) throws Exception {
		return this.documents.getDocuments(this.uid, limits);
	}

	/**
	 * Add a document in the index
	 *
	 * @param document Document to add in JSON string format
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public String addDocuments(String document) throws Exception {
		return this.documents.addDocuments(this.uid, document);
	}

	/**
	 * Delete a document from the index
	 *
	 * @param identifier Identifier of the document to delete
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public String deleteDocument(String identifier) throws Exception {
		return this.documents.deleteDocument(this.uid, identifier);
	}

	/**
	 * Delete list of documents from the index
	 * @param documentsIdentifiers list of identifiers of documents to delete
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public String deleteDocuments(List<String> documentsIdentifiers) throws Exception{
		return this.documents.deleteDocuments(this.uid, documentsIdentifiers);
	}

	/**
	 * Delete all documents in the index
	 *
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public String deleteAllDocuments() throws Exception {
		return this.documents.deleteAllDocuments(this.uid);
	}

	/**
	 * Search documents in index
	 *
	 * @param q Query string
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public String search(String q) throws Exception {
		return this.search.search(this.uid, q);
	}

	/**
	 * Search documents in index
	 *
	 * @param sr SearchRequest SearchRequest
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public String search(SearchRequest sr) throws Exception {
		return this.search.search(this.uid, sr);
	}

	/**
	 * Get an index update by its id
	 *
	 * @param updateId ID of the index update
	 * @return UpdateStatus
	 * @throws Exception If something goes wrong
	 */
	public UpdateStatus getUpdate(int updateId) throws Exception {
		return this.gson.fromJson(
			this.updates.getUpdate(this.uid, updateId),
			UpdateStatus.class
		);
	}

	/**
	 * Get all updates of the index
	 *
	 * @return List of updates in the index
	 * @throws Exception If something goes wrong
	 */
	public UpdateStatus[] getUpdates() throws Exception {
		return this.gson.fromJson(
			this.updates.getUpdates(this.uid),
			UpdateStatus[].class
		);
	}

	/**
	 * Wait for a pending update to be processed
	 *
	 * @param updateId ID of the index update
	 * @throws Exception If timeout is reached
	 */
	public void waitForPendingUpdate(int updateId) throws Exception {
		this.waitForPendingUpdate(updateId, 5000, 50);
	}

	/**
	 * Wait for a pending update to be processed
	 *
	 * @param updateId ID of the index update
	 * @param timeoutInMs number of milliseconds before throwing an Exception
	 * @param intervalInMs number of milliseconds before requesting the status again
	 * @throws Exception if timeout is reached
	 */
	public void waitForPendingUpdate(int updateId, int timeoutInMs, int intervalInMs) throws Exception {
		UpdateStatus updateStatus;
		String status = "";
		long startTime = new Date().getTime();
		long elapsedTime = 0;

		while (!status.equals("processed")){
			if (elapsedTime >= timeoutInMs){
				throw new Exception();
			}
			updateStatus = this.getUpdate(updateId);
			status = updateStatus.getStatus();
			Thread.sleep(intervalInMs);
			elapsedTime = new Date().getTime() - startTime;
		}
	}

	/**
	 * Fetch the primary key of the index in the Meilisearch instance
	 *
	 * @throws Exception If something goes wrong
	 */
	public void fetchPrimaryKey() throws Exception {
		String requestQuery = "/indexes/" + this.uid;
		MeiliSearchHttpRequest meilisearchHttpRequest = new MeiliSearchHttpRequest(config);
		Index retrievedIndex = new Gson().fromJson(
			meilisearchHttpRequest.get(requestQuery),
			Index.class
		);
		this.primaryKey = retrievedIndex.getPrimaryKey();
	}
}
