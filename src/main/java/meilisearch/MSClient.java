/*
 * Unofficial Java client for MeiliSearch
 */
package meilisearch;

import com.google.gson.Gson;

public class MSClient {
    public Config config;
    public Index index;
    public Gson gson;

    /**
     * Call instance for MeiliSearch client
     *
     * @param config
     */
    public MSClient(Config config) {
        this.config = config;
        gson = new Gson();
        this.index = new Index(config);
    }

    /**
     * Create index
     * Refer https://docs.meilisearch.com/references/indexes.html#create-an-index
     *
     * @param name
     * @param params
     * @return
     * @throws Exception
     */
    public String createIndex (String name, Schema params) throws Exception {
        return this.index.create(name, params);
    }

    /**
     * Get all indexes
     * Refer https://docs.meilisearch.com/references/indexes.html#list-all-indexes
     *
     * @return
     * @throws Exception
     */
    public Indexes[] getIndexList () throws Exception {
        Indexes[] indexList = gson.fromJson(this.index.getAll(), Indexes[].class);
        for (Indexes idxs: indexList) {
            idxs.setConfig(this.config);
        }
        return indexList;
    }

    /**
     * Get single index by uid
     * Refer https://docs.meilisearch.com/references/indexes.html#get-one-index
     *
     * @param uid
     * @return
     * @throws Exception
     */
    public Indexes getIndex (String uid) throws Exception {
        Indexes indexes = gson.fromJson(this.index.get(uid), Indexes.class);
        indexes.setConfig(this.config);
        return indexes;
    }

    /**
     * Update index by uid
     * Refer https://docs.meilisearch.com/references/indexes.html#update-an-index
     *
     * @param uid
     * @return
     * @throws Exception
     */
    public String updateIndex (String uid, String name) throws Exception {
        return this.index.update(uid, name);
    }

    /**
     * Delete single index by uid
     * Refer https://docs.meilisearch.com/references/indexes.html#get-one-index
     *
     * @param uid
     * @return
     * @throws Exception
     */
    public String deleteIndex (String uid) throws Exception {
        return this.index.delete(uid);
    }
}
