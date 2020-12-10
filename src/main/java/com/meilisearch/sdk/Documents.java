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

	String getDocument(String uid, String identifier) throws Exception {
		String requestQuery = "/indexes/" + uid + "/documents/" + identifier;
		return meilisearchHttpRequest.get(requestQuery);
	}

	String getDocuments(String uid) throws Exception {
		String requestQuery = "/indexes/" + uid + "/documents";
		return meilisearchHttpRequest.get(requestQuery);
	}

	String getDocuments(String uid, int limit) throws Exception {
		String requestQuery = "/indexes/" + uid + "/documents?limit=" + limit;
		return meilisearchHttpRequest.get(requestQuery);
	}

	String addDocuments(String uid, String document) throws Exception {
		String requestQuery = "/indexes/" + uid + "/documents";
		return meilisearchHttpRequest.post(requestQuery, document);
	}

	String updateDocuments(String uid, String document) throws Exception {
		String requestQuery = "/indexes/" + uid + "/documents";
		return meilisearchHttpRequest.put(requestQuery, document);
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
