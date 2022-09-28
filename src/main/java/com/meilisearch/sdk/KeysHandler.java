package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.Key;
import com.meilisearch.sdk.model.Result;

/**
 * Wrapper around MeilisearchHttpRequest class to use for Meilisearch keys
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/keys.html
 */
public class KeysHandler {
    private final MeilisearchHttpRequest meilisearchHttpRequest;

    /**
     * Creates and sets up an instance of Key to simplify Meilisearch API calls to manage keys
     *
     * @param config Meilisearch configuration
     */
    public KeysHandler(Config config) {
        this.meilisearchHttpRequest = new MeilisearchHttpRequest(config);
    }

    /**
     * Retrieves the Key with the specified uid
     *
     * @param uid Identifier of the requested Key
     * @return Key instance
     * @throws MeilisearchException if client request causes an error
     */
    public Key getKey(String uid) throws MeilisearchException {
        String urlPath = "/keys/" + uid;
        return meilisearchHttpRequest.jsonHandler.decode(
                this.meilisearchHttpRequest.get(urlPath), Key.class);
    }

    /**
     * Retrieves Keys from the client
     *
     * @return List of key instance
     * @throws MeilisearchException if client request causes an error
     */
    public Result<Key> getKeys() throws MeilisearchException {
        String urlPath = "/keys";
        Result<Key> result =
                meilisearchHttpRequest.jsonHandler.decode(
                        this.meilisearchHttpRequest.get(urlPath), Result.class, Key.class);
        return result;
    }

    /**
     * Creates a key
     *
     * @param options Key containing the options
     * @return Key Instance
     * @throws MeilisearchException if client request causes an error
     */
    public Key createKey(Key options) throws MeilisearchException {
        String urlPath = "/keys";
        return meilisearchHttpRequest.jsonHandler.decode(
                this.meilisearchHttpRequest.post(urlPath, options), Key.class);
    }

    /**
     * Deletes a key
     *
     * @param key String containing the key
     * @throws MeilisearchException if client request causes an error
     */
    public void deleteKey(String key) throws MeilisearchException {
        String urlPath = "/keys/" + key;
        this.meilisearchHttpRequest.delete(urlPath);
    }
}
