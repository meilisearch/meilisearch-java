package com.meilisearch.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.meilisearch.sdk.model.TasksQuery;
import org.junit.jupiter.api.Test;

class TasksHandlerUtilsTest {

    @Test
    void addIndexUidToQueryWithParamNull() {
        final Config config = new Config("http://localhost:7700", "masterKey");
        TasksHandler classToTest = new TasksHandler(config);
        TasksQuery param = null;
        TasksQuery query = classToTest.addIndexUidToQuery("indexName", param);

        assertEquals("?indexUids=indexName", query.toQuery().toString());
    }

    @Test
    void addIndexUidToQueryWithParam() {
        final Config config = new Config("http://localhost:7700", "masterKey");
        TasksHandler classToTest = new TasksHandler(config);
        TasksQuery param = new TasksQuery().setIndexUids(new String[] {});
        TasksQuery query = classToTest.addIndexUidToQuery("indexName", param);

        assertEquals("?indexUids=indexName", query.toQuery().toString());
    }

    @Test
    void addIndexUidToQueryWithOneIndexUid() {
        final Config config = new Config("http://localhost:7700", "masterKey");
        TasksHandler classToTest = new TasksHandler(config);
        TasksQuery param = new TasksQuery().setIndexUids(new String[] {"indexName2"});
        TasksQuery query = classToTest.addIndexUidToQuery("indexName1", param);

        assertEquals("?indexUids=indexName2,indexName1", query.toQuery().toString());
    }

    @Test
    void addIndexUidToQueryWithMultipleIndexUids() {
        final Config config = new Config("http://localhost:7700", "masterKey");
        TasksHandler classToTest = new TasksHandler(config);
        TasksQuery param =
                new TasksQuery()
                        .setIndexUids(
                                new String[] {
                                    "indexName2", "indexName3", "indexName4", "indexName5"
                                });
        TasksQuery query = classToTest.addIndexUidToQuery("indexName1", param);

        assertEquals(
                "?indexUids=indexName2,indexName3,indexName4,indexName5,indexName1",
                query.toQuery().toString());
    }
}
