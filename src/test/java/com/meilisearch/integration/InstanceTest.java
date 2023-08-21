package com.meilisearch.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.IndexStats;
import com.meilisearch.sdk.model.Stats;
import com.meilisearch.sdk.model.TaskInfo;
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

        assertThat(health, is(equalTo("{\"status\":\"available\"}")));
    }

    /** Test Is Healthy */
    @Test
    public void testIsHealthy() throws Exception {
        Boolean healthy = client.isHealthy();

        assertThat(healthy, is(equalTo(true)));
    }

    /** Test Get Version */
    @Test
    public void testGetVersion() throws Exception {
        String version = client.getVersion();

        assertThat(version, is(notNullValue()));
    }

    /** Test Get Stats */
    @Test
    public void testGetStats() throws Exception {
        Stats stats = client.getStats();

        assertThat(stats, is(notNullValue()));
        assertThat(stats.getDatabaseSize(), is(notNullValue()));
        assertThat(stats.getIndexes(), is(notNullValue()));
    }

    /** Test Get Index Stats */
    @Test
    public void testGetIndexStats() throws Exception {
        String indexUid = "IndexStats";
        Index index = client.index(indexUid);
        TaskInfo task = client.createIndex(indexUid);

        client.waitForTask(task.getTaskUid());
        IndexStats stats = index.getStats();

        assertThat(stats, is(notNullValue()));
        assertThat(stats.getNumberOfDocuments(), is(equalTo(0L)));
        assertThat(stats.isIndexing(), is(equalTo(false)));
        assertThat(stats.getFieldDistribution(), is(notNullValue()));
    }
}
