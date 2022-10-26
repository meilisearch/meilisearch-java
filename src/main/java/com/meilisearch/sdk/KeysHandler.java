package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.Key;
import com.meilisearch.sdk.model.Result;

/**
 * Wrapper around HttpClient class to use for Meilisearch keys
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/keys.html
 */
public class KeysHandler {
    private final HttpClient httpClient;

    /**
     * Creates and sets up an instance of Key to simplify Meilisearch API calls to manage keys
     *
     * @param config Meilisearch configuration
     */
    KeysHandler(Config config) {
        this.httpClient = config.httpClient;
    }

    /**
     * Retrieves the key with the specified key uid
     *
     * @param uid Identifier of the requested Key
     * @return Key instance
     * @throws MeilisearchException if client request causes an error
     */
    Key getKey(String uid) throws MeilisearchException {
        String urlPath = "/keys/" + uid;
        return httpClient.get(urlPath, Key.class);
    }

    /**
     * Retrieves keys from the client
     *
     * @return List of key instance
     * @throws MeilisearchException if client request causes an error
     */
    Result<Key> getKeys() throws MeilisearchException {
        String urlPath = "/keys";
        Result<Key> result = httpClient.get(urlPath, Result.class, Key.class);
        return result;
    }

    /**
     * Creates a key
     *
     * @param options Key containing the options
     * @return Key Instance
     * @throws MeilisearchException if client request causes an error
     */
    Key createKey(Key options) throws MeilisearchException {
        String urlPath = "/keys";
        return httpClient.post(urlPath, options, Key.class);
    }

    /**
     * Deletes a key
     *
     * @param key String containing the key
     * @throws MeilisearchException if client request causes an error
     */
    void deleteKey(String key) throws MeilisearchException {
        String urlPath = "/keys/" + key;
        httpClient.delete(urlPath, String.class);
    }
}
