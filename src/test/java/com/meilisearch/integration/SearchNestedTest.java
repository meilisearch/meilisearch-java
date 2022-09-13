package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.model.Settings;
import com.meilisearch.sdk.model.Task;
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
        boolean exhaustiveNbHits;
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
        Task task = index.addDocuments(testData.getRaw());
        index.waitForTask(task.getUid());
        Results searchResultGson = jsonGson.decode(index.rawSearch("An awesome"), Results.class);

        assertEquals(1, searchResultGson.hits.length);
        assertEquals("5", searchResultGson.hits[0].getId());
    }

    /** Test search on nested documents with searchable attributes */
    @Test
    public void testSearchOnNestedFieldsWithSearchableAttributes() throws Exception {
        String indexUid = "SearchOnNestedFields";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();
        String[] newSearchableAttributes = {"title", "info.comment"};

        TestData<Movie> testData = this.getTestData(NESTED_MOVIES, Movie.class);
        Task task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getUid());

        index.waitForTask(
                index.updateSearchableAttributesSettings(newSearchableAttributes).getUid());

        Results searchResultGson = jsonGson.decode(index.rawSearch("An awesome"), Results.class);

        assertEquals(1, searchResultGson.hits.length);
        assertEquals("5", searchResultGson.hits[0].getId());
    }

    /** Test search on nested documents with sortable attributes */
    @Test
    public void testSearchOnNestedFieldsWithSortableAttributes() throws Exception {
        String indexUid = "SearchOnNestedFields";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();
        Settings newSettings = new Settings();
        newSettings.setSortableAttributes(new String[] {"info.reviewNb"});

        TestData<Movie> testData = this.getTestData(NESTED_MOVIES, Movie.class);
        Task task = index.addDocuments(testData.getRaw());
        index.waitForTask(task.getUid());
        index.waitForTask(index.updateSettings(newSettings).getUid());
        SearchRequest searchRequest =
                new SearchRequest("An Awesome").setSort(new String[] {"info.reviewNb:desc"});

        Results searchResultGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertEquals(1, searchResultGson.hits.length);
        assertEquals("5", searchResultGson.hits[0].getId());
    }

    /** Test search on nested documents with sortable and searchable attributes */
    @Test
    public void testSearchOnNestedFieldsWithSortableAndSearchableAttributes() throws Exception {
        String indexUid = "SearchOnNestedFields";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();
        Settings newSettings = new Settings();
        newSettings.setSearchableAttributes(new String[] {"title", "info.comment"});
        newSettings.setSortableAttributes(new String[] {"info.reviewNb"});

        TestData<Movie> testData = this.getTestData(NESTED_MOVIES, Movie.class);
        Task task = index.addDocuments(testData.getRaw());
        index.waitForTask(task.getUid());
        index.waitForTask(index.updateSettings(newSettings).getUid());
        SearchRequest searchRequest =
                new SearchRequest("An Awesome").setSort(new String[] {"info.reviewNb:desc"});
        Results searchResultGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertEquals(1, searchResultGson.hits.length);
        assertEquals("5", searchResultGson.hits[0].getId());
    }
}
