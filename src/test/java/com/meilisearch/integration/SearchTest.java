package com.meilisearch.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.IndexSearchRequest;
import com.meilisearch.sdk.MultiSearchRequest;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.model.MatchingStrategy;
import com.meilisearch.sdk.model.MultiSearchResult;
import com.meilisearch.sdk.model.SearchResult;
import com.meilisearch.sdk.model.SearchResultPaginated;
import com.meilisearch.sdk.model.Searchable;
import com.meilisearch.sdk.model.Settings;
import com.meilisearch.sdk.model.TaskInfo;
import com.meilisearch.sdk.utils.Movie;
import java.util.HashSet;
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

        assertThat(searchResult.getFacetDistribution(), is(nullValue()));
        assertThat(searchResult.getHits(), hasSize(1));
        assertThat(searchResult.getOffset(), is(equalTo(0)));
        assertThat(searchResult.getLimit(), is(equalTo(20)));
        assertThat(searchResult.getEstimatedTotalHits(), is(equalTo(1)));
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

        assertThat(searchResult.getHits(), hasSize(10));
        assertThat(searchResult.getEstimatedTotalHits(), is(equalTo(30)));
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

        assertThat(searchResult.getHits(), hasSize(2));
        assertThat(searchResult.getEstimatedTotalHits(), is(equalTo(30)));
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

        assertThat(resGson.hits, is(arrayWithSize(20)));
        assertThat(resGson.hits[0].getId(), instanceOf(String.class));
        assertThat(resGson.hits[0].getTitle(), instanceOf(String.class));
        assertThat(resGson.hits[0].getPoster(), is(nullValue()));
        assertThat(resGson.hits[0].getOverview(), is(nullValue()));
        assertThat(resGson.hits[0].getRelease_date(), is(nullValue()));
        assertThat(resGson.hits[0].getLanguage(), is(nullValue()));
        assertThat(resGson.hits[0].getGenres(), is(nullValue()));
    }

    @Test
    public void testAttributeToSearchOnSuccess() throws Exception {
        String indexUid = "SearchOnAttributesSuccess";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder()
                        .q("videogame")
                        .attributesToSearchOn(new String[] {"overview"})
                        .build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertThat(resGson.hits, is(arrayWithSize(1)));
        assertThat(resGson.hits[0].getOverview(), instanceOf(String.class));
    }

    @Test
    public void testAttributeToSearchOnNoResult() throws Exception {
        String indexUid = "SearchOnAttributesNoResult";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder()
                        .q("videogame")
                        .attributesToSearchOn(new String[] {"title"})
                        .build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertThat(resGson.hits, is(arrayWithSize(0)));
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

        assertThat(resGson.hits, is(arrayWithSize(20)));
        assertThat(resGson.hits[0].getFormatted().getOverview(), hasLength(5));
        assertThat(resGson.hits[0].getFormatted().getOverview(), is(equalTo("…and…")));
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

        assertThat(resGson.hits, is(arrayWithSize(20)));
        assertThat(resGson.hits[0].getFormatted().getOverview(), is(equalTo("(ꈍᴗꈍ)and(ꈍᴗꈍ)")));
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

        assertThat(resGson.hits, is(arrayWithSize(20)));
        assertThat(
                resGson.hits[0].getFormatted().getTitle(),
                is(
                        equalTo(
                                "Birds of Prey (<em>and</em> the Fantabulous Emancipation of One Harley Quinn)")));
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

        assertThat(resGson.hits, is(arrayWithSize(20)));
        assertThat(
                resGson.hits[0].getFormatted().getTitle(),
                is(
                        equalTo(
                                "Birds of Prey ((⊃｡•́‿•̀｡)⊃ and ⊂(´• ω •`⊂) the Fantabulous Emancipation of One Harley Quinn)")));
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

        assertThat(searchResult.getHits(), hasSize(20));
        assertThat(searchResult.getEstimatedTotalHits(), is(equalTo(21)));
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

        assertThat(resGson.hits, is(arrayWithSize(1)));
        assertThat(resGson.hits[0].getId(), is(equalTo("671")));
        assertThat(
                resGson.hits[0].getTitle(),
                is(equalTo("Harry Potter and the Philosopher's Stone")));
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

        assertThat(resGson.hits, is(arrayWithSize(1)));
        assertThat(resGson.hits[0].getId(), is(equalTo("155")));
        assertThat(resGson.hits[0].getTitle(), is(equalTo("The Dark Knight")));
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

        assertThat(resGson.hits, is(arrayWithSize(2)));
        assertThat(resGson.hits[0].getId(), is(equalTo("155")));
        assertThat(resGson.hits[1].getId(), is(equalTo("290859")));
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

        assertThat(searchResult.getHits(), hasSize(1));
        assertThat(searchResult.getFacetDistribution(), is(not(nullValue())));
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

        assertThat(resGson.hits, is(arrayWithSize(20)));
        assertThat(resGson.hits[0].getId(), is(equalTo("495764")));
        assertThat(
                resGson.hits[0].getTitle(),
                is(
                        equalTo(
                                "Birds of Prey (and the Fantabulous Emancipation of One Harley Quinn)")));
        assertThat(resGson.hits[1].getId(), is(equalTo("671")));
        assertThat(
                resGson.hits[1].getTitle(),
                is(equalTo("Harry Potter and the Philosopher's Stone")));
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

        assertThat(resGson.hits, is(arrayWithSize(20)));
        assertThat(resGson.hits[0].getId(), is(equalTo("671")));
        assertThat(
                resGson.hits[0].getTitle(),
                is(equalTo("Harry Potter and the Philosopher's Stone")));
        assertThat(resGson.hits[1].getId(), is(equalTo("495764")));
        assertThat(
                resGson.hits[1].getTitle(),
                is(
                        equalTo(
                                "Birds of Prey (and the Fantabulous Emancipation of One Harley Quinn)")));
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

        assertThat(resGson.hits, is(arrayWithSize(3)));
        assertThat(resGson.hits[0].getId(), is(equalTo("155")));
        assertThat(resGson.hits[0].getTitle(), is(equalTo("The Dark Knight")));
        assertThat(resGson.hits[1].getId(), is(equalTo("290859")));
        assertThat(resGson.hits[1].getTitle(), is(equalTo("Terminator: Dark Fate")));
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

        assertThat(resGson.hits, is(arrayWithSize(20)));
        assertThat(resGson.hits[0].getId(), is(equalTo("155")));
        assertThat(resGson.hits[0].getTitle(), is(equalTo("The Dark Knight")));
        assertThat(resGson.hits[1].getId(), is(equalTo("671")));
        assertThat(
                resGson.hits[1].getTitle(),
                is(equalTo("Harry Potter and the Philosopher's Stone")));
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

        assertThat(searchResult.getHits(), hasSize(20));
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

        assertThat(searchResult.getHits(), hasSize(20));
        assertThat(searchResult.getPage(), is(equalTo(1)));
        assertThat(searchResult.getHitsPerPage(), is(equalTo(20)));
        assertThat(searchResult.getTotalHits(), is(equalTo(30)));
        assertThat(searchResult.getTotalPages(), is(equalTo(2)));
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

        assertThat(searchResult.getHits(), hasSize(2));
        assertThat(searchResult.getPage(), is(equalTo(2)));
        assertThat(searchResult.getHitsPerPage(), is(equalTo(2)));
        assertThat(searchResult.getTotalHits(), is(equalTo(30)));
        assertThat(searchResult.getTotalPages(), is(equalTo(15)));
    }

    /** Test place holder search */
    @Test
    public void testPlaceHolder() throws Exception {
        String indexUid = "placeHolder";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        SearchResult result = index.search("");

        assertThat(result.getLimit(), is(equalTo(20)));
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

        assertThat(searchResult.getHits(), hasSize(10));
    }

    @Test
    public void testMultiSearch() throws Exception {
        HashSet<String> indexUids = new HashSet();
        indexUids.add("MultiSearch1");
        indexUids.add("MultiSearch2");

        for (String indexUid : indexUids) {
            Index index = client.index(indexUid);

            TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
            TaskInfo task = index.addDocuments(testData.getRaw());

            index.waitForTask(task.getTaskUid());
        }

        MultiSearchRequest search = new MultiSearchRequest();

        for (String indexUid : indexUids) {
            search.addQuery(new IndexSearchRequest(indexUid).setQuery("batman"));
        }

        MultiSearchResult[] results = client.multiSearch(search).getResults();

        assertThat(results.length, is(2));

        for (MultiSearchResult searchResult : results) {
            assertThat(indexUids.contains(searchResult.getIndexUid()), is(true));
            assertThat(searchResult.getFacetDistribution(), is(nullValue()));
            assertThat(searchResult.getHits(), hasSize(1));
            assertThat(searchResult.getOffset(), is(equalTo(0)));
            assertThat(searchResult.getLimit(), is(equalTo(20)));
            assertThat(searchResult.getEstimatedTotalHits(), is(equalTo(1)));
        }
    }
}
