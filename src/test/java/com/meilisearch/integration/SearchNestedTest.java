package com.meilisearch.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.model.Settings;
import com.meilisearch.sdk.model.TaskInfo;
import com.meilisearch.sdk.utils.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
public class SearchNestedTest extends AbstractIT {

    private TestData<Movie> testData;

    final class Results {
        Movie[] hits;
        int offset;
        int limit;
        int nbHits;
        int processingTimeMs;
        String query;
    }

    @BeforeEach
    public void initialize() {
        cleanup();
        this.setUp();
        if (testData == null) testData = this.getTestData(NESTED_MOVIES, Movie.class);
    }

    /** Test basic search in nested fields */
    @Test
    public void testBasicSearchInNestedFields() throws Exception {
        String indexUid = "SearchOnNestedFields";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(NESTED_MOVIES, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());
        index.waitForTask(task.getTaskUid());
        Results searchResultGson = jsonGson.decode(index.rawSearch("An awesome"), Results.class);

        assertThat(searchResultGson.hits, is(arrayWithSize(1)));
        assertThat(searchResultGson.hits[0].getId(), is(equalTo("5")));
    }

    /** Test search on nested documents with searchable attributes */
    @Test
    public void testSearchOnNestedFieldsWithSearchableAttributes() throws Exception {
        String indexUid = "SearchOnNestedFields";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();
        String[] newSearchableAttributes = {"title", "info.comment"};

        TestData<Movie> testData = this.getTestData(NESTED_MOVIES, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        index.waitForTask(
                index.updateSearchableAttributesSettings(newSearchableAttributes).getTaskUid());

        Results searchResultGson = jsonGson.decode(index.rawSearch("An awesome"), Results.class);

        assertThat(searchResultGson.hits, is(arrayWithSize(1)));
        assertThat(searchResultGson.hits[0].getId(), is(equalTo("5")));
    }

    /** Test search on nested documents with sortable attributes */
    @Test
    public void testSearchOnNestedFieldsWithSortableAttributes() throws Exception {
        String indexUid = "SearchOnNestedFields";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();
        Settings newSettings = new Settings();
        newSettings.setSortableAttributes("info.reviewNb");

        TestData<Movie> testData = this.getTestData(NESTED_MOVIES, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());
        index.waitForTask(task.getTaskUid());
        index.waitForTask(index.updateSettings(newSettings).getTaskUid());
        SearchRequest searchRequest =
                SearchRequest.builder().q("An Awesome").sort("info.reviewNb:desc").build();

        Results searchResultGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertThat(searchResultGson.hits, is(arrayWithSize(1)));
        assertThat(searchResultGson.hits[0].getId(), is(equalTo("5")));
    }

    /** Test search on nested documents with sortable and searchable attributes */
    @Test
    public void testSearchOnNestedFieldsWithSortableAndSearchableAttributes() throws Exception {
        String indexUid = "SearchOnNestedFields";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();
        Settings newSettings = new Settings();
        newSettings.setSearchableAttributes("title", "info.comment");
        newSettings.setSortableAttributes("info.reviewNb");

        TestData<Movie> testData = this.getTestData(NESTED_MOVIES, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());
        index.waitForTask(task.getTaskUid());
        index.waitForTask(index.updateSettings(newSettings).getTaskUid());
        SearchRequest searchRequest =
                SearchRequest.builder().q("An Awesome").sort("info.reviewNb:desc").build();
        Results searchResultGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertThat(searchResultGson.hits, is(arrayWithSize(1)));
        assertThat(searchResultGson.hits[0].getId(), is(equalTo("5")));
    }
}
