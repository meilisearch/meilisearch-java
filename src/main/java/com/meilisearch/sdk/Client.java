/*
 * Official Java client for Meilisearch
 */
package com.meilisearch.sdk;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.json.JsonHandler;
import com.meilisearch.sdk.model.CancelTasksQuery;
import com.meilisearch.sdk.model.DeleteTasksQuery;
import com.meilisearch.sdk.model.IndexesQuery;
import com.meilisearch.sdk.model.Key;
import com.meilisearch.sdk.model.KeyUpdate;
import com.meilisearch.sdk.model.KeysQuery;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.Stats;
import com.meilisearch.sdk.model.SwapIndexesParams;
import com.meilisearch.sdk.model.Task;
import com.meilisearch.sdk.model.TaskInfo;
import com.meilisearch.sdk.model.TasksQuery;
import com.meilisearch.sdk.model.TasksResults;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

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
        return this.indexesHandler.createIndex(uid, primaryKey);
    }

    /**
     * Gets indexes https://docs.meilisearch.com/reference/api/indexes.html#list-all-indexes
     *
     * @return Results containing a list of indexes from the Meilisearch API
     * @throws MeilisearchException if an error occurs
     */
    public Results<Index> getIndexes() throws MeilisearchException {
        Results<Index> indexes = this.indexesHandler.getIndexes();
        for (Index index : indexes.getResults()) {
            index.setConfig(this.config);
        }
        return indexes;
    }

    /**
     * Gets indexes https://docs.meilisearch.com/reference/api/indexes.html#list-all-indexes
     *
     * @param params query parameters accepted by the get indexes route
     * @return Results containing a list of indexes from the Meilisearch API
     * @throws MeilisearchException if an error occurs
     */
    public Results<Index> getIndexes(IndexesQuery params) throws MeilisearchException {
        Results<Index> indexes = this.indexesHandler.getIndexes(params);
        for (Index index : indexes.getResults()) {
            index.setConfig(this.config);
        }
        return indexes;
    }

    /**
     * Gets all indexes https://docs.meilisearch.com/reference/api/indexes.html#list-all-indexes
     *
     * @return List of indexes from the Meilisearch API as String
     * @throws MeilisearchException if an error occurs
     */
    public String getRawIndexes() throws MeilisearchException {
        return this.indexesHandler.getRawIndexes();
    }

    /**
     * Gets all indexes https://docs.meilisearch.com/reference/api/indexes.html#list-all-indexes
     *
     * @param params query parameters accepted by the get indexes route
     * @return List of indexes from the Meilisearch API as String
     * @throws MeilisearchException if an error occurs
     */
    public String getRawIndexes(IndexesQuery params) throws MeilisearchException {
        return this.indexesHandler.getRawIndexes(params);
    }

    /**
     * Creates a local reference to an index identified by `uid`, without doing an HTTP call.
     * Calling this method doesn't create an index by itself, but grants access to all the other
     * methods in the Index class.
     *
     * @param uid Unique identifier of the index
     * @return Meilisearch API response as Index instance
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
     * @return Meilisearch API response as Index instance
     * @throws MeilisearchException if an error occurs
     */
    public Index getIndex(String uid) throws MeilisearchException {
        Index index = this.indexesHandler.getIndex(uid);
        index.setConfig(this.config);
        return index;
    }

    /**
     * Updates the primary key of an index
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
        return this.indexesHandler.deleteIndex(uid);
    }

    /**
     * Swap the documents, settings, and task history of two or more indexes
     * https://docs.meilisearch.com/reference/api/indexes.html#swap-indexes
     *
     * @param param accepted by the swap-indexes route
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo swapIndexes(SwapIndexesParams[] param) throws MeilisearchException {
        return config.httpClient.post("/swap-indexes", param, TaskInfo.class);
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
     * @return Meilisearch API response as Task Instance
     * @throws MeilisearchException if an error occurs
     */
    public Task getTask(int uid) throws MeilisearchException {
        return this.tasksHandler.getTask(uid);
    }

    /**
     * Retrieves list of tasks https://docs.meilisearch.com/reference/api/tasks.html#get-tasks
     *
     * @return TasksResults containing a list of tasks from the Meilisearch API
     * @throws MeilisearchException if an error occurs
     */
    public TasksResults getTasks() throws MeilisearchException {
        return this.tasksHandler.getTasks();
    }

    /**
     * Retrieves list of tasks https://docs.meilisearch.com/reference/api/tasks.html#get-tasks
     *
     * @param param accept by the tasks route
     * @return TasksResults containing a list of tasks from the Meilisearch API
     * @throws MeilisearchException if an error occurs
     */
    public TasksResults getTasks(TasksQuery param) throws MeilisearchException {
        return this.tasksHandler.getTasks(param);
    }

    /**
     * Cancel any number of enqueued or processing tasks
     * https://docs.meilisearch.com/reference/api/tasks.html#cancel-tasks
     *
     * @param param accept by the tasks route
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo cancelTasks(CancelTasksQuery param) throws MeilisearchException {
        return this.tasksHandler.cancelTasks(param);
    }

    /**
     * Delete a finished (succeeded, failed, or canceled) task
     * https://docs.meilisearch.com/reference/api/tasks.html#delete-tasks
     *
     * @param param accept by the tasks route
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo deleteTasks(DeleteTasksQuery param) throws MeilisearchException {
        return this.tasksHandler.deleteTasks(param);
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
     * @return Meilisearch API response as Key Instance
     * @throws MeilisearchException if an error occurs
     */
    public Key getKey(String uid) throws MeilisearchException {
        return this.keysHandler.getKey(uid);
    }

    /**
     * Retrieves list of keys https://docs.meilisearch.com/reference/api/keys.html#get-all-keys
     *
     * @return Results containing a list of Key from the Meilisearch API
     * @throws MeilisearchException if an error occurs
     */
    public Results<Key> getKeys() throws MeilisearchException {
        return this.keysHandler.getKeys();
    }

    /**
     * Get list of all API keys https://docs.meilisearch.com/reference/api/keys.html#get-all-keys
     *
     * @param params query parameters accepted by the get keys route
     * @return Results containing a list of Key from the Meilisearch API
     * @throws MeilisearchException if an error occurs
     */
    public Results<Key> getKeys(KeysQuery params) throws MeilisearchException {
        return this.keysHandler.getKeys(params);
    }

    /**
     * Creates a key https://docs.meilisearch.com/reference/api/keys.html#create-a-key
     *
     * @param options Key containing the options of the key
     * @return Meilisearch API response as Key Instance
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
     * @return Meilisearch API response as Key Instance
     * @throws MeilisearchException if an error occurs
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

    public String generateTenantToken(String apiKeyUid, Map<String, Object> searchRules)
            throws MeilisearchException {
        return this.generateTenantToken(apiKeyUid, searchRules, new TenantTokenOptions());
    }

    /**
     * Generate a tenant token
     * https://docs.meilisearch.com/learn/security/tenant_tokens.html#multitenancy-and-tenant-tokens
     *
     * @param apiKeyUid Uid of a signing API key.
     * @param searchRules A Map of string, object which contains the rules to be enforced at search
     *     time for all or specific accessible indexes for the signing API Key.
     * @param options A TenantTokenOptions, the following fileds are accepted: - apiKey: String
     *     containing the API key parent of the token. If you leave it empty the client API Key will
     *     be used. - expiresAt: A DateTime when the key will expire. Note that if an expiresAt
     *     value is included it should be in UTC time.
     * @return String containing the tenant token
     * @throws MeilisearchException if an error occurs
     */
    public String generateTenantToken(
            String apiKeyUid, Map<String, Object> searchRules, TenantTokenOptions options)
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
        if (apiKeyUid == "" || apiKeyUid == null || !isValidUUID(apiKeyUid)) {
            throw new MeilisearchException(
                    "The uid used for the token generation must exist and comply to uuid4 format");
        }

        // Encrypt the key
        Algorithm algorithm = Algorithm.HMAC256(secret);

        // Create JWT
        String jwtToken =
                JWT.create()
                        .withClaim("searchRules", searchRules)
                        .withClaim("apiKeyUid", apiKeyUid)
                        .withExpiresAt(options.getExpiresAt())
                        .sign(algorithm);

        return jwtToken;
    }

    private Boolean isValidUUID(String apiKeyUid) {
        try {
            UUID uuid = UUID.fromString(apiKeyUid);
        } catch (IllegalArgumentException exception) {
            return false;
        }
        return true;
    }
}
