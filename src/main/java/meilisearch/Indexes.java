package meilisearch;

import java.io.Serializable;

import com.google.gson.*;
import lombok.Getter;

/**
 * Meilisearch index
 */
public class Indexes implements Serializable {
	@Getter
	String name;

	@Getter
	String uid;

	@Getter
	String primaryKey;

	@Getter
	String createdAt;

	@Getter
	String updatedAt;

	@Getter
	MeilisearchConfig config;

	Documents documents;

	/**
	 * Set the Meilisearch configuration for the index
	 * @param config Meilisearch configuration to use
	 */
	void setConfig(MeilisearchConfig config) {
		this.config = config;
		this.documents = new Documents(config);
	}

	/**
	 * getDocument
	 *
	 * @param identifier
	 * @return
	 * @throws Exception
	 */
	public String getDocument(String identifier) throws Exception {
		return this.documents.getDocument(this.uid, identifier);
	}

	/**
	 * getDocuments
	 *
	 * @return
	 * @throws Exception
	 */
	public String getDocuments() throws Exception {
		return this.documents.getDocuments(this.uid);
	}

	public String getDocuments(int limits) throws Exception {
		return this.documents.getDocuments(this.uid, limits);
	}

	/**
	 * addDocument
	 *
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public String addDocument(String document) throws Exception {
		return this.documents.addDocument(this.uid, document);
	}

	/**
	 * deleteDocument
	 *
	 * @param identifier
	 * @return
	 * @throws Exception
	 */
	public String deleteDocument(String identifier) throws Exception {
		return this.documents.deleteDocument(this.uid, identifier);
	}

	public String deleteDocuments() throws Exception {
		return this.documents.deleteDocuments(this.uid);
	}

	/**
	 * search
	 *
	 * @param q
	 * @return
	 * @throws Exception
	 */
	public String search(String q) throws Exception {
		return this.documents.search(this.uid, q);
	}

	public String getUpdate(int updateId) throws Exception {
		return this.documents.getUpdate(this.uid, updateId);
	}

	public UpdateStatus[] getUpdates()  throws Exception {
		Gson gson = new Gson();
		return gson.fromJson(this.documents.getUpdates(this.uid), UpdateStatus[].class);
	}

	@Override
	public String toString() {
		// TODO: update format
		return "Indexes:" + name + " / uid: " + uid + " / pk: " + primaryKey;
	}
}
