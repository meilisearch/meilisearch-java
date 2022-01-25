/*
 * Unofficial Java client for MeiliSearch
 */
package com.meilisearch.sdk;

import com.google.gson.Gson;

/** MeiliSearch client */
public class Client {
    public Config config;
    public IndexesHandler indexesHandler;
    public TasksHandler tasksHandler;
    public KeysHandler keysHandler;
    public Gson gson;
    public DumpHandler dumpHandler;

    /**
     * Calls instance for MeiliSearch client
     *
     * @param config Configuration to connect to MeiliSearch instance
     */
    public Client(Config config) {
        this.config = config;
        this.gson = new Gson();
        this.indexesHandler = new IndexesHandler(config);
        this.tasksHandler = new TasksHandler(config);
        this.keysHandler = new KeysHandler(config);
        this.dumpHandler = new DumpHandler(config);
    }

    /**
     * Creates index Refer https://docs.meilisearch.com/reference/api/indexes.html#create-an-index
     *
     * @param uid Unique identifier for the index to create
     * @return MeiliSearch API response as Task
     * @throws Exception if an error occurs
     */
    public Task createIndex(String uid) throws Exception {
        return this.createIndex(uid, null);
    }

    /**
     * Creates index Refer https://docs.meilisearch.com/reference/api/indexes.html#create-an-index
     *
     * @param uid Unique identifier for the index to create
     * @param primaryKey The primary key of the documents in that index
     * @return MeiliSearch API response as Task
     * @throws Exception if an error occurs
     */
    public Task createIndex(String uid, String primaryKey) throws Exception {
        Task task = gson.fromJson(this.indexesHandler.create(uid, primaryKey), Task.class);
        return task;
    }

    /**
     * Gets all indexes Refer
     * https://docs.meilisearch.com/reference/api/indexes.html#list-all-indexes
     *
     * @return List of indexes in the MeiliSearch client
     * @throws Exception if an error occurs
     */
    public Index[] getIndexList() throws Exception {
        Index[] meiliSearchIndexList = gson.fromJson(getRawIndexList(), Index[].class);
        for (Index indexes : meiliSearchIndexList) {
            indexes.setConfig(this.config);
        }
        return meiliSearchIndexList;
    }

    /**
     * Gets all indexes https://docs.meilisearch.com/reference/api/indexes.html#list-all-indexes
     *
     * @return MeiliSearch API response as String
     * @throws Exception if an error occurs
     */
    public String getRawIndexList() throws Exception {
        return this.indexesHandler.getAll();
    }

    /**
     * Creates a local reference to an index identified by `uid`, without doing an HTTP call.
     * Calling this method doesn't create an index by itself, but grants access to all the other
     * methods in the Index class.
     *
     * @param uid Unique identifier of the index
     * @return Index instance
     * @throws Exception if an error occurs
     */
    public Index index(String uid) throws Exception {
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
     * @return MeiliSearch API response
     * @throws Exception if an error occurs
     */
    public Index getIndex(String uid) throws Exception {
        Index indexes = gson.fromJson(getRawIndex(uid), Index.class);
        indexes.setConfig(this.config);
        return indexes;
    }

    /**
     * Gets single index by uid Refer
     * https://docs.meilisearch.com/reference/api/indexes.html#get-one-index
     *
     * @param uid Unique identifier of the index to get
     * @return MeiliSearch API response as String
     * @throws Exception if an error occurs
     */
    public String getRawIndex(String uid) throws Exception {
        return this.indexesHandler.get(uid);
    }

    /**
     * Updates single index by uid Refer
     * https://docs.meilisearch.com/reference/api/indexes.html#update-an-index
     *
     * @param uid Unique identifier of the index to update
     * @param primaryKey Primary key of the documents in the index
     * @return MeiliSearch API response as Task
     * @throws Exception if an error occurs
     */
    public Task updateIndex(String uid, String primaryKey) throws Exception {
        Task task =
                gson.fromJson(this.indexesHandler.updatePrimaryKey(uid, primaryKey), Task.class);
        return task;
    }

    /**
     * Deletes single index by uid Refer
     * https://docs.meilisearch.com/reference/api/indexes.html#delete-one-index
     *
     * @param uid Unique identifier of the index to delete
     * @return MeiliSearch API response as Task
     * @throws Exception if an error occurs
     */
    public Task deleteIndex(String uid) throws Exception {
        Task task = gson.fromJson(this.indexesHandler.delete(uid), Task.class);
        return task;
    }

    /**
     * Triggers the creation of a MeiliSearch dump. Refer
     * https://docs.meilisearch.com/reference/api/dump.html#create-a-dump
     *
     * @return Dump instance
     * @throws Exception if an error occurs
     */
    public Dump createDump() throws Exception {
        return this.dumpHandler.createDump();
    }

    /**
     * Gets the status of a MeiliSearch dump.
     * https://docs.meilisearch.com/reference/api/dump.html#get-dump-status
     *
     * @param uid Unique identifier for correspondent dump
     * @return String with dump status
     * @throws Exception if an error occurs
     */
    public String getDumpStatus(String uid) throws Exception {
        return this.dumpHandler.getDumpStatus(uid);
    }

    /**
     * Retrieves a task with the specified uid
     *
     * @param uid Identifier of the requested Task
     * @return Task Instance
     * @throws Exception if an error occurs
     */
    public Task getTask(int uid) throws Exception {
        return this.tasksHandler.getTask(uid);
    }

    /**
     * Retrieves list of tasks
     *
     * @return List of tasks in the MeiliSearch client
     * @throws Exception if an error occurs
     */
    public Task[] getTasks() throws Exception {
        return this.tasksHandler.getTasks();
    }

    /**
     * Waits for a task to be processed
     *
     * @param uid Identifier of the requested Task
     * @throws Exception if an error occurs or if timeout is reached
     */
    public void waitForTask(int uid) throws Exception {
        this.tasksHandler.waitForTask(uid);
    }

    /**
     * Retrieves the key with the specified uid
     *
     * @param uid Identifier of the requested Key
     * @return Key Instance
     * @throws Exception if an error occurs
     */
    public Key getKey(String uid) throws Exception {
        return this.keysHandler.getKey(uid);
    }

    /**
     * Retrieves list of keys
     *
     * @return List of keys in the MeiliSearch client
     * @throws Exception if an error occurs
     */
    public Key[] getKeys() throws Exception {
        return this.keysHandler.getKeys();
    }

    /**
     * Creates a key
     *
     * @param options String containing the options of the key
     * @return Key Instance
     * @throws Exception if an error occurs
     */
    public Key createKey(String options) throws Exception {
        return this.keysHandler.createKey(options);
    }

    /**
     * Updates a key
     *
     * @param key String containing the key
     * @param options String containing the options to update
     * @return Key Instance
     * @throws Exception if client request causes an error
     */
    public Key updateKey(String key, String options) throws Exception {
        return this.keysHandler.updateKey(key, options);
    }

    /**
     * Deletes a key
     *
     * @param key String containing the key
     * @return MeiliSearch API response
     * @throws Exception if an error occurs
     */
    public String deleteKey(String key) throws Exception {
        return this.keysHandler.deleteKey(key);
    }
}
