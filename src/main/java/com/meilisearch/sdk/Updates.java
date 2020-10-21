package com.meilisearch.sdk;

public class Updates {
	private final MeiliSearchHttpRequest meilisearchHttpRequest;

	protected Updates(Config config) {
		meilisearchHttpRequest = new MeiliSearchHttpRequest(config);
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
