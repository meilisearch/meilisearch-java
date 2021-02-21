package com.meilisearch.sdk;

import com.google.gson.Gson;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;

/**
 * Wrapper around the MeilisearchHttpRequest class to ease usage for MeiliSearch dumps
 */
public class DumpHandler {

	private MeiliSearchHttpRequest meiliSearchHttpRequest;

	/**
	 * Creates and sets up an instance of Dump to simplify MeiliSearch API calls to manage dumps
	 *
	 * @param config MeiliSearch configuration
	 */
	public DumpHandler(Config config) {
		this.meiliSearchHttpRequest = new MeiliSearchHttpRequest(config);
	}

	/**
	 * Creates a dump
	 * Refer https://docs.meilisearch.com/reference/api/dump.html#create-a-dump
	 *
	 * @return Dump object with MeiliSearch API response
	 * @throws Exception if something goes wrong
	 */
	public Dump createDump() throws Exception, MeiliSearchApiException {
		return new Gson().fromJson(
			this.meiliSearchHttpRequest.post("/dumps", ""),
			Dump.class
		);
	}

	/**
	 * Gets dump status
	 * Refer https://docs.meilisearch.com/reference/api/dump.html#get-dump-status
	 *
	 * @param uid Unique identifier for correspondent dump
	 * @return MeiliSearch API response with dump status and dump uid
	 * @throws Exception if something goes wrong
	 */
	public String getDumpStatus(String uid) throws Exception, MeiliSearchApiException {
		return this.meiliSearchHttpRequest.get("/dumps/" + uid + "/status");
	}
}
