package com.meilisearch.sdk;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;

/**
 * Wrapper around MeilisearchHttpRequest class to use for MeiliSearch keys
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/keys.html
 */
public class KeysHandler {
    private final MeiliSearchHttpRequest meilisearchHttpRequest;
    private final Gson gson =
            new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

    /**
     * Creates and sets up an instance of Key to simplify MeiliSearch API calls to manage keys
     *
     * @param config MeiliSearch configuration
     */
    public KeysHandler(Config config) {
        this.meilisearchHttpRequest = new MeiliSearchHttpRequest(config);
    }

    /**
     * Retrieves the Key with the specified uid
     *
     * @param uid Identifier of the requested Key
     * @return Key instance
     * @throws Exception if client request causes an error
     */
    public Key getKey(String uid) throws Exception, MeiliSearchApiException {
        String urlPath = "/keys/" + uid;
        return this.gson.fromJson(this.meilisearchHttpRequest.get(urlPath), Key.class);
    }

    /**
     * Retrieves Keys from the client
     *
     * @return List of key instance
     * @throws Exception if client request causes an error
     */
    public Key[] getKeys() throws Exception {
        String urlPath = "/keys";
        Result<Key> result =
                this.gson.fromJson(
                        this.meilisearchHttpRequest.get(urlPath),
                        new TypeToken<Result<Key>>() {}.getType());
        return result.getResults();
    }

    /**
     * Creates a key
     *
     * @param options Key containing the options
     * @return Key Instance
     * @throws Exception if client request causes an error
     */
    public Key createKey(Key options) throws Exception {
        String urlPath = "/keys";
        return this.gson.fromJson(
                this.meilisearchHttpRequest.post(urlPath, options.toString()), Key.class);
    }

    /**
     * Deletes a key
     *
     * @param key String containing the key
     * @throws Exception if client request causes an error
     */
    public void deleteKey(String key) throws Exception {
        String urlPath = "/keys/" + key;
        this.meilisearchHttpRequest.delete(urlPath);
    }
}
