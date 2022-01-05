package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Dump;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;
import com.meilisearch.sdk.utils.Movie;
import java.util.Arrays;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
public class ClientTest extends AbstractIT {

    String primaryKey = "id";
    private TestData<Movie> testData;

    @BeforeEach
    public void initialize() {
        setUp();
        if (testData == null) testData = this.getTestData(MOVIES_INDEX, Movie.class);
    }

    @AfterAll
    static void cleanMeiliSearch() {
        cleanup();
    }

    /** Test Index creation without PrimaryKey */
    @Test
    public void testCreateIndexWithoutPrimaryKey() throws Exception {
        String indexUid = "CreateIndexWithoutPrimaryKey";
        Index index = client.createIndex(indexUid);
        assertEquals(index.getUid(), indexUid);
        assertNull(index.getPrimaryKey());
        client.deleteIndex(index.getUid());
    }

    /** Test Index creation with PrimaryKey */
    @Test
    public void testCreateIndexWithPrimaryKey() throws Exception {
        String indexUid = "CreateIndexWithPrimaryKey";
        Index index = client.createIndex(indexUid, this.primaryKey);
        assertEquals(index.getUid(), indexUid);
        assertEquals(index.getPrimaryKey(), this.primaryKey);
        client.deleteIndex(index.getUid());
    }

    /** Test Index creation error: already exists */
    @Test
    public void testCreateIndexAlreadyExists() throws Exception {
        String indexUid = "CreateIndexAlreadyExists";
        Index index = client.createIndex(indexUid, this.primaryKey);
        assertEquals(index.getUid(), indexUid);
        assertEquals(index.getPrimaryKey(), this.primaryKey);
        assertThrows(
                MeiliSearchApiException.class, () -> client.createIndex(indexUid, this.primaryKey));
        client.deleteIndex(index.getUid());
    }

    /** Test update Index PrimaryKey */
    @Test
    public void testUpdateIndexPrimaryKey() throws Exception {
        String indexUid = "UpdateIndexPrimaryKey";
        Index index = client.createIndex(indexUid);
        assertEquals(index.getUid(), indexUid);
        assertNull(index.getPrimaryKey());
        index = client.updateIndex(indexUid, this.primaryKey);
        assertTrue(index instanceof Index);
        assertEquals(index.getUid(), indexUid);
        assertEquals(index.getPrimaryKey(), this.primaryKey);
        client.deleteIndex(index.getUid());
    }

    /** Test getIndex */
    @Test
    public void testGetIndex() throws Exception {
        String indexUid = "GetIndex";
        Index index = client.createIndex(indexUid);
        Index getIndex = client.getIndex(indexUid);
        assertEquals(index.getUid(), getIndex.getUid());
        assertEquals(index.getPrimaryKey(), getIndex.getPrimaryKey());
        client.deleteIndex(index.getUid());
    }

    /** Test getRawIndex */
    @Test
    public void testGetRawIndex() throws Exception {
        String indexUid = "GetRawIndex";
        Index index = client.createIndex(indexUid, this.primaryKey);
        String getIndex = client.getRawIndex(indexUid);
        JsonObject indexJson = JsonParser.parseString(getIndex).getAsJsonObject();
        assertEquals(index.getUid(), indexJson.get("uid").getAsString());
        assertEquals(index.getPrimaryKey(), indexJson.get("primaryKey").getAsString());
        client.deleteIndex(index.getUid());
    }

    /** Test getIndexList */
    @Test
    public void testGetIndexList() throws Exception {
        String[] indexUids = {"GetIndexList", "GetIndexList2"};
        Index index1 = client.createIndex(indexUids[0]);
        Index index2 = client.createIndex(indexUids[1], this.primaryKey);
        Index[] indexes = client.getIndexList();
        assertEquals(2, indexes.length);
        assert (Arrays.asList(indexUids).contains(indexUids[0]));
        assert (Arrays.asList(indexUids).contains(indexUids[1]));
        client.deleteIndex(indexUids[0]);
        client.deleteIndex(indexUids[1]);
    }

    /** Test getRawIndexList */
    @Test
    public void testGetRawIndexList() throws Exception {
        String[] indexUids = {"GetRawIndexList", "GetRawIndexList2"};
        Index index1 = client.createIndex(indexUids[0]);
        Index index2 = client.createIndex(indexUids[1], this.primaryKey);
        String indexes = client.getRawIndexList();
        JsonArray jsonIndexArray = JsonParser.parseString(indexes).getAsJsonArray();
        assertEquals(jsonIndexArray.size(), 2);
        assert (Arrays.asList(indexUids)
                .contains(jsonIndexArray.get(0).getAsJsonObject().get("uid").getAsString()));
        assert (Arrays.asList(indexUids)
                .contains(jsonIndexArray.get(1).getAsJsonObject().get("uid").getAsString()));
        client.deleteIndex(indexUids[0]);
        client.deleteIndex(indexUids[1]);
    }

    /** Test deleteIndex */
    @Test
    public void testDeleteIndex() throws Exception {
        String indexUid = "DeleteIndex";
        Index index = client.createIndex(indexUid);
        client.deleteIndex(index.getUid());
        assertThrows(MeiliSearchApiException.class, () -> client.getIndex(indexUid));
    }

    /** Test call to index method with an inexistent index */
    @Test
    public void testIndexMethodCallInexistentIndex() throws Exception {
        String indexUid = "IndexMethodCallInexistentIndex";
        Index index = client.index(indexUid);
        assertEquals(indexUid, index.getUid());
        assertThrows(MeiliSearchApiException.class, () -> client.getIndex(indexUid));
    }

    /** Test call to index method with an existing index */
    @Test
    public void testIndexMethodCallExistingIndex() throws Exception {
        String indexUid = "IndexMethodCallExistingIndex";
        Index createdIndex = client.createIndex(indexUid);
        Index index = client.index(indexUid);
        assertEquals(createdIndex.getUid(), index.getUid());
        assertNull(index.getPrimaryKey());
    }

    /** Test call to index method with an existing index with primary key */
    @Test
    public void testIndexMethodCallExistingIndexWithPrimaryKey() throws Exception {
        String indexUid = "IndexMethodCallExistingIndexWithPrimaryKey";
        String primaryKey = "PrimaryKey";
        Index createdIndex = client.createIndex(indexUid, primaryKey);
        Index index = client.index(indexUid);
        assertEquals(createdIndex.getUid(), index.getUid());
        assertNull(index.getPrimaryKey());
        index.fetchPrimaryKey();
        assertEquals(primaryKey, index.getPrimaryKey());
    }

    /** Test call to create dump */
    @Test
    public void testCreateDump() throws Exception {
        Dump dump = client.createDump();
        String status = dump.getStatus();
        assertEquals(status, "in_progress");
    }

    /** Test call to get dump status by uid */
    @Test
    public void testGetDumpStatus() throws Exception {
        Dump dump = client.createDump();
        String uid = dump.getUid();
        String status = client.getDumpStatus(uid);
        assertNotNull(status);
        assertNotNull(uid);
    }
}
