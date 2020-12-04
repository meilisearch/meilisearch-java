package com.meilisearch.sdk;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Wrapper around MeilisearchHttpRequest class to use for Meilisearch documents
 */
class Documents {
	private final MeiliSearchHttpRequest meilisearchHttpRequest;

	protected Documents(Config config) {
		meilisearchHttpRequest = new MeiliSearchHttpRequest(config);
	}

	/**
	 * Retrieves the document at the specified uid with the specified identifier
	 * @param uid Partial path to the requested document
	 * @param identifier ID of the document
	 * @return Requested document
	 * @throws Exception if client request causes an error
	 */
	String getDocument(String uid, String identifier) throws Exception {
		String requestQuery = "/indexes/" + uid + "/documents/" + identifier;
		return meilisearchHttpRequest.get(requestQuery);
	}

	/**
	 * Retrieves the documents at the specified uid
	 * @param uid Partial path to the requested documents
	 * @return Requested documents
	 * @throws Exception if the client request causes an error
	 */
	String getDocuments(String uid) throws Exception {
		String requestQuery = "/indexes/" + uid + "/documents";
		return meilisearchHttpRequest.get(requestQuery);
	}

	/**
	 * Retrieves the documents at the specified uid
	 * @param uid Partial path to the requested documents
	 * @param limit Limit on the requested documents to be returned
	 * @return Requested documents
	 * @throws Exception if the client request causes an error
	 */
	String getDocuments(String uid, int limit) throws Exception {
		String requestQuery = "/indexes/" + uid + "/documents?limit=" + limit;
		return meilisearchHttpRequest.get(requestQuery);
	}

	String addDocuments(String uid, String document) throws Exception {
		String requestQuery = "/indexes/" + uid + "/documents";
		return meilisearchHttpRequest.post(requestQuery, document);
	}

	String deleteDocument(String uid, String identifier) throws Exception {
		String requestQuery = "/indexes/" + uid + "/documents/" + identifier;
		return meilisearchHttpRequest.delete(requestQuery);
	}

	String deleteDocuments(String uid, List<String> identifiers) throws Exception {
		String requestQuery = "/indexes/" + uid + "/documents/" + "delete-batch";
		JsonArray requestData = new JsonArray(identifiers.size());
		identifiers.forEach(requestData::add);

		return meilisearchHttpRequest.post(requestQuery,requestData.toString());
	}

	String deleteAllDocuments(String uid) throws Exception {
		String requestQuery = "/indexes/" + uid + "/documents";
		return meilisearchHttpRequest.delete(requestQuery);
	}
}
