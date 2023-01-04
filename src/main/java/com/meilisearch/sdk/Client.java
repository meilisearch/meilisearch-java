/*
 * Official Java client for Meilisearch
 */
package com.meilisearch.sdk;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.json.JsonHandler;
import com.meilisearch.sdk.model.Key;
import com.meilisearch.sdk.model.KeyUpdate;
import com.meilisearch.sdk.model.KeysQuery;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.Stats;
import com.meilisearch.sdk.model.Task;
import com.meilisearch.sdk.model.TaskInfo;
import com.meilisearch.sdk.model.TasksQuery;
import com.meilisearch.sdk.model.TasksResults;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/** Meilisearch client */
public class Client {
    private Config config;
    private IndexesHandler indexesHandler;
    private InstanceHandler instanceHandler;
    private TasksHandler tasksHandler;
    private KeysHandler keysHandler;
    private JsonHandler jsonHandler;

    /**
     * Calls instance for Meilisearch client
     *
     * @param config Configuration to connect to Meilisearch instance
     */
    public Client(Config config) {
        this.config = config;
        this.indexesHandler = new IndexesHandler(config);
        this.instanceHandler = new InstanceHandler(config);
        this.tasksHandler = new TasksHandler(config);
        this.keysHandler = new KeysHandler(config);
        this.jsonHandler = config.jsonHandler;
    }

    /**
     * Creates an index with a unique identifier
     * https://docs.meilisearch.com/reference/api/indexes.html#create-an-index
     *
     * @param uid Unique identifier for the index to create
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo createIndex(String uid) throws MeilisearchException {
        return this.createIndex(uid, null);
    }

    /**
     * Creates an index with a unique identifier
     * https://docs.meilisearch.com/reference/api/indexes.html#create-an-index
     *
     * @param uid Unique identifier for the index to create
     * @param primaryKey The primary key of the documents in that index
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo createIndex(String uid, String primaryKey) throws MeilisearchException {
        return this.indexesHandler.create(uid, primaryKey);
    }

    /**
     * Gets all indexes in the current Meilisearch instance
     * https://docs.meilisearch.com/reference/api/indexes.html#list-all-indexes
     *
     * @return Array of indexes in the Meilisearch client
     * @throws MeilisearchException if an error occurs
     */
    public Index[] getIndexes() throws MeilisearchException {
        Index[] indexes = jsonHandler.decode(getRawIndexes(), Index[].class);
        for (Index index : indexes) {
            index.setConfig(this.config);
        }
        return indexes;
    }

