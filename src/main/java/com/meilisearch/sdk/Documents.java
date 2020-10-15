package com.meilisearch.sdk;

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

	String deleteDocument(String uid, String identifier) throws Exception {
		String requestQuery = "/indexes/" + uid + "/documents/" + identifier;
		return meilisearchHttpRequest.delete(requestQuery);
	}

	String deleteDocuments(String uid) throws Exception {
		String requestQuery = "/indexes/" + uid + "/documents";
		return meilisearchHttpRequest.delete(requestQuery);
	}

	String search(String uid, String q) throws Exception {
		String requestQuery = "/indexes/" + uid + "/search";
		SearchRequest sr = new SearchRequest(q);
		return meilisearchHttpRequest.get(requestQuery, sr.getQuery());
	}

	String search(String uid,
				  String q,
				  int offset,
				  int limit,
				  String attributesToRetrieve,
				  String attributesToCrop,
				  int cropLength,
				  String attributesToHighlight,
				  String filters,
				  boolean matches
	) throws Exception {
		String requestQuery = "/indexes/" + uid + "/search";
		SearchRequest sr = new SearchRequest(q, offset, limit, attributesToRetrieve, attributesToCrop, cropLength, attributesToHighlight, filters, matches);
		return meilisearchHttpRequest.get(requestQuery, sr.getQuery());
	}

	String getUpdate(String uid, int updateId) throws Exception {
		String requestQuery = "/indexes/" + uid + "/updates/" + updateId;
		return meilisearchHttpRequest.get(requestQuery);
	}

	String getUpdates(String uid) throws Exception {
		String requestQuery = "/indexes/" + uid + "/updates";
		return meilisearchHttpRequest.get(requestQuery);
	}
}
