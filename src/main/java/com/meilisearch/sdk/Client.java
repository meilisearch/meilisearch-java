/*
 * Official Java client for Meilisearch
 */
package com.meilisearch.sdk;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.json.JsonHandler;
import com.meilisearch.sdk.model.Key;
import com.meilisearch.sdk.model.Result;
import com.meilisearch.sdk.model.Task;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/** Meilisearch client */
public class Client {
    public Config config;
    public IndexesHandler indexesHandler;
    public TasksHandler tasksHandler;
    public KeysHandler keysHandler;
    public JsonHandler jsonHandler;

    /**
     * Calls instance for Meilisearch client
     *
     * @param config Configuration to connect to Meilisearch instance
     */
    public Client(Config config) {
        this.config = config;
        this.indexesHandler = new IndexesHandler(config);
        this.tasksHandler = new TasksHandler(config);
        this.keysHandler = new KeysHandler(config);
        this.jsonHandler = config.jsonHandler;
    }

    /**
     * Creates index Refer https://docs.meilisearch.com/reference/api/indexes.html#create-an-index
     *
     * @param uid Unique identifier for the index to create
     * @return Meilisearch API response as Task
     * @throws MeilisearchException if an error occurs
     */
    public Task createIndex(String uid) throws MeilisearchException {
        return this.createIndex(uid, null);
    }

    /**
     * Creates index Refer https://docs.meilisearch.com/reference/api/indexes.html#create-an-index
     *
     * @param uid Unique identifier for the index to create
     * @param primaryKey The primary key of the documents in that index
     * @return Meilisearch API response as Task
     * @throws MeilisearchException if an error occurs
     */
    public Task createIndex(String uid, String primaryKey) throws MeilisearchException {
        return this.indexesHandler.create(uid, primaryKey);
    }

    /**
     * Gets all indexes Refer
     * https://docs.meilisearch.com/reference/api/indexes.html#list-all-indexes
     *
     * @return List of indexes in the Meilisearch client
     * @throws MeilisearchException if an error occurs
     */
    public Index[] getIndexList() throws MeilisearchException {
        Index[] meiliSearchIndexList = jsonHandler.decode(getRawIndexList(), Index[].class);
        for (Index indexes : meiliSearchIndexList) {
            indexes.setConfig(this.config);
        }
        return meiliSearchIndexList;
    }

    /**
     * Gets all indexes https://docs.meilisearch.com/reference/api/indexes.html#list-all-indexes
     *
     * @return Meilisearch API response as String
     * @throws MeilisearchException if an error occurs
     */
    public String getRawIndexList() throws MeilisearchException {
        return this.indexesHandler.getAll();
    }

    /**
     * Creates a local reference to an index identified by `uid`, without doing an HTTP call.
     * Calling this method doesn't create an index by itself, but grants access to all the other
     * methods in the Index class.
     *
     * @param uid Unique identifier of the index
     * @return Index instance
     * @throws MeilisearchException if an error occurs
     */
    public Index index(String uid) throws MeilisearchException {
        Index index = new Index();
        index.uid = uid;
        index.setConfig(this.config);
        return index;
    }

    /**
     * Gets single index by uid Refer
     * https://docs.meilisearch.com/reference/api/indexes.html#get-one-index
     *
     * @param uid Unique identifier of the index to get
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public Index getIndex(String uid) throws MeilisearchException {
        Index index = jsonHandler.decode(getRawIndex(uid), Index.class);
        index.setConfig(this.config);
        return index;
    }

    /**
     * Gets single index by uid Refer
     * https://docs.meilisearch.com/reference/api/indexes.html#get-one-index
     *
     * @param uid Unique identifier of the index to get
     * @return Meilisearch API response as String
     * @throws MeilisearchException if an error occurs
     */
    public String getRawIndex(String uid) throws MeilisearchException {
        return this.indexesHandler.get(uid);
    }

    /**
     * Updates single index by uid Refer
     * https://docs.meilisearch.com/reference/api/indexes.html#update-an-index
     *
     * @param uid Unique identifier of the index to update
     * @param primaryKey Primary key of the documents in the index
     * @return Meilisearch API response as Task
     * @throws MeilisearchException if an error occurs
     */
    public Task updateIndex(String uid, String primaryKey) throws MeilisearchException {
        Task task =
                jsonHandler.decode(
                        this.indexesHandler.updatePrimaryKey(uid, primaryKey), Task.class);
        return task;
    }

    /**
     * Deletes single index by uid Refer
     * https://docs.meilisearch.com/reference/api/indexes.html#delete-one-index
     *
     * @param uid Unique identifier of the index to delete
     * @return Meilisearch API response as Task
     * @throws MeilisearchException if an error occurs
     */
    public Task deleteIndex(String uid) throws MeilisearchException {
        Task task = jsonHandler.decode(this.indexesHandler.delete(uid), Task.class);
        return task;
    }

