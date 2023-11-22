package com.meilisearch.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.*;
import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.exceptions.MeilisearchApiException;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.*;
import com.meilisearch.sdk.utils.Movie;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.*;

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

        assertThat(index.getUid(), is(equalTo(indexUid)));
        assertThat(index.getPrimaryKey(), is(nullValue()));
    }

    /** Test Index creation without PrimaryKey with Jackson Json Handler */
    @Test
    public void testCreateIndexWithoutPrimaryKeyWithJacksonJsonHandler() throws Exception {
        String indexUid = "CreateIndexWithoutPrimaryKeyWithJacksonJsonHandler";
        TaskInfo task = clientJackson.createIndex(indexUid);
        clientJackson.waitForTask(task.getTaskUid());
        Index index = clientJackson.getIndex(indexUid);

        assertThat(index.getUid(), is(equalTo(indexUid)));
        assertThat(index.getPrimaryKey(), is(nullValue()));
    }

    /** Test Index creation with PrimaryKey */
    @Test
    public void testCreateIndexWithPrimaryKey() throws Exception {
        String indexUid = "CreateIndexWithPrimaryKey";
        Index index = createEmptyIndex(indexUid, this.primaryKey);

        assertThat(index.getUid(), is(equalTo(indexUid)));
        assertThat(index.getPrimaryKey(), is(equalTo(this.primaryKey)));
    }

    /** Test Index creation with PrimaryKey with Jackson Json Handler */
    @Test
    public void testCreateIndexWithPrimaryKeyWithJacksonJsonHandler() throws Exception {
        String indexUid = "CreateIndexWithPrimaryKeyWithJacksonJsonHandler";
        TaskInfo task = clientJackson.createIndex(indexUid, this.primaryKey);
        clientJackson.waitForTask(task.getTaskUid());
        Index index = clientJackson.getIndex(indexUid);

        assertThat(index.getUid(), is(equalTo(indexUid)));
        assertThat(index.getPrimaryKey(), is(equalTo(this.primaryKey)));
    }

    /** Test Index creation twice doesn't throw an error: already exists */
    @Test
    public void testCreateIndexAlreadyExists() throws Exception {
        String indexUid = "CreateIndexAlreadyExists";
        Index index = createEmptyIndex(indexUid, this.primaryKey);

        assertThat(index.getUid(), is(equalTo(indexUid)));
        assertThat(index.getPrimaryKey(), is(equalTo(this.primaryKey)));

        Index indexDuplicate = createEmptyIndex(indexUid, this.primaryKey);

        assertThat(index.getUid(), is(equalTo(indexUid)));
        assertThat(indexDuplicate.getUid(), is(equalTo(indexUid)));
        assertThat(index.getPrimaryKey(), is(equalTo(this.primaryKey)));
        assertThat(indexDuplicate.getPrimaryKey(), is(equalTo(this.primaryKey)));
    }

    /** Test update Index PrimaryKey */
    @Test
    public void testUpdateIndexPrimaryKey() throws Exception {
        String indexUid = "UpdateIndexPrimaryKey";
        Index index = createEmptyIndex(indexUid);

        assertThat(index.getUid(), is(equalTo(indexUid)));
        assertThat(index.getPrimaryKey(), is(nullValue()));

        TaskInfo task = client.updateIndex(indexUid, this.primaryKey);
        client.waitForTask(task.getTaskUid());
        index = client.getIndex(indexUid);

        assertThat(index, instanceOf(Index.class));
        assertThat(index.getUid(), is(equalTo(indexUid)));
        assertThat(index.getPrimaryKey(), is(equalTo(this.primaryKey)));
    }

    /** Test getIndex */
    @Test
    public void testGetIndex() throws Exception {
        String indexUid = "GetIndex";
        Index index = createEmptyIndex(indexUid);
        Index getIndex = client.getIndex(indexUid);

        assertThat(getIndex.getUid(), is(equalTo(index.getUid())));
        assertThat(getIndex.getPrimaryKey(), is(equalTo(index.getPrimaryKey())));
    }

    /** Test getIndexes */
    @Test
    public void testGetIndexes() throws Exception {
        String[] indexUids = {"GetIndexes", "GetIndexes2"};
        createEmptyIndex(indexUids[0]);
        createEmptyIndex(indexUids[1], this.primaryKey);
        Results<Index> indexes = client.getIndexes();

        assertThat(indexes.getResults(), is(arrayWithSize(2)));
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

        assertThat(indexes.getResults(), is(arrayWithSize(limit)));
        assertThat(indexes.getLimit(), is(equalTo(limit)));
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

        assertThat(indexes.getResults(), is(arrayWithSize(limit)));
        assertThat(indexes.getLimit(), is(equalTo(limit)));
        assertThat(indexes.getOffset(), is(equalTo(offset)));
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

        assertThat(jsonIndexArray.size(), is(equalTo(2)));
        assertThat(
                jsonIndexArray.get(0).getAsJsonObject().get("uid").getAsString(),
                is(in(indexUids)));
        assertThat(
                jsonIndexArray.get(1).getAsJsonObject().get("uid").getAsString(),
                is(in(indexUids)));
    }

    /** Test getRawIndexes with limits */
    @Test
    public void testGetRawIndexesLimit() throws Exception {
        int limit = 1;
        String[] indexUids = {"GetRawIndexes", "GetRawIndexes2"};
        createEmptyIndex(indexUids[0]);
        createEmptyIndex(indexUids[1], this.primaryKey);
        IndexesQuery query = new IndexesQuery().setLimit(limit);

        String indexes = client.getRawIndexes(query);
        JsonObject jsonIndexObject = JsonParser.parseString(indexes).getAsJsonObject();
        JsonArray jsonIndexArray = jsonIndexObject.getAsJsonArray("results");

        assertThat(jsonIndexArray.size(), is(equalTo(limit)));
        assertThat(jsonIndexObject.get("limit").getAsInt(), is(equalTo(limit)));
        assertThat(
                jsonIndexArray.get(0).getAsJsonObject().get("uid").getAsString(),
                is(in(indexUids)));
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
        client.waitForTask(task.getTaskUid());

        assertThat(task.getType(), is(equalTo("indexSwap")));
        assertThat(indexB.getDocument("1", Movie.class).getTitle(), is(equalTo("Document1")));
        assertThat(indexB.getDocument("2", Movie.class).getTitle(), is(equalTo("Document2")));
        assertThrows(MeilisearchApiException.class, () -> indexA.getDocument("1", Movie.class));
        assertThrows(MeilisearchApiException.class, () -> indexA.getDocument("2", Movie.class));
    }

    /** Test call to index method with an inexistent index */
    @Test
    public void testIndexMethodCallInexistentIndex() throws Exception {
        String indexUid = "IndexMethodCallInexistentIndex";
        Index index = client.index(indexUid);

        assertThat(index.getUid(), is(equalTo(indexUid)));
        assertThrows(MeilisearchApiException.class, () -> client.getIndex(indexUid));
    }

    /** Test call to index method with an existing index */
    @Test
    public void testIndexMethodCallExistingIndex() throws Exception {
        String indexUid = "IndexMethodCallExistingIndex";
        Index createdIndex = createEmptyIndex(indexUid);
        Index index = client.index(indexUid);

        assertThat(index.getUid(), is(equalTo(createdIndex.getUid())));
        assertThat(index.getPrimaryKey(), is(nullValue()));
    }

    /** Test call to index method with an existing index with primary key */
    @Test
    public void testIndexMethodCallExistingIndexWithPrimaryKey() throws Exception {
        String indexUid = "IndexMethodCallExistingIndexWithPrimaryKey";
        String primaryKey = "PrimaryKey";
        Index createdIndex = createEmptyIndex(indexUid, primaryKey);
        Index index = client.index(indexUid);

        assertThat(index.getUid(), is(equalTo(createdIndex.getUid())));
        assertThat(index.getPrimaryKey(), is(nullValue()));

        index.fetchPrimaryKey();

        assertThat(index.getPrimaryKey(), is(equalTo(primaryKey)));
    }

    /** Test call to create dump */
    @Test
    public void testCreateDump() throws Exception {
        TaskInfo task = client.createDump();
        client.waitForTask(task.getTaskUid());
        Task dump = client.getTask(task.getTaskUid());

        assertThat(task.getStatus(), is(equalTo(TaskStatus.ENQUEUED)));
        assertThat(dump.getType(), is(equalTo("dumpCreation")));
    }

    /** Test call to create snapshot */
    @Test
    public void testCreateSnapshot() throws Exception {
        TaskInfo task = client.createSnapshot();
        client.waitForTask(task.getTaskUid());
        Task snapshot = client.getTask(task.getTaskUid());

        assertThat(task.getStatus(), is(equalTo(TaskStatus.ENQUEUED)));
        assertThat(snapshot.getType(), is(equalTo("dumpCreation")));
    }

    /**
     * Test the exclusion of transient fields.
     *
     * @see <a href="https://github.com/meilisearch/meilisearch-java/issues/655">Issue #655</a>
     */
    @Test
    public void testTransientFieldExclusion() throws MeilisearchException {
        Index test = client.index("Transient");

        Gson gsonWithTransient =
                new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC).create();

        // TODO: Throws StackOverflowError on JDK 1.8, but InaccessibleObjectException on JDK9+
        Assertions.assertThrows(StackOverflowError.class, () -> gsonWithTransient.toJson(test));
        Assertions.assertDoesNotThrow(() -> gson.toJson(test));
    }
}
