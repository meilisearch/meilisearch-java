package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.sdk.model.CursorResults;
import com.meilisearch.sdk.model.batch.req.BatchesQuery;
import com.meilisearch.sdk.model.batch.res.Batch;
import org.junit.jupiter.api.*;

@Tag("integration")
public class BatchTest extends AbstractIT {

    @BeforeEach
    void setup() throws Exception {
        this.setUp();
        this.setUpJacksonClient();
        cleanup();
    }

    @AfterAll
    static void cleanMeilisearch() {
        cleanup();
    }

    /** Check if able to fetch a single batch by uid */
    @Test
    void testGetBatch() {
        CursorResults<Batch> allBatches = client.getAllBatches(new BatchesQuery());
        assertFalse(allBatches.getResults().isEmpty(), "No batches found");
        int batchUid = allBatches.getResults().get(0).getUid();

        Batch batch = client.getBatch(batchUid);

        assertNotNull(batch);
        assertEquals(batchUid, batch.getUid());
        assertNotNull(batch.getDetails());
        assertNotNull(batch.getStats());
        assertTrue(batch.getStats().getTotalNbTasks() > 0);
        assertNotNull(batch.getStats().getStatus());
        assertNotNull(batch.getStats().getTypes());
        assertNotNull(batch.getBatchStrategy());
        assertNotNull(batch.getStats().getIndexUids());
        assertNotNull(batch.getDuration());
        assertNotNull(batch.getStartedAt());
        assertNotNull(batch.getFinishedAt());
        // TODO: Add Check for progress to be non-null, but response always provides null
    }

    /** Check if able to fetch all batch data as page */
    @Test
    void testGetAllBatches() {
        CursorResults<Batch> allBatches = client.getAllBatches(new BatchesQuery());

        assertNotNull(allBatches);
        assertFalse(allBatches.getResults().isEmpty(), "Batch results should not be empty");
    }
}
