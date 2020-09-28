package meilisearch;

import java.io.Serializable;

import com.google.gson.*;
import lombok.Getter;
import lombok.ToString;

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

	/**
	 * Set the Meilisearch configuration for the index
	 *
	 * @param config Meilisearch configuration to use
	 */
	void setConfig(Config config) {
		this.config = config;
		this.documents = new Documents(config);
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
	 * Delete all documents in the index
	 *
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public String deleteDocuments() throws Exception {
		return this.documents.deleteDocuments(this.uid);
	}

	/**
	 * Search documents in index
	 *
	 * @param q Query string
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public String search(String q) throws Exception {
		return this.documents.search(this.uid, q);
	}

	/**
	 * Get an index update by its id
	 *
	 * @param updateId ID of the index update
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public String getUpdate(int updateId) throws Exception {
		return this.documents.getUpdate(this.uid, updateId);
	}

	/**
	 * Get all updates of the index
	 *
	 * @return List of updates in the index
	 * @throws Exception If something goes wrong
	 */
	public UpdateStatus[] getUpdates()  throws Exception {
		Gson gson = new Gson();
		return gson.fromJson(this.documents.getUpdates(this.uid), UpdateStatus[].class);
	}
}