    /**
     * Gets all indexes in the current Meilisearch instance
     * https://docs.meilisearch.com/reference/api/indexes.html#list-all-indexes
     *
     * @return Meilisearch API response as String
     * @throws MeilisearchException if an error occurs
     */
    public String getRawIndexes() throws MeilisearchException {
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
     * Gets single index by its unique identifier
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
     * Gets single index by its unique identifier
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
     * Updates the primary key of an index in the Meilisearch instance
     * https://docs.meilisearch.com/reference/api/indexes.html#update-an-index
     *
     * @param uid Unique identifier of the index to update
     * @param primaryKey Primary key of the documents in the index
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo updateIndex(String uid, String primaryKey) throws MeilisearchException {
        return this.indexesHandler.updatePrimaryKey(uid, primaryKey);
    }

    /**
     * Deletes single index by its unique identifier
     * https://docs.meilisearch.com/reference/api/indexes.html#delete-one-index
     *
     * @param uid Unique identifier of the index to delete
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo deleteIndex(String uid) throws MeilisearchException {
        return this.indexesHandler.delete(uid);
    }

    /**
     * Triggers the creation of a Meilisearch dump.
     * https://docs.meilisearch.com/reference/api/dump.html#create-a-dump
     *
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo createDump() throws MeilisearchException {
        return config.httpClient.post("/dumps", "", TaskInfo.class);
    }

    /**
     * Gets the status and availability of a Meilisearch instance
     * https://docs.meilisearch.com/reference/api/health.html#health
     *
     * @return String containing the status of the Meilisearch instance from Meilisearch API
     *     response
     * @throws MeilisearchException if an error occurs
     */
    public String health() throws MeilisearchException {
        return this.instanceHandler.health();
    }

    /**
     * Gets the status and availability of a Meilisearch instance
     * https://docs.meilisearch.com/reference/api/health.html#health
     *
     * @return True if the Meilisearch instance is available or false if it is not
     * @throws MeilisearchException if an error occurs
     */
    public Boolean isHealthy() throws MeilisearchException {
        return this.instanceHandler.isHealthy();
    }

    /**
     * Gets extended information and metrics about indexes and the Meilisearch database
     * https://docs.meilisearch.com/reference/api/stats.html#stats-object
     *
     * @return Stats instance from Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public Stats getStats() throws MeilisearchException {
        return this.instanceHandler.getStats();
    }

    /**
     * Gets the version of Meilisearch instance
     * https://docs.meilisearch.com/reference/api/version.html#version
     *
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public String getVersion() throws MeilisearchException {
        return this.instanceHandler.getVersion();
    }

    /**
     * Retrieves a task with the specified uid
     * https://docs.meilisearch.com/reference/api/tasks.html#get-one-task
     *
     * @param uid Identifier of the requested Task
     * @return Task Instance
     * @throws MeilisearchException if an error occurs
     */
    public Task getTask(int uid) throws MeilisearchException {
        return this.tasksHandler.getTask(uid);
    }

    /**
     * Retrieves list of tasks https://docs.meilisearch.com/reference/api/tasks.html#get-tasks
     *
     * @return List of tasks in the Meilisearch client
     * @throws MeilisearchException if an error occurs
     */
    public TasksResults getTasks() throws MeilisearchException {
        return this.tasksHandler.getTasks();
    }

    /**
     * Retrieves list of tasks https://docs.meilisearch.com/reference/api/tasks.html#get-tasks
     *
     * @param param accept by the tasks route
     * @return List of tasks in the Meilisearch client
     * @throws MeilisearchException if an error occurs
     */
    public TasksResults getTasks(TasksQuery param) throws MeilisearchException {
        return this.tasksHandler.getTasks(param);
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
     * https://docs.meilisearch.com/reference/api/keys.html#get-one-key
     *
     * @param uid Identifier of the requested Key
     * @return Key Instance
     * @throws MeilisearchException if an error occurs
     */
    public Key getKey(String uid) throws MeilisearchException {
        return this.keysHandler.getKey(uid);
    }

    /**
     * Retrieves list of keys https://docs.meilisearch.com/reference/api/keys.html#get-all-keys
     *
     * @return List of keys in the Meilisearch client
     * @throws MeilisearchException if an error occurs
     */
    public Results<Key> getKeys() throws MeilisearchException {
        return this.keysHandler.getKeys();
    }

    /**
     * Get list of all API keys https://docs.meilisearch.com/reference/api/keys.html#get-all-keys
     *
     * @param param accept by the keys route
     * @return List of keys in the Meilisearch client
     * @throws MeilisearchException if an error occurs
     */
    public Results<Key> getKeys(KeysQuery param) throws MeilisearchException {
        return this.keysHandler.getKeys(param);
    }

    /**
     * Creates a key https://docs.meilisearch.com/reference/api/keys.html#create-a-key
     *
     * @param options Key containing the options of the key
     * @return Key Instance
     * @throws MeilisearchException if an error occurs
     */
    public Key createKey(Key options) throws MeilisearchException {
        return this.keysHandler.createKey(options);
    }

    /**
     * Updates a key https://docs.meilisearch.com/reference/api/keys.html#update-a-key
     *
     * @param key String containing the key
     * @param options String containing the options to update
     * @return Key Instance
     * @throws MeilisearchException if client request causes an error
     */
    public Key updateKey(String key, KeyUpdate options) throws MeilisearchException {
        return this.keysHandler.updateKey(key, options);
    }

    /**
     * Deletes a key https://docs.meilisearch.com/reference/api/keys.html#delete-a-key
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
     * https://docs.meilisearch.com/learn/security/tenant_tokens.html#multitenancy-and-tenant-tokens
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
