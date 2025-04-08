package com.meilisearch.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.*;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.model.*;
import com.meilisearch.sdk.utils.Movie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

        assertThat(searchResult.getHits(), hasSize(11));
        assertThat(searchResult.getEstimatedTotalHits(), is(equalTo(31)));
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
        assertThat(searchResult.getEstimatedTotalHits(), is(equalTo(31)));
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
                resGson.hits[3].getFormatted().getTitle(),
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
                resGson.hits[3].getFormatted().getTitle(),
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
        assertThat(searchResult.getEstimatedTotalHits(), is(equalTo(22)));
    }

    /** Test search with frequency matching strategy */
    @Test
    public void testSearchWithFrequencyMatchingStrategy() throws Exception {
        String indexUid = "SearchFrequencyMatchingStrategy";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder()
                        .q("white shirt")
                        .matchingStrategy(MatchingStrategy.FREQUENCY)
                        .build();

        SearchResult searchResult = (SearchResult) index.search(searchRequest);

        assertThat(searchResult.getHits(), hasSize(4));
    }

    /** Test search with show ranking score */
    @Test
    public void testSearchWithShowRankingScore() throws Exception {
        String indexUid = "SearchRankingScore";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder().q("and").showRankingScore(true).build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertThat(resGson.hits, is(arrayWithSize(20)));
        assertThat(resGson.hits[0].getRankingScore(), instanceOf(Double.class));
        assertThat(resGson.hits[0].getRankingScore(), is(greaterThanOrEqualTo(0.0)));
    }

    /** Test search with show ranking score details */
    @Test
    public void testSearchWithShowRankingScoreDetails() throws Exception {
        String indexUid = "SearchRankingScoreDetails";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder().q("and").showRankingScoreDetails(true).build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertThat(resGson.hits, is(arrayWithSize(20)));
        assertThat(resGson.hits[0].getRankingScoreDetails(), is(instanceOf(HashMap.class)));
        assertThat(resGson.hits[0].getRankingScoreDetails().get("words"), is(not(nullValue())));
        assertThat(resGson.hits[0].getRankingScoreDetails().get("typo"), is(not(nullValue())));
        assertThat(resGson.hits[0].getRankingScoreDetails().get("proximity"), is(not(nullValue())));
        assertThat(resGson.hits[0].getRankingScoreDetails().get("exactness"), is(not(nullValue())));
        assertThat(resGson.hits[0].getRankingScoreDetails().get("attribute"), is(not(nullValue())));
    }

    /** Test search with ranking score threshold */
    @Test
    public void testSearchWithRankingScoreThreshold() throws Exception {
        String indexUid = "SearchRankingScoreThreshold";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder()
                        .q("and")
                        .showRankingScore(true)
                        .rankingScoreThreshold(0.9)
                        .limit(20)
                        .build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertThat(resGson.hits, is(arrayWithSize(1)));
        assertThat(resGson.hits[0].getRankingScore(), instanceOf(Double.class));
        assertThat(resGson.hits[0].getRankingScore(), is(greaterThanOrEqualTo(0.9)));
    }

    /** Test with show distinct */
    @Test
    public void testSearchWithDistinct() throws Exception {
        String indexUid = "SearchDistinct";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        Settings settings = index.getSettings();

        settings.setFilterableAttributes(new String[] {"language"});
        index.waitForTask(index.updateSettings(settings).getTaskUid());

        SearchRequest searchRequest = SearchRequest.builder().q("").distinct("language").build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);
        assertThat(resGson.hits, is(arrayWithSize(4)));
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

    /** Test search facet stats */
    @Test
    public void testSearchFacetStats() throws Exception {
        String indexUid = "SearchFacetStats";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        Settings settings = index.getSettings();

        settings.setFilterableAttributes(new String[] {"title", "id"});
        index.waitForTask(index.updateSettings(settings).getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder().q("knight").facets(new String[] {"*"}).build();

        Searchable searchResult = index.search(searchRequest);

        assertThat(searchResult.getHits(), hasSize(1));
        assertThat(searchResult.getFacetStats(), is(instanceOf(HashMap.class)));
        assertThat(searchResult.getFacetStats().get("id"), is(instanceOf(FacetRating.class)));
        assertThat(searchResult.getFacetStats().get("id").getMin(), is(Double.valueOf(155.0)));
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
        assertThat(resGson.hits[0].getId(), is(equalTo("38700")));
        assertThat(resGson.hits[0].getTitle(), is(equalTo("Bad Boys for Life")));
        assertThat(resGson.hits[2].getId(), is(equalTo("495764")));
        assertThat(
                resGson.hits[2].getTitle(),
                is(
                        equalTo(
                                "Birds of Prey (and the Fantabulous Emancipation of One Harley Quinn)")));
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
        assertThat(resGson.hits[0].getId(), is(equalTo("38700")));
        assertThat(resGson.hits[0].getTitle(), is(equalTo("Bad Boys for Life")));
        assertThat(resGson.hits[1].getId(), is(equalTo("671")));
        assertThat(
                resGson.hits[1].getTitle(),
                is(equalTo("Harry Potter and the Philosopher's Stone")));
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
        assertThat(resGson.hits[0].getId(), is(equalTo("2")));
        assertThat(resGson.hits[0].getTitle(), is(equalTo("Hobbit")));
        assertThat(resGson.hits[1].getId(), is(equalTo("155")));
        assertThat(resGson.hits[1].getTitle(), is(equalTo("The Dark Knight")));
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
        assertThat(searchResult.getTotalHits(), is(equalTo(31)));
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
        assertThat(searchResult.getTotalHits(), is(equalTo(31)));
        assertThat(searchResult.getTotalPages(), is(equalTo(16)));
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

    /*
     * Test the federation parameter in multi search method
     */
    @Test
    public void testFederation() throws Exception {
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
        search.addQuery(new IndexSearchRequest("MultiSearch1").setQuery("batman"));
        search.addQuery(
                new IndexSearchRequest("MultiSearch2")
                        .setQuery("batman")
                        .setFederationOptions(new FederationOptions().setWeight(0.9)));

        MultiSearchFederation federation = new MultiSearchFederation();
        federation.setLimit(2);
        MultiSearchResult results = client.multiSearch(search, federation);

        assertThat(results.getEstimatedTotalHits(), is(2));
        assertThat(results.getLimit(), is(2));
        ArrayList<HashMap<String, Object>> hits = results.getHits();
        assertThat(hits, hasSize(2));
        for (HashMap<String, Object> record : hits) {
            assertThat(record.containsKey("_federation"), is(true));
        }
    }

    /** Test multisearch with ranking score threshold */
    @Test
    public void testMultiSearchWithRankingScoreThreshold() throws Exception {
        HashSet<String> indexUids = new HashSet<>();
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
            search.addQuery(
                    new IndexSearchRequest(indexUid)
                            .setQuery("batman")
                            .setShowRankingScore(true)
                            .setRankingScoreThreshold(0.98)
                            .setLimit(20));
        }

        MultiSearchResult[] results = client.multiSearch(search).getResults();

        assertThat(results.length, is(2));

        for (MultiSearchResult searchResult : results) {
            assertThat(indexUids.contains(searchResult.getIndexUid()), is(true));
            assertThat(
                    searchResult.getHits().get(0).get("_rankingScore"), instanceOf(Double.class));
            Double rankingScore = (Double) searchResult.getHits().get(0).get("_rankingScore");
            assertThat(rankingScore, is(greaterThanOrEqualTo(0.98)));
        }
    }

    /** Test multisearch with distinct */
    @Test
    public void testMultiSearchWithDistinct() throws Exception {
        HashSet<String> indexUids = new HashSet<String>();
        indexUids.add("MultiSearch1");
        indexUids.add("MultiSearch2");

        for (String indexUid : indexUids) {
            Index index = client.index(indexUid);

            TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
            TaskInfo task = index.addDocuments(testData.getRaw());

            Settings settings = new Settings();
            settings.setFilterableAttributes(new String[] {"language", "title"});

            index.waitForTask(index.updateSettings(settings).getTaskUid());

            index.waitForTask(task.getTaskUid());
        }

        MultiSearchRequest search = new MultiSearchRequest();

        for (String indexUid : indexUids) {
            search.addQuery(new IndexSearchRequest(indexUid).setQuery("").setDistinct("language"));
        }

        MultiSearchResult[] results = client.multiSearch(search).getResults();

        assertThat(results.length, is(2));

        for (MultiSearchResult searchResult : results) {
            assertThat(indexUids.contains(searchResult.getIndexUid()), is(true));
            assertThat(searchResult.getHits().size(), is(4));
        }
    }

    @Test
    public void testMultiSearchWithFacetsByIndex() {
        HashSet<String> indexUids = new HashSet<String>();
        indexUids.add("movies");
        indexUids.add("nestedMovies");

        for (String indexUid : indexUids) {

            Index index = client.index(indexUid);

            TestData<Movie> nestedTestData = this.getTestData(NESTED_MOVIES, Movie.class);
            TaskInfo task1 = index.addDocuments(nestedTestData.getRaw());

            index.waitForTask(task1.getTaskUid());

            Settings settings = new Settings();
            settings.setFilterableAttributes(new String[] {"id", "title"});
            settings.setSortableAttributes(new String[] {"id"});

            index.waitForTask(index.updateSettings(settings).getTaskUid());

            TestData<Movie> moviesTestData = this.getTestData(MOVIES_INDEX, Movie.class);
            TaskInfo task2 = index.addDocuments(moviesTestData.getRaw());

            index.waitForTask(task2.getTaskUid());
        }

        MultiSearchRequest search = new MultiSearchRequest();

        for (String indexUid : indexUids) {
            search.addQuery(new IndexSearchRequest(indexUid).setQuery("Hobbit"));
        }

        MultiSearchFederation federation = new MultiSearchFederation();
        federation.setLimit(20);
        federation.setOffset(0);
        Map<String, String[]> facetsByIndex = new HashMap<String, String[]>();
        facetsByIndex.put("nestedMovies", new String[] {"title"});
        facetsByIndex.put("movies", new String[] {"title", "id"});
        federation.setFacetsByIndex(facetsByIndex);

        MultiSearchResult results = client.multiSearch(search, federation);

        assertThat(results.getHits().size(), is(4));

        HashMap<String, FacetRating> facetStats = results.getFacetStats();
        HashMap<String, HashMap<String, Integer>> facetDistribution =
                results.getFacetDistribution();

        HashMap<String, FacetsByIndexInfo> facetsByIndexInfo = results.getFacetsByIndex();

        assertThat(facetDistribution, is(nullValue()));
        assertThat(facetStats, is(nullValue()));
        assertThat(facetsByIndexInfo, is(not(nullValue())));

        for (String indexUid : indexUids) {
            FacetsByIndexInfo indexInfo = facetsByIndexInfo.get(indexUid);
            assertThat(indexInfo.getDistribution(), is(not(nullValue())));
            assertThat(indexInfo.getStats(), is(not(nullValue())));
        }

        HashMap<String, HashMap<String, Integer>> moviesIndexDistribution =
                facetsByIndexInfo.get("movies").getDistribution();

        assertThat(moviesIndexDistribution.get("id"), is(not(nullValue())));
        assertThat(moviesIndexDistribution.get("id").get("2"), is(equalTo(1)));
        assertThat(moviesIndexDistribution.get("id").get("5"), is(equalTo(1)));
        assertThat(moviesIndexDistribution.get("title"), is(not(nullValue())));
        assertThat(moviesIndexDistribution.get("title").get("Hobbit"), is(equalTo(1)));
        assertThat(moviesIndexDistribution.get("title").get("The Hobbit"), is(equalTo(1)));

        HashMap<String, FacetRating> moviesFacetRating = facetsByIndexInfo.get("movies").getStats();
        FacetRating idMoviesFacetRating = moviesFacetRating.get("id");

        assertThat(idMoviesFacetRating, is(not(nullValue())));
        assertThat(idMoviesFacetRating.getMin(), is(equalTo(2.0)));
        assertThat(idMoviesFacetRating.getMax(), is(equalTo(5.0)));

        HashMap<String, HashMap<String, Integer>> nestedMoviesIndexDistribution =
                facetsByIndexInfo.get("nestedMovies").getDistribution();

        assertThat(nestedMoviesIndexDistribution.get("title"), is(not(nullValue())));
        assertThat(nestedMoviesIndexDistribution.get("title").get("Hobbit"), is(equalTo(1)));
        assertThat(nestedMoviesIndexDistribution.get("title").get("The Hobbit"), is(equalTo(1)));

        HashMap<String, FacetRating> nestedMoviesFacetRating =
                facetsByIndexInfo.get("nestedMovies").getStats();
        assertThat(nestedMoviesFacetRating.size(), is(equalTo((0))));
    }

    @Test
    public void testMultiSearchWithMergeFacets() {
        HashSet<String> indexUids = new HashSet<String>();
        indexUids.add("movies");
        indexUids.add("nestedMovies");

        for (String indexUid : indexUids) {

            Index index = client.index(indexUid);

            TestData<Movie> nestedTestData = this.getTestData(NESTED_MOVIES, Movie.class);
            TaskInfo task1 = index.addDocuments(nestedTestData.getRaw());

            index.waitForTask(task1.getTaskUid());

            Settings settings = new Settings();
            settings.setFilterableAttributes(new String[] {"id", "title"});
            settings.setSortableAttributes(new String[] {"id"});

            index.waitForTask(index.updateSettings(settings).getTaskUid());

            TestData<Movie> moviesTestData = this.getTestData(MOVIES_INDEX, Movie.class);
            TaskInfo task2 = index.addDocuments(moviesTestData.getRaw());

            index.waitForTask(task2.getTaskUid());
        }

        MultiSearchRequest search = new MultiSearchRequest();

        for (String indexUid : indexUids) {
            search.addQuery(new IndexSearchRequest(indexUid).setQuery("Hobbit"));
        }

        MultiSearchFederation federation = new MultiSearchFederation();
        federation.setLimit(20);
        federation.setOffset(0);
        federation.setMergeFacets(new MergeFacets(10));
        Map<String, String[]> facetsByIndex = new HashMap<String, String[]>();
        facetsByIndex.put("nestedMovies", new String[] {"title"});
        facetsByIndex.put("movies", new String[] {"title", "id"});
        federation.setFacetsByIndex(facetsByIndex);

        MultiSearchResult results = client.multiSearch(search, federation);

        assertThat(results.getHits().size(), is(4));

        HashMap<String, FacetRating> facetStats = results.getFacetStats();
        HashMap<String, HashMap<String, Integer>> facetDistribution =
                results.getFacetDistribution();

        assertThat(facetDistribution, is(not(nullValue())));
        assertThat(facetStats, is(not(nullValue())));
        assertThat(results.getFacetsByIndex(), is(nullValue()));

        FacetRating idFacet = facetStats.get("id");

        assertThat(idFacet.getMin(), is(equalTo(2.0)));
        assertThat(idFacet.getMax(), is(equalTo(5.0)));

        assertThat(facetDistribution.get("id").get("2"), is(equalTo(1)));
        assertThat(facetDistribution.get("id").get("5"), is(equalTo(1)));

        assertThat(facetDistribution.get("title").get("Hobbit"), is(equalTo(2)));
        assertThat(facetDistribution.get("title").get("The Hobbit"), is(equalTo(2)));
    }

    @Test
    public void testSimilarDocuments() throws Exception {
        String indexUid = "SimilarDocuments";
        Index index = client.index(indexUid);
        HashMap<String, Embedder> embedders = new HashMap<>();
        embedders.put(
                "manual", new Embedder().setSource(EmbedderSource.USER_PROVIDED).setDimensions(3));

        Settings settings = new Settings();
        settings.setEmbedders(embedders);

        index.updateSettings(settings);

        TestData<Movie> testData = this.getTestData(VECTOR_MOVIES, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SimilarDocumentsResults results =
                index.searchSimilarDocuments(
                        new SimilarDocumentRequest().setId("143").setEmbedder("manual"));

        ArrayList<HashMap<String, Object>> hits = results.getHits();
        assertThat(hits.size(), is(4));
        assertThat(hits.get(0).get("title"), is("Escape Room"));
        assertThat(hits.get(1).get("title"), is("Captain Marvel"));
        assertThat(hits.get(2).get("title"), is("How to Train Your Dragon: The Hidden World"));
        assertThat(hits.get(3).get("title"), is("Shazam!"));
    }

    /** Test vector search */
    @Test
    public void testVectorSearch() throws Exception {
        String indexUid = "testVectorSearch";
        Index index = client.index(indexUid);
        HashMap<String, Embedder> embedders = new HashMap<>();
        embedders.put(
                "manual", new Embedder().setSource(EmbedderSource.USER_PROVIDED).setDimensions(3));

        Settings settings = new Settings();
        settings.setEmbedders(embedders);

        index.updateSettings(settings);

        TestData<Movie> testData = this.getTestData(VECTOR_MOVIES, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder()
                        .vector(new Double[] {0.1, 0.6, 0.8})
                        .hybrid(Hybrid.builder().semanticRatio(0.5).embedder("manual").build())
                        .build();

        SearchResult searchResult = (SearchResult) index.search(searchRequest);

        assertThat(searchResult.getHits(), hasSize(5));
        // The most similar document should be "Escape Room" since its vector [0.1, 0.6, 0.8]
        assertThat(searchResult.getHits().get(0).get("id"), is("522681"));
        assertThat(searchResult.getHits().get(0).get("title"), is("Escape Room"));
    }

    /** Test vector search with retrieveVectors option */
    @Test
    public void testVectorSearchWithRetrieveVectors() throws Exception {
        String indexUid = "testVectorSearchWithRetrieveVectors";
        Index index = client.index(indexUid);
        HashMap<String, Embedder> embedders = new HashMap<>();
        embedders.put(
                "manual", new Embedder().setSource(EmbedderSource.USER_PROVIDED).setDimensions(3));

        Settings settings = new Settings();
        settings.setEmbedders(embedders);

        index.updateSettings(settings);

        TestData<Movie> testData = this.getTestData(VECTOR_MOVIES, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder()
                        .vector(new Double[] {0.1, 0.6, 0.8})
                        .hybrid(Hybrid.builder().semanticRatio(0.5).embedder("manual").build())
                        .retrieveVectors(true)
                        .build();

        SearchResult searchResult = (SearchResult) index.search(searchRequest);

        assertThat(searchResult.getHits(), hasSize(5));
        // The most similar document should be "Escape Room" since its vector [0.1, 0.6, 0.8]
        assertThat(searchResult.getHits().get(0).get("id"), is("522681"));
        assertThat(searchResult.getHits().get(0).get("title"), is("Escape Room"));

        // Verify that vectors are returned in the response
        Map<String, Object> escapeRoomHit = searchResult.getHits().get(0);
        assertThat(escapeRoomHit.containsKey("_vectors"), is(true));

        @SuppressWarnings("unchecked")
        Map<String, Object> vectors = (Map<String, Object>) escapeRoomHit.get("_vectors");
        assertThat(vectors.containsKey("manual"), is(true));
    }

    /** Test Search with locales */
    @Test
    public void testSearchWithLocales() throws Exception {
        String indexUid = "SearchLocales";
        Index index = client.index(indexUid);
        GsonJsonHandler jsonGson = new GsonJsonHandler();

        TestData<Movie> testData = this.getTestData(NESTED_MOVIES, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        Settings settings = index.getSettings();

        LocalizedAttribute localizedAttribute = new LocalizedAttribute();
        localizedAttribute.setAttributePatterns(new String[] {"title", "comment"});
        localizedAttribute.setLocales(new String[] {"fra", "eng"});
        settings.setLocalizedAttributes(new LocalizedAttribute[] {localizedAttribute});

        index.waitForTask(index.updateSettings(settings).getTaskUid());

        SearchRequest searchRequest =
                SearchRequest.builder().q("french").locales(new String[] {"fra", "eng"}).build();

        Results resGson = jsonGson.decode(index.rawSearch(searchRequest), Results.class);

        assertThat(resGson.hits, is(arrayWithSize(2)));
    }

    /** Test multisearch with locales */
    @Test
    public void testMultiSearchWithLocales() throws Exception {
        HashSet<String> indexUids = new HashSet<String>();
        indexUids.add("LocaleSearch1");
        indexUids.add("LocaleSearch2");

        for (String indexUid : indexUids) {
            Index index = client.index(indexUid);

            TestData<Movie> testData = this.getTestData(NESTED_MOVIES, Movie.class);
            TaskInfo task = index.addDocuments(testData.getRaw());

            index.waitForTask(task.getTaskUid());

            Settings settings = index.getSettings();

            LocalizedAttribute localizedAttribute = new LocalizedAttribute();
            localizedAttribute.setAttributePatterns(new String[] {"title", "comment"});
            localizedAttribute.setLocales(new String[] {"fra", "eng"});
            settings.setLocalizedAttributes(new LocalizedAttribute[] {localizedAttribute});

            index.waitForTask(index.updateSettings(settings).getTaskUid());
        }

        MultiSearchRequest search = new MultiSearchRequest();

        search.addQuery(
                new IndexSearchRequest("LocaleSearch1")
                        .setQuery("")
                        .setLocales(new String[] {"eng"}));
        search.addQuery(
                new IndexSearchRequest("LocaleSearch2")
                        .setQuery("french")
                        .setLocales(new String[] {"fra"}));

        MultiSearchResult[] results = client.multiSearch(search).getResults();

        assertThat(results.length, is(2));

        for (MultiSearchResult searchResult : results) {
            assertThat(indexUids.contains(searchResult.getIndexUid()), is(true));
        }
        assertThat(results[0].getHits().size(), is(7));
        assertThat(results[1].getHits().size(), is(2));
    }

    /** Test search with retrieveVectors parameter */
    @Test
    public void testSearchWithRetrieveVectors() throws Exception {
        String indexUid = "testSearchWithRetrieveVectors";
        Index index = client.index(indexUid);
        HashMap<String, Embedder> embedders = new HashMap<>();
        embedders.put(
                "manual", new Embedder().setSource(EmbedderSource.USER_PROVIDED).setDimensions(3));

        Settings settings = new Settings();
        settings.setEmbedders(embedders);

        index.updateSettings(settings);

        TestData<Movie> testData = this.getTestData(VECTOR_MOVIES, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());

        // First search without retrieveVectors
        SearchRequest searchRequestWithout = SearchRequest.builder().q("").build();
        SearchResult searchResultWithout = (SearchResult) index.search(searchRequestWithout);

        assertThat(searchResultWithout.getHits(), hasSize(5));
        Map<String, Object> hitWithout = searchResultWithout.getHits().get(0);
        assertThat(hitWithout.containsKey("_vectors"), is(false));

        // Then search with retrieveVectors
        SearchRequest searchRequestWith =
                SearchRequest.builder().q("").retrieveVectors(true).build();
        SearchResult searchResultWith = (SearchResult) index.search(searchRequestWith);

        assertThat(searchResultWith.getHits(), hasSize(5));
        Map<String, Object> hitWith = searchResultWith.getHits().get(0);
        assertThat(hitWith.containsKey("_vectors"), is(true));

        @SuppressWarnings("unchecked")
        Map<String, Object> vectors = (Map<String, Object>) hitWith.get("_vectors");
        assertThat(vectors.containsKey("manual"), is(true));
    }
}
