/*
 * Official Java client for Meilisearch
 */
package com.meilisearch.sdk;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.json.JsonHandler;
import com.meilisearch.sdk.model.*;
import com.meilisearch.sdk.model.batch.req.BatchesQuery;
import com.meilisearch.sdk.model.batch.res.Batch;
import java.util.*;

/** Meilisearch client */
public class Client {
    private Config config;
    private IndexesHandler indexesHandler;
    private InstanceHandler instanceHandler;
    private TasksHandler tasksHandler;
    private KeysHandler keysHandler;
    private JsonHandler jsonHandler;
    private NetworkHandler networkHandler;

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
        this.networkHandler = new NetworkHandler(config);
    }

    /**
     * Creates an index with a unique identifier
     *
     * @param uid Unique identifier for the index to create
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/indexes#create-an-index">API
     * specification</a>
     */
    public TaskInfo createIndex(String uid) throws MeilisearchException {
        return this.createIndex(uid, null);
    }

    /**
     * Creates an index with a unique identifier
     *
     * @param uid Unique identifier for the index to create
     * @param primaryKey The primary key of the documents in that index
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/indexes#create-an-index">API
     *     specification</a>
     */
    public TaskInfo createIndex(String uid, String primaryKey) throws MeilisearchException {
        return this.indexesHandler.createIndex(uid, primaryKey);
    }

    /**
     * Gets indexes
     *
     * @return Results containing a list of indexes from the Meilisearch API
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/indexes#list-all-indexes">API
     *     specification</a>
     */
    public Results<Index> getIndexes() throws MeilisearchException {
        Results<Index> indexes = this.indexesHandler.getIndexes();
        for (Index index : indexes.getResults()) {
            index.setConfig(this.config);
        }
        return indexes;
    }

    /**
     * Gets indexes
     *
     * @param params query parameters accepted by the get indexes route
     * @return Results containing a list of indexes from the Meilisearch API
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/indexes#list-all-indexes">API
     *     specification</a>
     */
    public Results<Index> getIndexes(IndexesQuery params) throws MeilisearchException {
        Results<Index> indexes = this.indexesHandler.getIndexes(params);
        for (Index index : indexes.getResults()) {
            index.setConfig(this.config);
        }
        return indexes;
    }

    /**
     * Gets all indexes
     *
     * @return List of indexes from the Meilisearch API as String
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/indexes#list-all-indexes">API
     *     specification</a>
     */
    public String getRawIndexes() throws MeilisearchException {
        return this.indexesHandler.getRawIndexes();
    }

    /**
     * Gets all indexes
     *
     * @param params query parameters accepted by the get indexes route
     * @return List of indexes from the Meilisearch API as String
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/indexes#list-all-indexes">API
     *     specification</a>
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
     *
     * @param uid Unique identifier of the index to get
     * @return Meilisearch API response as Index instance
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/indexes#get-one-index">API
     *     specification</a>
     */
    public Index getIndex(String uid) throws MeilisearchException {
        Index index = this.indexesHandler.getIndex(uid);
        index.setConfig(this.config);
        return index;
    }

    /**
     * Updates the primary key of an index
     *
     * @param uid Unique identifier of the index to update
     * @param primaryKey Primary key of the documents in the index
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/indexes#update-an-index">API
     *     specification</a>
     */
    public TaskInfo updateIndex(String uid, String primaryKey) throws MeilisearchException {
        return this.indexesHandler.updatePrimaryKey(uid, primaryKey);
    }

    /** Update an index: either update primary key or rename the index by passing indexUid. */
    public TaskInfo updateIndex(String uid, String primaryKey, String indexUid)
            throws MeilisearchException {
        if (indexUid != null) {
            return this.indexesHandler.updateIndexUid(uid, indexUid);
        }
        return this.indexesHandler.updatePrimaryKey(uid, primaryKey);
    }

    /**
     * Deletes single index by its unique identifier
     *
     * @param uid Unique identifier of the index to delete
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/indexes#delete-one-index">API
     *     specification</a>
     */
    public TaskInfo deleteIndex(String uid) throws MeilisearchException {
        return this.indexesHandler.deleteIndex(uid);
    }

    /**
     * Swap the documents, settings, and task history of two or more indexes
     *
     * @param param accepted by the swap-indexes route
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/indexes#swap-indexes">API
     *     specification</a>
     */
    public TaskInfo swapIndexes(SwapIndexesParams[] param) throws MeilisearchException {
        return config.httpClient.post("/swap-indexes", param, TaskInfo.class);
    }

    /**
     * Triggers the creation of a Meilisearch dump.
     *
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/dump#create-a-dump">API
     *     specification</a>
     */
    public TaskInfo createDump() throws MeilisearchException {
        return config.httpClient.post("/dumps", "", TaskInfo.class);
    }

    /**
     * Triggers the creation of a Meilisearch snapshot.
     *
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/snapshots">API specification</a>
     */
    public TaskInfo createSnapshot() throws MeilisearchException {
        return config.httpClient.post("/snapshots", "", TaskInfo.class);
    }

    /**
     * Triggers the export of documents between Meilisearch instances.
     *
     * @param request Export request parameters
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/export">API specification</a>
     */
    public TaskInfo export(ExportRequest request) throws MeilisearchException {
        return config.httpClient.post("/export", request, TaskInfo.class);
    }

    /**
     * Gets the status and availability of a Meilisearch instance
     *
     * @return String containing the status of the Meilisearch instance from Meilisearch API
     *     response
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/health#health">API
     *     specification</a>
     */
    public String health() throws MeilisearchException {
        return this.instanceHandler.health();
    }

    /**
     * Gets the status and availability of a Meilisearch instance
     *
     * @return True if the Meilisearch instance is available or false if it is not
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/health#health">API
     *     specification</a>
     */
    public Boolean isHealthy() throws MeilisearchException {
        return this.instanceHandler.isHealthy();
    }

    /**
     * Gets extended information and metrics about indexes and the Meilisearch database
     *
     * @return Stats instance from Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/stats#stats-object">API
     *     specification</a>
     */
    public Stats getStats() throws MeilisearchException {
        return this.instanceHandler.getStats();
    }

    /**
     * Gets the version of Meilisearch instance
     *
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/version#version">API
     *     specification</a>
     */
    public String getVersion() throws MeilisearchException {
        return this.instanceHandler.getVersion();
    }

    /**
     * Retrieves a task with the specified uid
     *
     * @param uid Identifier of the requested Task
     * @return Meilisearch API response as Task Instance
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/tasks#get-one-task">API
     *     specification</a>
     */
    public Task getTask(int uid) throws MeilisearchException {
        return this.tasksHandler.getTask(uid);
    }

    /**
     * Retrieves list of tasks
     *
     * @return TasksResults containing a list of tasks from the Meilisearch API
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/tasks#get-tasks">API
     *     specification</a>
     */
    public TasksResults getTasks() throws MeilisearchException {
        return this.tasksHandler.getTasks();
    }

    /**
     * Retrieves list of tasks
     *
     * @param param accept by the tasks route
     * @return TasksResults containing a list of tasks from the Meilisearch API
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/tasks#get-tasks">API
     *     specification</a>
     */
    public TasksResults getTasks(TasksQuery param) throws MeilisearchException {
        return this.tasksHandler.getTasks(param);
    }

    /**
     * Cancel any number of enqueued or processing tasks
     *
     * @param param accept by the tasks route
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/tasks#cancel-tasks">API
     *     specification</a>
     */
    public TaskInfo cancelTasks(CancelTasksQuery param) throws MeilisearchException {
        return this.tasksHandler.cancelTasks(param);
    }

    /**
     * Delete a finished (succeeded, failed, or canceled) task
     *
     * @param param accept by the tasks route
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/tasks#delete-tasks">API
     *     specification</a>
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
     * Retrieves a batch by its unique identifier, with exception handling.
     *
     * @param uid The unique identifier of the batch.
     * @return The Batch object corresponding to the given uid.
     * @throws MeilisearchException If an error occurs during the request.
     */
    public Batch getBatch(int uid) throws MeilisearchException {
        return this.tasksHandler.getBatch(uid);
    }

    /**
     * Retrieves all batches based on the provided query parameters, with exception handling.
     *
     * @param batchesQuery An instance of BatchesQuery containing filtering criteria.
     * @return A CursorResults object containing a list of Batch objects.
     * @throws MeilisearchException If an error occurs during the request.
     */
    public CursorResults<Batch> getAllBatches(BatchesQuery batchesQuery)
            throws MeilisearchException {
        return this.tasksHandler.getAllBatches(batchesQuery);
    }

    /**
     * Retrieves the key with the specified uid
     *
     * @param uid Identifier of the requested Key
     * @return Meilisearch API response as Key Instance
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/keys#get-one-key">API
     *     specification</a>
     */
    public Key getKey(String uid) throws MeilisearchException {
        return this.keysHandler.getKey(uid);
    }

    /**
     * Retrieves list of keys
     *
     * @return Results containing a list of Key from the Meilisearch API
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/keys#get-all-keys">API
     *     specification</a>
     */
    public Results<Key> getKeys() throws MeilisearchException {
        return this.keysHandler.getKeys();
    }

    /**
     * Get list of all API keys
     *
     * @param params query parameters accepted by the get keys route
     * @return Results containing a list of Key from the Meilisearch API
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/keys#get-all-keys">API
     *     specification</a>
     */
    public Results<Key> getKeys(KeysQuery params) throws MeilisearchException {
        return this.keysHandler.getKeys(params);
    }

    /**
     * Creates a key
     *
     * @param options Key containing the options of the key
     * @return Meilisearch API response as Key Instance
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/keys#create-a-key">API
     *     specification</a>
     */
    public Key createKey(Key options) throws MeilisearchException {
        return this.keysHandler.createKey(options);
    }

    /**
     * Updates a key
     *
     * @param key String containing the key
     * @param options String containing the options to update
     * @return Meilisearch API response as Key Instance
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/keys#update-a-key">API
     *     specification</a>
     */
    public Key updateKey(String key, KeyUpdate options) throws MeilisearchException {
        return this.keysHandler.updateKey(key, options);
    }

    /**
     * Deletes a key
     *
     * @param key String containing the key
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/keys#delete-a-key">API
     *     specification</a>
     */
    public void deleteKey(String key) throws MeilisearchException {
        this.keysHandler.deleteKey(key);
    }

    /*
     * Method overloading the multi search method to add federation parameter
     */
    public MultiSearchResult multiSearch(
            MultiSearchRequest search, MultiSearchFederation federation)
            throws MeilisearchException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("queries", search.getQueries());
        payload.put("federation", federation);
        return this.config.httpClient.post("/multi-search", payload, MultiSearchResult.class);
    }

    public Results<MultiSearchResult> multiSearch(MultiSearchRequest search)
            throws MeilisearchException {
        return this.config.httpClient.post(
                "/multi-search", search, Results.class, MultiSearchResult.class);
    }

    public void experimentalFeatures(Map<String, Boolean> features) {
        this.config.httpClient.patch("/experimental-features", features, Void.class);
    }

    public String generateTenantToken(String apiKeyUid, Map<String, Object> searchRules)
            throws MeilisearchException {
        return this.generateTenantToken(apiKeyUid, searchRules, new TenantTokenOptions());
    }

    /**
     * Generate a tenant token
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
     * @see <a
     *     href="https://www.meilisearch.com/docs/learn/security/tenant_tokens#multitenancy-and-tenant-tokens">Meilisearch
     *     Tenant Tokens</a>
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

    /**
     * Returns the current value of the instance’s network object.
     *
     * @return Network object
     */
    public Network getNetwork() {
        return this.networkHandler.getNetworkState();
    }

    /**
     * Update the fields of the network object. Updates to the network object are partial. Only provide the fields you intend to update.
     * Fields that are null in the payload will remain unchanged. To reset self and remotes to their original value, set them to null.
     * To remove a single remote from your network, set the value of its name to null.
     *
     * @param updatedNetwork Updated network configs
     * @return Updated Network
     */
    public Network updateNetwork(UpdateNetwork updatedNetwork) {
        return this.networkHandler.updateNetwork(updatedNetwork);
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
