package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.exceptions.MeilisearchApiException;
import com.meilisearch.sdk.model.IndexesQuery;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.SwapIndexesParams;
import com.meilisearch.sdk.model.Task;
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
        cleanup();
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
    }

    /** Test Index creation with PrimaryKey */
    @Test
    public void testCreateIndexWithPrimaryKey() throws Exception {
        String indexUid = "CreateIndexWithPrimaryKey";
        Index index = createEmptyIndex(indexUid, this.primaryKey);

        assertEquals(index.getUid(), indexUid);
        assertEquals(index.getPrimaryKey(), this.primaryKey);
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
    }

    /** Test getIndex */
    @Test
    public void testGetIndex() throws Exception {
        String indexUid = "GetIndex";
        Index index = createEmptyIndex(indexUid);
        Index getIndex = client.getIndex(indexUid);

        assertEquals(index.getUid(), getIndex.getUid());
        assertEquals(index.getPrimaryKey(), getIndex.getPrimaryKey());
    }

    /** Test getIndexes */
    @Test
    public void testGetIndexes() throws Exception {
        String[] indexUids = {"GetIndexes", "GetIndexes2"};
        createEmptyIndex(indexUids[0]);
        createEmptyIndex(indexUids[1], this.primaryKey);
        Results<Index> indexes = client.getIndexes();

        assertEquals(2, indexes.getResults().length);
        assert (Arrays.asList(indexUids).contains(indexUids[0]));
        assert (Arrays.asList(indexUids).contains(indexUids[1]));
    }

    /** Test getIndexes with limit */
    @Test
    public void testGetIndexesLimit() throws Exception {
        int limit = 1;
        String[] indexUids = {"GetIndexesLimit", "GetIndexesLimit2"};
        IndexesQuery query = new IndexesQuery().setLimit(limit);
        createEmptyIndex(indexUids[0]);
        createEmptyIndex(indexUids[1], this.primaryKey);
        Results<Index> indexes = client.getIndexes(query);

        assertEquals(limit, indexes.getResults().length);
        assertEquals(limit, indexes.getLimit());
    }

    /** Test getIndexes with limit and offset */
    @Test
    public void testGetIndexesLimitAndOffset() throws Exception {
        int limit = 1;
        int offset = 1;
        String[] indexUids = {"GetIndexesLimitOffset", "GetIndexesLimitOffset2"};
        IndexesQuery query = new IndexesQuery().setLimit(limit).setOffset(offset);
        createEmptyIndex(indexUids[0]);
        createEmptyIndex(indexUids[1], this.primaryKey);
        Results<Index> indexes = client.getIndexes(query);

        assertEquals(limit, indexes.getResults().length);
        assertEquals(limit, indexes.getLimit());
        assertEquals(offset, indexes.getOffset());
    }

    /** Test getRawIndexes */
    @Test
    public void testGetRawIndexes() throws Exception {
        String[] indexUids = {"GetRawIndexes", "GetRawIndexes2"};
        createEmptyIndex(indexUids[0]);
        createEmptyIndex(indexUids[1], this.primaryKey);

        String indexes = client.getRawIndexes();
        JsonObject jsonIndexObject = JsonParser.parseString(indexes).getAsJsonObject();
        JsonArray jsonIndexArray = jsonIndexObject.getAsJsonArray("results");

        assertEquals(2, jsonIndexArray.size());
        assert (Arrays.asList(indexUids)
                .contains(jsonIndexArray.get(0).getAsJsonObject().get("uid").getAsString()));
        assert (Arrays.asList(indexUids)
                .contains(jsonIndexArray.get(1).getAsJsonObject().get("uid").getAsString()));
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

    /** Test swapIndexes */
    @Test
    public void testSwapIndexes() throws Exception {
        String indexUidA = "IndexA";
        String indexUidB = "IndexB";
        Index indexA = createEmptyIndex(indexUidA);
        Index indexB = createEmptyIndex(indexUidB);
        TaskInfo taskAddDocumentIndexA =
                indexA.addDocuments(
                        "[{"
                                + "\"id\": 1,"
                                + "\"title\": \"Document1\""
                                + "},"
                                + "{"
                                + "\"id\": 2,"
                                + "\"title\": \"Document2\""
                                + "}]");
        indexA.waitForTask(taskAddDocumentIndexA.getTaskUid());

        SwapIndexesParams[] params =
                new SwapIndexesParams[] {
                    new SwapIndexesParams().setIndexes(new String[] {indexUidA, indexUidB})
                };
        TaskInfo task = client.swapIndexes(params);

        assertEquals("indexSwap", task.getType());
        assertEquals("Document1", indexB.getDocument("1", Movie.class).getTitle());
        assertEquals("Document2", indexB.getDocument("2", Movie.class).getTitle());
        assertThrows(MeilisearchApiException.class, () -> indexA.getDocument("1", Movie.class));
        assertThrows(MeilisearchApiException.class, () -> indexA.getDocument("2", Movie.class));
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
