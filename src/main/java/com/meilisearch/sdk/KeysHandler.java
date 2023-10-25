package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.http.URLBuilder;
import com.meilisearch.sdk.model.Key;
import com.meilisearch.sdk.model.KeyUpdate;
import com.meilisearch.sdk.model.KeysQuery;
import com.meilisearch.sdk.model.Results;

/**
 * Class covering the Meilisearch Key API
 *
 * <p><a href="https://www.meilisearch.com/docs/reference/api/keys">https://www.meilisearch.com/docs/reference/api/keys</a>
 */
public class KeysHandler {
    private final HttpClient httpClient;

    /**
     * Creates and sets up an instance of Key to simplify Meilisearch API calls to manage keys
     *
     * @param config Meilisearch configuration
     */
    protected KeysHandler(Config config) {
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
        return httpClient.get(keysPath().addSubroute(uid).getURL(), Key.class);
    }

    /**
     * Retrieves keys from the client
     *
     * @return Results containing a list of Key instance
     * @throws MeilisearchException if client request causes an error
     */
    Results<Key> getKeys() throws MeilisearchException {
        return httpClient.get(keysPath().getURL(), Results.class, Key.class);
    }

    /**
     * Retrieves keys from the client
     *
     * @param params accept by the keys route
     * @return Results containing a list of Key instance
     * @throws MeilisearchException if client request causes an error
     */
    Results<Key> getKeys(KeysQuery params) throws MeilisearchException {
        return httpClient.get(
                keysPath().addQuery(params.toQuery()).getURL(), Results.class, Key.class);
    }

    /**
     * Creates a key
     *
     * @param options Key containing the options
     * @return Key Instance
     * @throws MeilisearchException if client request causes an error
     */
    Key createKey(Key options) throws MeilisearchException {
        return httpClient.post(keysPath().getURL(), options, Key.class);
    }

    /**
     * Updates a key
     *
     * @param key String containing the key
     * @param options String containing the options of the key
     * @return Key Instance
     * @throws MeilisearchException if client request causes an error
     */
    Key updateKey(String key, KeyUpdate options) throws MeilisearchException {
        return httpClient.patch(keysPath().addSubroute(key).getURL(), options, Key.class);
    }

    /**
     * Deletes a key
     *
     * @param key String containing the key
     * @throws MeilisearchException if client request causes an error
     */
    void deleteKey(String key) throws MeilisearchException {
        httpClient.delete(keysPath().addSubroute(key).getURL(), String.class);
    }

    /** Creates an URLBuilder for the constant route keys */
    private URLBuilder keysPath() {
        return new URLBuilder("/keys");
    }
}
