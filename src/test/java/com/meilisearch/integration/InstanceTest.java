package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.IndexStats;
import com.meilisearch.sdk.model.Stats;
import com.meilisearch.sdk.model.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
public class InstanceTest extends AbstractIT {

    @BeforeEach
    public void initialize() {
        setUp();
    }

    @AfterAll
    static void cleanMeilisearch() {
        cleanup();
    }

    /** Test Health */
    @Test
    public void testHealth() throws Exception {
        String health = client.health();

        assertEquals(health, "{\"status\":\"available\"}");
    }

    /** Test Is Healthy */
    @Test
    public void testIsHealthy() throws Exception {
        Boolean healthy = client.isHealthy();

        assertTrue(healthy);
    }

    /** Test Get Version */
    @Test
    public void testGetVersion() throws Exception {
        String version = client.getVersion();

        assertNotNull(version);
    }

    /** Test Get Stats */
    @Test
    public void testGetStats() throws Exception {
        Stats stats = client.getStats();

        assertNotNull(stats);
        assertNotNull(stats.getDatabaseSize());
        assertNotNull(stats.getIndexes());
    }

    /** Test Get Index Stats */
    @Test
    public void testGetIndexStats() throws Exception {
        String indexUid = "IndexStats";
        Index index = client.index(indexUid);
        Task task = client.createIndex(indexUid);

        client.waitForTask(task.getUid());
        IndexStats stats = index.getStats();

        assertNotNull(stats);
        assertEquals(0, stats.getNumberOfDocuments());
        assertFalse(stats.isIndexing());
        assertNotNull(stats.getFieldDistribution());
    }
}
