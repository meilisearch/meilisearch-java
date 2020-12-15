package com.meilisearch.sdk;

/**
 * Search Object for searching on indexes
 */
public class Search {
	private final MeiliSearchHttpRequest meilisearchHttpRequest;

	/**
	 * Constructor for the Meilisearch Search object
	 * @param config Meilisearch configuration
	 */
	protected Search(Config config) {
		meilisearchHttpRequest = new MeiliSearchHttpRequest(config);
	}

	/**
	 * Performs a search on a given index with a given query
	 * @param uid Index identifier
	 * @param q Query to search on index
	 * @return search results
	 * @throws Exception Search Exception or Client Error
	 */
	String search(String uid, String q) throws Exception {
		String requestQuery = "/indexes/" + uid + "/search";
		SearchRequest sr = new SearchRequest(q);
		return meilisearchHttpRequest.post(requestQuery, sr.getQuery());
	}

	/**
	 * Performs a search on a given index with a given query
	 * @param q Query string
	 * @param offset Number of documents to skip
	 * @param limit Maximum number of documents returned
	 * @param attributesToRetrieve Attributes to display in the returned documents
	 * @param attributesToCrop Attributes whose values have been cropped
	 * @param cropLength Length used to crop field values
	 * @param attributesToHighlight Attributes whose values will contain highlighted matching terms
	 * @param filters Filter queries by an attribute value
	 * @param matches Defines whether an object that contains information about the matches should be returned or not
	 * @return search results
	 * @throws Exception Search Exception or Client Error
	 */
	String search(String uid,
				  String q,
				  int offset,
				  int limit,
				  String[] attributesToRetrieve,
				  String[] attributesToCrop,
				  int cropLength,
				  String[] attributesToHighlight,
				  String filters,
				  boolean matches
	) throws Exception {
		String requestQuery = "/indexes/" + uid + "/search";
		SearchRequest sr = new SearchRequest(q, offset, limit, attributesToRetrieve, attributesToCrop, cropLength, attributesToHighlight, filters, matches);
		return meilisearchHttpRequest.post(requestQuery, sr.getQuery());
	}

	/**
	 * Performs a search on a given index with a given query
	 * @param uid Index identifier
	 * @param searchRequest SearchRequest to search on index
	 * @return search results
	 * @throws Exception Search Exception or Client Error
	 */
	String search(String uid, SearchRequest searchRequest) throws Exception {
		String requestQuery = "/indexes/" + uid + "/search";
		return meilisearchHttpRequest.post(requestQuery, searchRequest.getQuery());
	}
}
