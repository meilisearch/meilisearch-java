package com.meilisearch.sdk;

import com.google.gson.Gson;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;

/** Wrapper around the MeiliSearchHttpRequest class to ease usage for Meilisearch dumps */
public class DumpHandler {

    private MeiliSearchHttpRequest meiliSearchHttpRequest;

    /**
     * Creates and sets up an instance of Dump to simplify Meilisearch API calls to manage dumps
     *
     * @param meiliSearchHttpRequest Meilisearch Http Request
     */
    public DumpHandler(MeiliSearchHttpRequest meiliSearchHttpRequest) {
        this.meiliSearchHttpRequest = meiliSearchHttpRequest;
    }

    /**
     * Creates a dump Refer https://docs.meilisearch.com/reference/api/dump.html#create-a-dump
     *
     * @return Dump object with Meilisearch API response
     * @throws Exception if an error occurs
     */
    public Dump createDump() throws Exception, MeiliSearchApiException {
        return new Gson().fromJson(this.meiliSearchHttpRequest.post("/dumps", ""), Dump.class);
    }

    /**
     * Gets dump status Refer https://docs.meilisearch.com/reference/api/dump.html#get-dump-status
     *
     * @param uid Unique identifier for correspondent dump
     * @return Meilisearch API response with dump status and dump uid
     * @throws Exception if an error occurs
     */
    public String getDumpStatus(String uid) throws Exception, MeiliSearchApiException {
        return this.meiliSearchHttpRequest.get("/dumps/" + uid + "/status");
    }
}
