package com.meilisearch.integration;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.model.MatchingStrategy;
import com.meilisearch.sdk.model.SearchResult;
import com.meilisearch.sdk.model.SearchResultPaginated;
import com.meilisearch.sdk.model.Searchable;
import com.meilisearch.sdk.model.Settings;
import com.meilisearch.sdk.model.TaskInfo;
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
        int estimatedTotalHits;
        int processingTimeMs;
        String query;
    }

    @BeforeEach
    public void initialize() {
        this.setUp();
        if (testData == null) testData = this.getTestData(MOVIES_INDEX, Movie.class);
    }

    @AfterAll
    static void cleanMeilisearch() {
        cleanup();
    }

    // TODO: Real Search tests after search refactor

    /** Test basic search */
    @Test
    public void testBasicSearch() throws Exception {
        String indexUid = "BasicSearch";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchResult searchResult = index.search("batman");

        assertNull(searchResult.getFacetDistribution());
        assertEquals(1, searchResult.getHits().size());
        assertEquals(0, searchResult.getOffset());
        assertEquals(20, searchResult.getLimit());
        assertEquals(1, searchResult.getEstimatedTotalHits());
    }

    /** Test search offset */
    @Test
    public void testSearchOffset() throws Exception {
        String indexUid = "SearchOffset";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest = SearchRequest.builder().q("a").offset(20).build();
        SearchResult searchResult = (SearchResult) index.search(searchRequest);

        assertEquals(10, searchResult.getHits().size());
        assertEquals(30, searchResult.getEstimatedTotalHits());
    }

    /** Test search limit */
    @Test
    public void testSearchLimit() throws Exception {
        String indexUid = "SearchLimit";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest = SearchRequest.builder().q("a").limit(2).build();
        SearchResult searchResult = (SearchResult) index.search(searchRequest);

        assertEquals(2, searchResult.getHits().size());
        assertEquals(30, searchResult.getEstimatedTotalHits());
    }

    /** Test search attributesToRetrieve */
    @Test
    public void testRawSearchAttributesToRetrieve() throws Exception {
        String indexUid = "SearchAttributesToRetrieve";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder()
                        .q("a")
                        .attributesToRetrieve(new String[] {"id", "title"})
                        .build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertEquals(20, resGson.hits.length);
        assertThat(resGson.hits[0].getId(), instanceOf(String.class));
        assertThat(resGson.hits[0].getTitle(), instanceOf(String.class));
        assertNull(resGson.hits[0].getPoster());
        assertNull(resGson.hits[0].getOverview());
        assertNull(resGson.hits[0].getRelease_date());
        assertNull(resGson.hits[0].getLanguage());
        assertNull(resGson.hits[0].getGenres());
    }

    /** Test search crop */
    @Test
    public void testSearchCrop() throws Exception {
        String indexUid = "SearchCrop";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder()
                        .q("and")
                        .attributesToCrop(new String[] {"overview"})
                        .cropLength(1)
                        .build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertEquals(20, resGson.hits.length);
        assertEquals(5, resGson.hits[0].getFormatted().getOverview().length());
        assertEquals("…and…", resGson.hits[0].getFormatted().getOverview());
    }

    /** Test search with customized crop marker */
    @Test
    public void testSearchCropMarker() throws Exception {
        String indexUid = "SearchCropMarker";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder()
                        .q("and")
                        .attributesToCrop(new String[] {"overview"})
                        .cropLength(1)
                        .cropMarker("(ꈍᴗꈍ)")
                        .build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertEquals(20, resGson.hits.length);
        assertEquals("(ꈍᴗꈍ)and(ꈍᴗꈍ)", resGson.hits[0].getFormatted().getOverview());
    }

    /** Test search highlight */
    @Test
    public void testSearchHighlight() throws Exception {
        String indexUid = "SearchHighlight";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder()
                        .q("and")
                        .attributesToHighlight(new String[] {"title"})
                        .build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertEquals(20, resGson.hits.length);
        assertEquals(
                "Harry Potter <em>and</em> the Philosopher's Stone",
                resGson.hits[0].getFormatted().getTitle());
    }

    /** Test search with customized highlight */
    @Test
    public void testSearchWithCustomizedHighlight() throws Exception {
        String indexUid = "SearchHighlight";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder()
                        .q("and")
                        .attributesToHighlight(new String[] {"title"})
                        .highlightPreTag("(⊃｡•́‿•̀｡)⊃ ")
                        .highlightPostTag(" ⊂(´• ω •`⊂)")
                        .build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertEquals(20, resGson.hits.length);
        assertEquals(
                "Harry Potter (⊃｡•́‿•̀｡)⊃ and ⊂(´• ω •`⊂) the Philosopher's Stone",
                resGson.hits[0].getFormatted().getTitle());
    }

    /** Test search with customized highlight */
    @Test
    public void testSearchWithMatchingStrategy() throws Exception {
        String indexUid = "SearchMatchingStrategy";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder().q("and").matchingStrategy(MatchingStrategy.ALL).build();

        SearchResult searchResult = (SearchResult) index.search(searchRequest);

        assertEquals(20, searchResult.getHits().size());
        assertEquals(21, searchResult.getEstimatedTotalHits());
    }

    /** Test search with phrase */
    @Test
    public void testSearchPhrase() throws Exception {
        String indexUid = "SearchPhrase";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        Results resGson = jsonGson.decode(index.rawSearch("coco \"harry\""), Results.class);

        assertEquals(1, resGson.hits.length);
        assertEquals("671", resGson.hits[0].getId());
        assertEquals("Harry Potter and the Philosopher's Stone", resGson.hits[0].getTitle());
    }

    /** Test search filter */
    @Test
    public void testRawSearchFilter() throws Exception {
        String indexUid = "SearchFilter";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        Settings settings = index.getSettings();

        settings.setFilterableAttributes(new String[] {"title"});
        index.waitForTask(index.updateSettings(settings).getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder()
                        .q("and")
                        .filter(new String[] {"title = \"The Dark Knight\""})
                        .build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertEquals(1, resGson.hits.length);
        assertEquals("155", resGson.hits[0].getId());
        assertEquals("The Dark Knight", resGson.hits[0].getTitle());
    }

    /** Test search filter complex */
    @Test
    public void testRawSearchFilterComplex() throws Exception {
        String indexUid = "SearchFilterComplex";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        Settings settings = index.getSettings();

        settings.setFilterableAttributes(new String[] {"title", "id"});
        index.waitForTask(index.updateSettings(settings).getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder()
                        .q("and")
                        .filter(new String[] {"title = \"The Dark Knight\" OR id = 290859"})
                        .build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertEquals(2, resGson.hits.length);
        assertEquals("155", resGson.hits[0].getId());
        assertEquals("290859", resGson.hits[1].getId());
    }

    /** Test search facet distribution */
    @Test
    public void testSearchFacetsDistribution() throws Exception {
        String indexUid = "SearchFacetsDistribution";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        Settings settings = index.getSettings();

        settings.setFilterableAttributes(new String[] {"title"});
        index.waitForTask(index.updateSettings(settings).getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder().q("knight").facets(new String[] {"*"}).build();

        Searchable searchResult = index.search(searchRequest);

        assertEquals(1, searchResult.getHits().size());
        assertNotNull(searchResult.getFacetDistribution());
    }

    /** Test search sort */
    @Test
    public void testRawSearchSort() throws Exception {
        String indexUid = "SearchSort";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        Settings settings = index.getSettings();

        settings.setSortableAttributes(new String[] {"title"});
        index.waitForTask(index.updateSettings(settings).getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder().q("and").sort(new String[] {"title:asc"}).build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertEquals(20, resGson.hits.length);
        assertEquals("671", resGson.hits[0].getId());
        assertEquals("Harry Potter and the Philosopher's Stone", resGson.hits[0].getTitle());
        assertEquals("495764", resGson.hits[1].getId());
        assertEquals(
                "Birds of Prey (and the Fantabulous Emancipation of One Harley Quinn)",
                resGson.hits[1].getTitle());
    }

    /** Test search sort */
    @Test
    public void testRawSearchSortWithIntParameter() throws Exception {
        String indexUid = "SearchSortWithIntParameter";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        Settings settings = index.getSettings();

        settings.setSortableAttributes(new String[] {"id"});
        index.waitForTask(index.updateSettings(settings).getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder().q("and").sort(new String[] {"id:asc"}).build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertEquals(20, resGson.hits.length);
        assertEquals("671", resGson.hits[0].getId());
        assertEquals("Harry Potter and the Philosopher's Stone", resGson.hits[0].getTitle());
        assertEquals("495764", resGson.hits[1].getId());
        assertEquals(
                "Birds of Prey (and the Fantabulous Emancipation of One Harley Quinn)",
                resGson.hits[1].getTitle());
    }

    /** Test search sort */
    @Test
    public void testRawSearchSortWithMultipleParameter() throws Exception {
        String indexUid = "SearchSortWithMultipleParameter";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        Settings settings = index.getSettings();

        settings.setSortableAttributes(new String[] {"id", "title"});
        index.waitForTask(index.updateSettings(settings).getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder()
                        .q("dark")
                        .sort(new String[] {"id:asc", "title:asc"})
                        .build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertEquals(3, resGson.hits.length);
        assertEquals("155", resGson.hits[0].getId());
        assertEquals("The Dark Knight", resGson.hits[0].getTitle());
        assertEquals("290859", resGson.hits[1].getId());
        assertEquals("Terminator: Dark Fate", resGson.hits[1].getTitle());
    }

    /** Test search sort */
    @Test
    public void testRawSearchSortWithPlaceHolder() throws Exception {
        String indexUid = "SearchSortWithPlaceHolder";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        Settings settings = index.getSettings();

        settings.setSortableAttributes(new String[] {"id", "title"});
        index.waitForTask(index.updateSettings(settings).getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder().q("").sort(new String[] {"id:asc", "title:asc"}).build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertEquals(20, resGson.hits.length);
        assertEquals("155", resGson.hits[0].getId());
        assertEquals("The Dark Knight", resGson.hits[0].getTitle());
        assertEquals("671", resGson.hits[1].getId());
        assertEquals("Harry Potter and the Philosopher's Stone", resGson.hits[1].getTitle());
    }

    /** Test search matches */
    @Test
    public void testSearchMatches() throws Exception {
        String indexUid = "SearchMatches";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder().q("and").showMatchesPosition(true).build();
        Searchable searchResult = index.search(searchRequest);

        assertEquals(20, searchResult.getHits().size());
    }

    /** Test search page */
    @Test
    public void testSearchPage() throws Exception {
        String indexUid = "SearchOffset";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest = SearchRequest.builder().q("a").page(1).build();
        SearchResultPaginated searchResult = (SearchResultPaginated) index.search(searchRequest);

        assertEquals(20, searchResult.getHits().size());
        assertEquals(1, searchResult.getPage());
        assertEquals(20, searchResult.getHitsPerPage());
        assertEquals(30, searchResult.getTotalHits());
        assertEquals(2, searchResult.getTotalPages());
    }

    /** Test search pagination */
    @Test
    public void testSearchPagination() throws Exception {
        String indexUid = "SearchOffset";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest = SearchRequest.builder().q("a").page(2).hitsPerPage(2).build();
        SearchResultPaginated searchResult = (SearchResultPaginated) index.search(searchRequest);

        assertEquals(2, searchResult.getHits().size());
        assertEquals(2, searchResult.getPage());
        assertEquals(2, searchResult.getHitsPerPage());
        assertEquals(30, searchResult.getTotalHits());
        assertEquals(15, searchResult.getTotalPages());
    }

    /** Test place holder search */
    @Test
    public void testPlaceHolder() throws Exception {
        String indexUid = "placeHolder";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        SearchResult result = (SearchResult) index.search("");

        assertEquals(20, result.getLimit());
    }

    /** Test place holder search */
    @Test
    public void testPlaceHolderWithLimit() throws Exception {
        String indexUid = "placeHolderWithLimit";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        Searchable searchResult = index.search(SearchRequest.builder().q(null).limit(10).build());

        assertEquals(10, searchResult.getHits().size());
    }
}
