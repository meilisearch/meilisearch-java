package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.sdk.model.BatchesQuery;
import com.meilisearch.sdk.model.CursorResults;
import com.meilisearch.sdk.model.batch_dto.Batch;
import org.junit.jupiter.api.*;

@Tag("integration")
public class BatchTest extends AbstractIT {

    private static final String INDEX_UID = "batch-test";

    @BeforeEach
    void setup() throws Exception {
        this.setUp();
        this.setUpJacksonClient();
        client.createIndex(INDEX_UID);
    }

    @AfterAll
    static void cleanMeilisearch() {
        cleanup();
    }

    @Test
    void testGetAllBatches() {
        CursorResults<Batch> batches = client.getAllBatches(new BatchesQuery());
        assertNotNull(batches);
        assertFalse(batches.getResults().isEmpty(), "Batch results should not be empty");
    }

    @Test
    void testGetOneBatch() {

        CursorResults<Batch> batches = client.getAllBatches(new BatchesQuery());
        assertFalse(batches.getResults().isEmpty(), "No batches found");

        Batch firstBatch = client.getBatch(batches.getResults().get(0).getUid());

        assertNotNull(firstBatch);
        assertEquals(batches.getResults().get(0).getUid(), firstBatch.getUid());
        assertNotNull(firstBatch.getDetails());
        assertNotNull(firstBatch.getStats());
        assertTrue(firstBatch.getStats().getTotalNbTasks() > 0);
        assertNotNull(firstBatch.getStats().getStatus());
        assertNotNull(firstBatch.getStats().getTypes());
        assertNotNull(firstBatch.getStats().getIndexUids());
        assertNotNull(firstBatch.getDuration());
        assertNotNull(firstBatch.getStartedAt());
        assertNotNull(firstBatch.getFinishedAt());
        assertNotNull(firstBatch.getProgress());
    }
}
