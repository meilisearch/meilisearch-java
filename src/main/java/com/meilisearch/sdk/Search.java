package com.meilisearch.sdk;

public class Search {
	private final MeiliSearchHttpRequest meilisearchHttpRequest;

	protected Search(Config config) {
		meilisearchHttpRequest = new MeiliSearchHttpRequest(config);
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
				  String[] attributesToRetrieve,
				  String[] attributesToCrop,
				  int cropLength,
				  String attributesToHighlight,
				  String filters,
				  boolean matches
	) throws Exception {
		String requestQuery = "/indexes/" + uid + "/search";
		SearchRequest sr = new SearchRequest(q, offset, limit, attributesToRetrieve, attributesToCrop, cropLength, attributesToHighlight, filters, matches);
		return meilisearchHttpRequest.get(requestQuery, sr.getQuery());
	}

	String search(String uid, SearchRequest searchRequest) throws Exception {
		String requestQuery = "/indexes/" + uid + "/search";
		return meilisearchHttpRequest.get(requestQuery, searchRequest.getQuery());
	}
}
