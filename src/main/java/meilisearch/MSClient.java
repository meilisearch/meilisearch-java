/*
 * Unofficial Java client for MeiliSearch
 */
package meilisearch;

import com.google.gson.Gson;
import meilisearch.model.Index;

public class MSClient {
    public Config config;
    public Indexes indexes;
    public Documents documents;
    public Gson gson;

    /**
     * Call instance for MeiliSearch client
     *
     * @param config
     */
    public MSClient(Config config) {
        this.config = config;
        gson = new Gson();
        this.indexes = new Indexes(config);
        this.documents = new Documents(config);
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
    public Index[] getIndexes () throws Exception {
        Index[] indexList = gson.fromJson(this.indexes.getAll(), Index[].class);
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
    public Index getIndex (String uid) throws Exception {
        Index index = gson.fromJson(this.indexes.get(uid), Index.class);
        return index;
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
        return this.indexes.update(uid, name);
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
        return this.indexes.delete(uid);
    }

    /**
     *
     * @param uid
     * @return
     * @throws Exception
     */
    public String getDocuments (String uid) throws Exception {
        return this.documents.getDocuments(uid);
    }

    /**
     *
     * @param uid
     * @param identifier
     * @return
     * @throws Exception
     */
    public String getDocument (String uid, String identifier) throws Exception {
        return this.documents.getDocument(uid, identifier);
    }
}
