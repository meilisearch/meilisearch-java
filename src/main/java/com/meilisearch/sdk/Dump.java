package com.meilisearch.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;

/**
 * Wrapper around the MeilisearchHttpRequest class to ease usage for Meilisearch dumps
 */
public class Dump {
	private MeiliSearchHttpRequest meiliSearchHttpRequest;
	private String status;
	private String uid;

	/**
	 * Creates and sets up an instance of Dump to simplify Meilisearch API calls to manage dumps
	 *
	 * @param config Meilisearch configuration
	 */
	public Dump(Config config) { this.meiliSearchHttpRequest = new MeiliSearchHttpRequest(config); }

	/**
	 * Creates a dump
	 * Refer https://docs.meilisearch.com/references/dump.html#create-a-dump
	 *
	 * @return Dump object with Meilisearch API response
	 * @throws Exception if something goes wrong
	 */
	public Dump createDump() throws Exception, MeiliSearchApiException {
		Dump dump = new Gson().fromJson(
			this.meiliSearchHttpRequest.post("/dumps", ""),
			Dump.class
		);

		return dump;
	}

	/**
	 * Gets dump status
	 * Refer https://docs.meilisearch.com/references/dump.html#get-dump-status
	 *
	 * @param uid Unique identifier for correspondent dump
	 * @return Meilisearch API response with dump status and dump uid
	 * @throws Exception if something goes wrong
	 */
	public String getStatusByUid(String uid) throws Exception, MeiliSearchApiException {
		return this.meiliSearchHttpRequest.get("/dumps/" + uid + "/status");
	}

	public String getStatus() {
		return status;
	}

	public String getUid() {
		return uid;
	}


}
