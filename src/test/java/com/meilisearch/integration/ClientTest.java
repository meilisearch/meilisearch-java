package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.exceptions.MeilisearchApiException;
import com.meilisearch.sdk.model.TaskInfo;
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
        setUpJacksonClient();
        if (testData == null) testData = this.getTestData(MOVIES_INDEX, Movie.class);
    }

    @AfterAll
    static void cleanMeilisearch() {
        cleanup();
    }

    /** Test Index creation without PrimaryKey */
    @Test
    public void testCreateIndexWithoutPrimaryKey() throws Exception {
        String indexUid = "CreateIndexWithoutPrimaryKey";
        Index index = createEmptyIndex(indexUid);

        assertEquals(index.getUid(), indexUid);
        assertNull(index.getPrimaryKey());

        client.deleteIndex(index.getUid());
    }

    /** Test Index creation without PrimaryKey with Jackson Json Handler */
    @Test
    public void testCreateIndexWithoutPrimaryKeyWithJacksonJsonHandler() throws Exception {
        String indexUid = "CreateIndexWithoutPrimaryKeyWithJacksonJsonHandler";
        TaskInfo task = clientJackson.createIndex(indexUid);
        clientJackson.waitForTask(task.getTaskUid());
        Index index = clientJackson.getIndex(indexUid);

        assertEquals(index.getUid(), indexUid);
        assertNull(index.getPrimaryKey());

        clientJackson.deleteIndex(index.getUid());
    }

    /** Test Index creation with PrimaryKey */
    @Test
    public void testCreateIndexWithPrimaryKey() throws Exception {
        String indexUid = "CreateIndexWithPrimaryKey";
        Index index = createEmptyIndex(indexUid, this.primaryKey);

        assertEquals(index.getUid(), indexUid);
        assertEquals(index.getPrimaryKey(), this.primaryKey);

        client.deleteIndex(index.getUid());
    }

    /** Test Index creation with PrimaryKey with Jackson Json Handler */
    @Test
    public void testCreateIndexWithPrimaryKeyWithJacksonJsonHandler() throws Exception {
        String indexUid = "CreateIndexWithPrimaryKeyWithJacksonJsonHandler";
        TaskInfo task = clientJackson.createIndex(indexUid, this.primaryKey);
        clientJackson.waitForTask(task.getTaskUid());
        Index index = clientJackson.getIndex(indexUid);

        assertEquals(index.getUid(), indexUid);
        assertEquals(index.getPrimaryKey(), this.primaryKey);

        clientJackson.deleteIndex(index.getUid());
    }

    /** Test Index creation twice doesn't throw an error: already exists */
    @Test
    public void testCreateIndexAlreadyExists() throws Exception {
        String indexUid = "CreateIndexAlreadyExists";
        Index index = createEmptyIndex(indexUid, this.primaryKey);

        assertEquals(index.getUid(), indexUid);
        assertEquals(index.getPrimaryKey(), this.primaryKey);

        Index indexDuplicate = createEmptyIndex(indexUid, this.primaryKey);

        assertEquals(index.getUid(), indexUid);
        assertEquals(indexDuplicate.getUid(), indexUid);
        assertEquals(index.getPrimaryKey(), this.primaryKey);
        assertEquals(indexDuplicate.getPrimaryKey(), this.primaryKey);

        client.deleteIndex(index.getUid());
    }

    /** Test update Index PrimaryKey */
    @Test
    public void testUpdateIndexPrimaryKey() throws Exception {
        String indexUid = "UpdateIndexPrimaryKey";
        Index index = createEmptyIndex(indexUid);

        assertEquals(index.getUid(), indexUid);
        assertNull(index.getPrimaryKey());

        TaskInfo task = client.updateIndex(indexUid, this.primaryKey);
        client.waitForTask(task.getTaskUid());
        index = client.getIndex(indexUid);

        assertTrue(index instanceof Index);
        assertEquals(index.getUid(), indexUid);
        assertEquals(index.getPrimaryKey(), this.primaryKey);

        client.deleteIndex(index.getUid());
    }

    /** Test getIndex */
    @Test
    public void testGetIndex() throws Exception {
        String indexUid = "GetIndex";
        Index index = createEmptyIndex(indexUid);
        Index getIndex = client.getIndex(indexUid);

        assertEquals(index.getUid(), getIndex.getUid());
        assertEquals(index.getPrimaryKey(), getIndex.getPrimaryKey());

        client.deleteIndex(index.getUid());
    }

    /** Test getRawIndex */
    @Test
    public void testGetRawIndex() throws Exception {
        String indexUid = "GetRawIndex";
        Index index = createEmptyIndex(indexUid, this.primaryKey);
        String getIndex = client.getRawIndex(indexUid);
        JsonObject indexJson = JsonParser.parseString(getIndex).getAsJsonObject();

        assertEquals(index.getUid(), indexJson.get("uid").getAsString());
        assertEquals(index.getPrimaryKey(), indexJson.get("primaryKey").getAsString());

        client.deleteIndex(index.getUid());
    }

    /** Test getIndexes */
    @Test
    public void testGetIndexes() throws Exception {
        String[] indexUids = {"GetIndexes", "GetIndexes2"};
        createEmptyIndex(indexUids[0]);
        createEmptyIndex(indexUids[1], this.primaryKey);
        Index[] indexes = client.getIndexes();

        assertEquals(2, indexes.length);
        assert (Arrays.asList(indexUids).contains(indexUids[0]));
        assert (Arrays.asList(indexUids).contains(indexUids[1]));

        client.deleteIndex(indexUids[0]);
        client.deleteIndex(indexUids[1]);
    }

    /** Test getRawIndexes */
    @Test
    public void testGetRawIndexes() throws Exception {
        String[] indexUids = {"GetRawIndexes", "GetRawIndexes2"};
        createEmptyIndex(indexUids[0]);
        createEmptyIndex(indexUids[1], this.primaryKey);

        String indexes = client.getRawIndexes();
        JsonArray jsonIndexArray = JsonParser.parseString(indexes).getAsJsonArray();

        assertEquals(4, jsonIndexArray.size());
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
        Index index = createEmptyIndex(indexUid);
        TaskInfo task = client.deleteIndex(index.getUid());
        client.waitForTask(task.getTaskUid());

        assertThrows(MeilisearchApiException.class, () -> client.getIndex(indexUid));
    }

    /** Test call to index method with an inexistent index */
    @Test
    public void testIndexMethodCallInexistentIndex() throws Exception {
        String indexUid = "IndexMethodCallInexistentIndex";
        Index index = client.index(indexUid);

        assertEquals(indexUid, index.getUid());
        assertThrows(MeilisearchApiException.class, () -> client.getIndex(indexUid));
    }

    /** Test call to index method with an existing index */
    @Test
    public void testIndexMethodCallExistingIndex() throws Exception {
        String indexUid = "IndexMethodCallExistingIndex";
        Index createdIndex = createEmptyIndex(indexUid);
        Index index = client.index(indexUid);

        assertEquals(createdIndex.getUid(), index.getUid());
        assertNull(index.getPrimaryKey());
    }

    /** Test call to index method with an existing index with primary key */
    @Test
    public void testIndexMethodCallExistingIndexWithPrimaryKey() throws Exception {
        String indexUid = "IndexMethodCallExistingIndexWithPrimaryKey";
        String primaryKey = "PrimaryKey";
        Index createdIndex = createEmptyIndex(indexUid, primaryKey);
        Index index = client.index(indexUid);

        assertEquals(createdIndex.getUid(), index.getUid());
        assertNull(index.getPrimaryKey());

        index.fetchPrimaryKey();

        assertEquals(primaryKey, index.getPrimaryKey());
    }

    /** Test call to create dump */
    @Test
    public void testCreateDump() throws Exception {
        TaskInfo task = client.createDump();
        client.waitForTask(task.getTaskUid());
        Task dump = client.getTask(task.getTaskUid());

        assertEquals(task.getStatus(), "enqueued");
        assertEquals("dumpCreation", dump.getType());
    }
}
