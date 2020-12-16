package com.meilisearch.sdk;

/**
 * Wrapper around MeilisearchHttpRequest class to use for Meilisearch updates
 */
public class Updates {
	private final MeiliSearchHttpRequest meilisearchHttpRequest;

	protected Updates(Config config) {
		meilisearchHttpRequest = new MeiliSearchHttpRequest(config);
	}

	/**
	 * Retrieves the Update at the specified iud with the specified updateId
	 *
	 * @param uid Index identifier to the requested Update
	 * @param updateId Identifier of the requested Update
	 * @return String containing the requested Update
	 * @throws Exception if client request causes an error
	 */
	String getUpdate(String uid, int updateId) throws Exception {
		String requestQuery = "/indexes/" + uid + "/updates/" + updateId;
		return meilisearchHttpRequest.get(requestQuery);
	}

	/**
	 * Retrieves all Updates at the specified iud
	 *
	 * @param uid Index identifier to the requested Updates
	 * @return String containing the requested Updates
	 * @throws Exception if client request causes an error
	 */
	String getUpdates(String uid) throws Exception {
		String requestQuery = "/indexes/" + uid + "/updates";
		return meilisearchHttpRequest.get(requestQuery);
	}
}
