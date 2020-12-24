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

	@ToString.Exclude
	Settings settings;

	Gson gson = new Gson();

	/**
	 * Sets the Meilisearch configuration for the index
	 *
	 * @param config Meilisearch configuration to use
	 */
	void setConfig(Config config) {
		this.config = config;
		this.documents = new Documents(config);
		this.updates = new Updates(config);
		this.search = new Search(config);
		this.settings = new Settings(config);
	}

	/**
	 * Gets a document in the index
	 *
	 * @param identifier Identifier of the document to get
	 * @return Meilisearch API response
	 * @throws Exception if something goes wrong
	 */
	public String getDocument(String identifier) throws Exception {
		return this.documents.getDocument(this.uid, identifier);
	}

	/**
	 * Gets documents in the index
	 *
	 * @return Meilisearch API response
	 * @throws Exception if something goes wrong
	 */
	public String getDocuments() throws Exception {
		return this.documents.getDocuments(this.uid);
	}

	/**
	 * Gets documents in the index and limit the number of documents returned
	 *
	 * @param limits Maximum amount of documents to return
	 * @return Meilisearch API response
	 * @throws Exception if something goes wrong
	 */
	public String getDocuments(int limits) throws Exception {
		return this.documents.getDocuments(this.uid, limits);
	}

	/**
	 * Adds a document in the index
	 *
	 * @param document Document to add in JSON string format
	 * @return Meilisearch API response
	 * @throws Exception if something goes wrong
	 */
	public String addDocuments(String document) throws Exception {
		return this.documents.addDocuments(this.uid, document);
	}

	/**
	 * Updates a document in the index
	 *
	 * @param document Document to update in JSON string format
	 * @return Meilisearch API response
	 * @throws Exception if something goes wrong
	 */
	public String updateDocuments(String document) throws Exception {
		return this.documents.updateDocuments(this.uid, document);
	}

	/**
	 * Deletes a document from the index
	 *
	 * @param identifier Identifier of the document to delete
	 * @return Meilisearch API response
	 * @throws Exception if something goes wrong
	 */
	public String deleteDocument(String identifier) throws Exception {
		return this.documents.deleteDocument(this.uid, identifier);
	}

	/**
	 * Deletes list of documents from the index
	 * @param documentsIdentifiers list of identifiers of documents to delete
	 * @return Meilisearch API response
	 * @throws Exception if something goes wrong
	 */
	public String deleteDocuments(List<String> documentsIdentifiers) throws Exception{
		return this.documents.deleteDocuments(this.uid, documentsIdentifiers);
	}

	/**
	 * Deletes all documents in the index
	 *
	 * @return Meilisearch API response
	 * @throws Exception if something goes wrong
	 */
	public String deleteAllDocuments() throws Exception {
		return this.documents.deleteAllDocuments(this.uid);
	}

	/**
	 * Searches documents in index
	 *
	 * @param q Query string
	 * @return Meilisearch API response
	 * @throws Exception if something goes wrong
	 */
	public String search(String q) throws Exception {
		return this.search.search(this.uid, q);
	}

	/**
	 * Searches documents in index
	 *
	 * @param sr SearchRequest SearchRequest
	 * @return Meilisearch API response
	 * @throws Exception if something goes wrong
	 */
	public String search(SearchRequest sr) throws Exception {
		return this.search.search(this.uid, sr);
	}

	/**
	 * Gets the settings of the index
	 * Refer https://docs.meilisearch.com/references/settings.html#get-settings
	 *
	 * @return settings of a given uid as String
	 * @throws Exception if something goes wrong
	 */
	public String getSettings() throws Exception {
		return this.settings.getSettings(this.uid);
	}

	/**
	 * Updates the settings in index
	 * Refer https://docs.meilisearch.com/references/settings.html#update-settings
	 *
	 * @param settingsRequest the object that contains the data with the new settings
	 * @return updateId is the id of the update
	 * @throws Exception if something goes wrong
	 */
	public String updateSettings(SettingsRequest settingsRequest) throws Exception {
		return this.settings.updateSettings(this.uid, settingsRequest);
	}

	/**
	 * Resets the settings of the index
	 * Refer https://docs.meilisearch.com/references/settings.html#reset-settings
	 *
	 * @return updateId is the id of the update
	 * @throws Exception if something goes wrong
	 */
	public void resetSettings() throws Exception {
		this.settings.resetSettings(this.uid);
	}

	/**
	 * Gets an index update by its id
	 *
	 * @param updateId ID of the index update
	 * @return UpdateStatus
	 * @throws Exception if something goes wrong
	 */
	public UpdateStatus getUpdate(int updateId) throws Exception {
		return this.gson.fromJson(
			this.updates.getUpdate(this.uid, updateId),
			UpdateStatus.class
		);
	}

	/**
	 * Gets all updates of the index
	 *
	 * @return list of updates in the index
	 * @throws Exception if something goes wrong
	 */
	public UpdateStatus[] getUpdates() throws Exception {
		return this.gson.fromJson(
			this.updates.getUpdates(this.uid),
			UpdateStatus[].class
		);
	}

	/**
	 * Waits for a pending update to be processed
	 *
	 * @param updateId ID of the index update
	 * @throws Exception if timeout is reached
	 */
	public void waitForPendingUpdate(int updateId) throws Exception {
		this.waitForPendingUpdate(updateId, 5000, 50);
	}

	/**
	 * Waits for a pending update to be processed
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
	 * Fetches the primary key of the index in the Meilisearch instance
	 *
	 * @throws Exception if something goes wrong
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
