package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.meilisearch.sdk.model.TasksQuery;
import org.junit.jupiter.api.Test;

class TaskHandlerUtilsTest {

    @Test
    void addIndexUidToQueryWithParamNull() {
        final Config config = new Config("http://localhost:7700", "masterKey");
        TasksHandler classToTest = new TasksHandler(config);
        TasksQuery param = null;
        TasksQuery query = classToTest.addIndexUidToQuery("indexName", param);

        assertThat(query.toQuery(), is(equalTo("?indexUids=indexName")));
    }

    @Test
    void addIndexUidToQueryWithParam() {
        final Config config = new Config("http://localhost:7700", "masterKey");
        TasksHandler classToTest = new TasksHandler(config);
        TasksQuery param = new TasksQuery().setIndexUids();
        TasksQuery query = classToTest.addIndexUidToQuery("indexName", param);

        assertThat(query.toQuery(), is(equalTo("?indexUids=indexName")));
    }

    @Test
    void addIndexUidToQueryWithOneIndexUid() {
        final Config config = new Config("http://localhost:7700", "masterKey");
        TasksHandler classToTest = new TasksHandler(config);
        TasksQuery param = new TasksQuery().setIndexUids("indexName2");
        TasksQuery query = classToTest.addIndexUidToQuery("indexName1", param);

        assertThat(query.toQuery(), is(equalTo("?indexUids=indexName2,indexName1")));
    }

    @Test
    void addIndexUidToQueryWithMultipleIndexUids() {
        final Config config = new Config("http://localhost:7700", "masterKey");
        TasksHandler classToTest = new TasksHandler(config);
        TasksQuery param =
                new TasksQuery()
                        .setIndexUids("indexName2", "indexName3", "indexName4", "indexName5");
        TasksQuery query = classToTest.addIndexUidToQuery("indexName1", param);

        assertThat(
                query.toQuery(),
                is(equalTo("?indexUids=indexName2,indexName3,indexName4,indexName5,indexName1")));
    }
}
