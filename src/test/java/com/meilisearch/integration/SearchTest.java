package com.meilisearch.integration;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.UpdateStatus;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.model.SearchResult;
import com.meilisearch.sdk.utils.Movie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
public class SearchTest extends AbstractIT {

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
        this.setUp();
        if (testData == null) testData = this.getTestData(MOVIES_INDEX, Movie.class);
    }

    @AfterAll
    static void cleanMeiliSearch() {
        cleanup();
    }

    // TODO: Real Search tests after search refactor

    /** Test basic search */
    @Test
    public void testBasicSearch() throws Exception {
        String indexUid = "BasicSearch";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                jsonGson.decode(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());

        SearchResult searchResult = index.search("batman");

        System.out.println(searchResult.getFacetsDistribution());
        assertEquals(1, searchResult.getHits().size());
        assertEquals(0, searchResult.getOffset());
        assertEquals(20, searchResult.getLimit());
        assertEquals(1, searchResult.getNbHits());
    }

    /** Test search offset */
    @Test
    public void testSearchOffset() throws Exception {
        String indexUid = "SearchOffset";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                jsonGson.decode(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());

        SearchRequest searchRequest = new SearchRequest("a").setOffset(20);
        SearchResult searchResult = index.search(searchRequest);

        assertEquals(10, searchResult.getHits().size());
        assertEquals(30, searchResult.getNbHits());
    }

    /** Test search limit */
    @Test
    public void testSearchLimit() throws Exception {
        String indexUid = "SearchLimit";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                jsonGson.decode(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());

        SearchRequest searchRequest = new SearchRequest("a").setLimit(2);
        SearchResult searchResult = index.search(searchRequest);

        assertEquals(2, searchResult.getHits().size());
        assertEquals(30, searchResult.getNbHits());
    }

    /** Test search attributesToRetrieve */
    @Test
    public void testRawSearchAttributesToRetrieve() throws Exception {
        String indexUid = "SearchAttributesToRetrieve";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                jsonGson.decode(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());

        SearchRequest searchRequest =
                new SearchRequest("a").setAttributesToRetrieve(new String[] {"id", "title"});

        Results res_gson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertEquals(20, res_gson.hits.length);
        assertThat(res_gson.hits[0].getId(), instanceOf(String.class));
        assertThat(res_gson.hits[0].getTitle(), instanceOf(String.class));
        assertNull(res_gson.hits[0].getPoster());
        assertNull(res_gson.hits[0].getOverview());
        assertNull(res_gson.hits[0].getRelease_date());
        assertNull(res_gson.hits[0].getLanguage());
        assertNull(res_gson.hits[0].getGenres());
    }

    /** Test search crop */
    @Test
    public void testSearchCrop() throws Exception {
        String indexUid = "SearchCrop";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                jsonGson.decode(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());

        SearchRequest searchRequest =
                new SearchRequest("and")
                        .setAttributesToCrop(new String[] {"overview"})
                        .setCropLength(5);

        SearchResult searchResult = index.search(searchRequest);

        assertEquals(20, searchResult.getHits().size());
    }

    /** Test search highlight */
    @Test
    public void testSearchHighlight() throws Exception {
        String indexUid = "SearchHighlight";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                jsonGson.decode(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());

        SearchRequest searchRequest =
                new SearchRequest("and").setAttributesToHighlight(new String[] {"overview"});

        SearchResult searchResult = index.search(searchRequest);

        assertEquals(20, searchResult.getHits().size());
    }

    /** Test search filters */
    @Test
    public void testRawSearchFilters() throws Exception {
        String indexUid = "SearchFilters";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                jsonGson.decode(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());

        SearchRequest searchRequest =
                new SearchRequest("and").setFilters("title = \"The Dark Knight\"");

        Results res_gson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertEquals(1, res_gson.hits.length);
        assertEquals("155", res_gson.hits[0].getId());
        assertEquals("The Dark Knight", res_gson.hits[0].getTitle());
    }

    /** Test search filters complex */
    @Test
    public void testRawSearchFiltersComplex() throws Exception {
        String indexUid = "SearchFiltersComplex";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                jsonGson.decode(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());

        SearchRequest searchRequest =
                new SearchRequest("and").setFilters("title = \"The Dark Knight\" OR id = 290859");

        Results res_gson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertEquals(2, res_gson.hits.length);
        assertEquals("155", res_gson.hits[0].getId());
        assertEquals("290859", res_gson.hits[1].getId());
    }

    /** Test search matches */
    @Test
    public void testSearchMatches() throws Exception {
        String indexUid = "SearchMatches";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                jsonGson.decode(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());

        SearchRequest searchRequest = new SearchRequest("and").setMatches(true);
        SearchResult searchResult = index.search(searchRequest);

        assertEquals(20, searchResult.getHits().size());
    }
    /** Test place holder search */
    @Test
    public void testPlaceHolder() throws Exception {
        String indexUid = "placeHolder";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                jsonGson.decode(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());
        SearchResult result = index.search("");

        assertEquals(20, result.getLimit());
    }

    /** Test place holder search */
    @Test
    public void testPlaceHolderWithLimit() throws Exception {
        String indexUid = "BasicSearch";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                jsonGson.decode(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());
        SearchResult searchResult = index.search(new SearchRequest(null).setLimit(10));

        assertEquals(10, searchResult.getHits().size());
    }
}
