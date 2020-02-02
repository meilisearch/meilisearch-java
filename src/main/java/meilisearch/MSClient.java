/*
 * Unofficial Java client for MeiliSearch
 */
package meilisearch;

public class MSClient {
    public Config config;
    public Indexes indexes;

    /**
     * Call instance for MeiliSearch client
     *
     * @param config
     */
    public MSClient(Config config) {
        this.config = config;
        this.indexes = new Indexes(config);
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
        return this.indexes.create(name, params);
    }

    /**
     * Get all indexes
     * Refer https://docs.meilisearch.com/references/indexes.html#list-all-indexes
     *
     * @return
     * @throws Exception
     */
    public String getIndexes () throws Exception {
        return this.indexes.getAll();
    }

    /**
     * Get single index by uid
     * Refer https://docs.meilisearch.com/references/indexes.html#get-one-index
     *
     * @param uid
     * @return
     * @throws Exception
     */
    public String getIndex (String uid) throws Exception {
        return this.indexes.get(uid);
    }
}