    // TODO createDump will return a Task in v0.28
    // /**
    //  * Triggers the creation of a Meilisearch dump. Refer
    //  * https://docs.meilisearch.com/reference/api/dump.html#create-a-dump
    //  *
    //  * @return Dump instance
    //  * @throws MeilisearchException if an error occurs
    //  */
    // public Dump createDump() throws MeilisearchException {
    //     return this.dumpHandler.createDump();
    // }
    // /**
    //  * Creates a dump Refer https://docs.meilisearch.com/reference/api/dump.html#create-a-dump
    //  *
    //  * @return Dump object with Meilisearch API response
    //  * @throws MeilisearchException if an error occurs
    //  */
    // public Dump createDump() throws MeilisearchException {
    //     return this.meiliSearchHttpRequest.post("/dumps", "", Dump.class);
    // }

    /**
     * Retrieves a task with the specified uid
     *
     * @param uid Identifier of the requested Task
     * @return Task Instance
     * @throws MeilisearchException if an error occurs
     */
    public Task getTask(int uid) throws MeilisearchException {
        return this.tasksHandler.getTask(uid);
    }

    /**
     * Retrieves list of tasks
     *
     * @return List of tasks in the Meilisearch client
     * @throws MeilisearchException if an error occurs
     */
    public Result<Task> getTasks() throws MeilisearchException {
        return this.tasksHandler.getTasks();
    }

    /**
     * Waits for a task to be processed
     *
     * @param uid Identifier of the requested Task
     * @throws MeilisearchException if an error occurs or if timeout is reached
     */
    public void waitForTask(int uid) throws MeilisearchException {
        this.tasksHandler.waitForTask(uid);
    }

    /**
     * Retrieves the key with the specified uid
     *
     * @param uid Identifier of the requested Key
     * @return Key Instance
     * @throws MeilisearchException if an error occurs
     */
    public Key getKey(String uid) throws MeilisearchException {
        return this.keysHandler.getKey(uid);
    }

    /**
     * Retrieves list of keys
     *
     * @return List of keys in the Meilisearch client
     * @throws MeilisearchException if an error occurs
     */
    public Result<Key> getKeys() throws MeilisearchException {
        return this.keysHandler.getKeys();
    }

    /**
     * Creates a key
     *
     * @param options Key containing the options of the key
     * @return Key Instance
     * @throws MeilisearchException if an error occurs
     */
    public Key createKey(Key options) throws MeilisearchException {
        return this.keysHandler.createKey(options);
    }

    /**
     * Deletes a key
     *
     * @param key String containing the key
     * @throws MeilisearchException if an error occurs
     */
    public void deleteKey(String key) throws MeilisearchException {
        this.keysHandler.deleteKey(key);
    }

    public String generateTenantToken(Map<String, Object> searchRules) throws MeilisearchException {
        return this.generateTenantToken(searchRules, new TenantTokenOptions());
    }

    /**
     * Generate a tenant token
     *
     * @param searchRules A Map of string, object which contains the rules to be enforced at search
     *     time for all or specific accessible indexes for the signing API Key.
     * @param options A TenantTokenOptions, the following fileds are accepted: - apiKey: String
     *     containing the API key parent of the token. If you leave it empty the client API Key will
     *     be used. - expiresAt: A DateTime when the key will expire. Note that if an expiresAt
     *     value is included it should be in UTC time.
     * @return String containing the tenant token
     * @throws MeilisearchException if an error occurs
     */
    public String generateTenantToken(Map<String, Object> searchRules, TenantTokenOptions options)
            throws MeilisearchException {
        // Validate all fields
        Date now = new Date();
        String secret;

        if (options.getApiKey() == null || options.getApiKey() == "") {
            secret = this.config.apiKey;
        } else {
            secret = options.getApiKey();
        }
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        if (options.getExpiresAt() != null && now.after((Date) options.getExpiresAt())) {
            throw new MeilisearchException("The date expiresAt should be in the future.");
        }
        if (secret == null || secret == "" || secret.length() <= 8) {
            throw new MeilisearchException(
                    "An api key is required in the client or should be passed as an argument and this key cannot be the master key.");
        }
        if (searchRules == null) {
            throw new MeilisearchException(
                    "The searchRules field is mandatory and should be defined.");
        }

        // Encrypt the key
        Algorithm algorithm = Algorithm.HMAC256(secret);

        // Create JWT
        String jwtToken =
                JWT.create()
                        .withClaim("searchRules", searchRules)
                        .withClaim("apiKeyPrefix", secret.substring(0, 8))
                        .withExpiresAt(options.getExpiresAt())
                        .sign(algorithm);

        return jwtToken;
    }
}
