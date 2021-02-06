package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.api.documents.DocumentHandler;
import com.meilisearch.sdk.api.documents.SearchRequest;
import com.meilisearch.sdk.api.documents.SearchResponse;
import com.meilisearch.sdk.api.documents.Update;
import com.meilisearch.sdk.api.index.Index;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.utils.Movie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
		if (testData == null)
			testData = this.getTestData(MOVIES_INDEX, Movie.class);
	}

	@AfterAll
	static void cleanMeiliSearch() {
		cleanup();
	}

	// TODO: Real Search tests after search refactor

	/**
	 * Test basic search
	 */
	@Test
	public void testBasicSearch() throws Exception {
		String indexUid = "BasicSearch";
		Index index = client.index().createIndex(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		SearchResponse<Movie> res = movies.search("batman");
		assertEquals(1, res.getHits().size());
		assertEquals(0, res.getOffset());
		assertEquals(20, res.getLimit());
		assertFalse(res.isExhaustiveNbHits());
		assertEquals(1, res.getNbHits());
		assertNotEquals(0, res.getProcessingTimeMs());
		assertEquals("batman", res.getQuery());
	}

	/**
	 * Test search offset
	 */
	@Test
	public void testSearchOffset() throws Exception {
		String indexUid = "SearchOffset";
		Index index = client.index().createIndex(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		SearchRequest searchRequest = new SearchRequest("a").setOffset(20);
		SearchResponse<Movie> search = movies.search(searchRequest);
		assertEquals(10, search.getHits().size());
		assertEquals(30, search.getNbHits());
	}

	/**
	 * Test search limit
	 */
	@Test
	public void testSearchLimit() throws Exception {
		String indexUid = "SearchLimit";
		Index index = client.index().createIndex(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		SearchRequest searchRequest = new SearchRequest("a").setLimit(2);
		SearchResponse<Movie> search = movies.search(searchRequest);
		assertEquals(2, search.getHits().size());
		assertEquals(30, search.getNbHits());
	}

	/**
	 * Test search attributesToRetrieve
	 */
	@Test
	public void testSearchAttributesToRetrieve() throws Exception {
		String indexUid = "SearchAttributesToRetrieve";
		Index index = client.index().createIndex(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		SearchRequest searchRequest = new SearchRequest("a")
			.setAttributesToRetrieve(Stream.of("id", "title").collect(Collectors.toList()));
		SearchResponse<Movie> search = movies.search(searchRequest);
		assertEquals(20, search.getHits().size());
		assertThat(search.getHits().get(0).getId(), instanceOf(String.class));
		assertThat(search.getHits().get(0).getTitle(), instanceOf(String.class));
		assertNull(search.getHits().get(0).getPoster());
		assertNull(search.getHits().get(0).getOverview());
		assertNull(search.getHits().get(0).getRelease_date());
		assertNull(search.getHits().get(0).getLanguage());
		assertNull(search.getHits().get(0).getGenres());
	}

	/**
	 * Test search crop
	 */
	@Test
	public void testSearchCrop() throws Exception {
		String indexUid = "SearchCrop";
		Index index = client.index().createIndex(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		SearchRequest searchRequest = new SearchRequest("and")
			.setAttributesToCrop(Collections.singletonList("overview"))
			.setCropLength(5);
		SearchResponse<Movie> search = movies.search(searchRequest);
		assertEquals(20, search.getHits().size());
		assertEquals("aunt and uncle", search.getHits().get(0).getFormatted().getOverview());
	}

	/**
	 * Test search highlight
	 */
	@Test
	public void testSearchHighlight() throws Exception {
		String indexUid = "SearchHighlight";
		Index index = client.index().createIndex(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		SearchRequest searchRequest = new SearchRequest("and")
			.setAttributesToHighlight(Collections.singletonList("overview"));
		SearchResponse<Movie> search = movies.search(searchRequest);
		assertEquals(20, search.getHits().size());
		assertTrue(search.getHits().get(0).getFormatted().getOverview().contains("<em>"));
		assertTrue(search.getHits().get(0).getFormatted().getOverview().contains("</em>"));
	}

	/**
	 * Test search filters
	 */
	@Test
	public void testSearchFilters() throws Exception {
		String indexUid = "SearchFilters";
		Index index = client.index().createIndex(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		SearchRequest searchRequest = new SearchRequest("and")
			.setFilters("title = \"The Dark Knight\"");
		SearchResponse<Movie> search = movies.search(searchRequest);
		assertEquals(1, search.getHits().size());
		assertEquals("155", search.getHits().get(0).getId());
		assertEquals("The Dark Knight", search.getHits().get(0).getTitle());
	}

	/**
	 * Test search filters complex
	 */
	@Test
	public void testSearchFiltersComplex() throws Exception {
		String indexUid = "SearchFiltersComplex";
		Index index = client.index().createIndex(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		SearchRequest searchRequest = new SearchRequest("and")
			.setFilters("title = \"The Dark Knight\" OR id = 290859");
		SearchResponse<Movie> search = movies.search(searchRequest);
		assertEquals(2, search.getHits().size());
		assertEquals("155", search.getHits().get(0).getId());
		assertEquals("290859", search.getHits().get(1).getId());
	}

	/**
	 * Test search matches
	 */
	@Test
	public void testSearchMatches() throws Exception {
		String indexUid = "SearchMatches";
		Index index = client.index().createIndex(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		SearchRequest searchRequest = new SearchRequest("and")
			.setMatches(true);
		SearchResponse<Movie> search = movies.search(searchRequest);

		assertEquals(20, search.getHits().size());
		assertEquals(52, search.getHits().get(0).getMatchesInfo().get("overview").get(0).start);
		assertEquals(3, search.getHits().get(0).getMatchesInfo().get("overview").get(0).length);
		assertEquals(214, search.getHits().get(0).getMatchesInfo().get("overview").get(1).start);
		assertEquals(3, search.getHits().get(0).getMatchesInfo().get("overview").get(1).length);
		assertEquals(375, search.getHits().get(0).getMatchesInfo().get("overview").get(2).start);
		assertEquals(3, search.getHits().get(0).getMatchesInfo().get("overview").get(2).length);
	}

	/**
	 * Test place holder search
	 */
	@Test
	public void testPlaceHolder() throws Exception {
		String indexUid = "placeHolder";
		Index index = client.index().createIndex(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		SearchResponse<Movie> emptySearch = movies.search("");
		SearchResponse<Movie> nullSearch = movies.search(new SearchRequest(null));
		assertThat(emptySearch.getHits().size(), equalTo(nullSearch.getHits().size()));
		assertEquals(20, nullSearch.getHits().size());
	}

	/**
	 * Test place holder search
	 */
	@Test
	public void testPlaceHolderWithLimit() throws Exception {
		String indexUid = "BasicSearch";
		Index index = client.index().getOrCreateIndex(indexUid, "");
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		SearchResponse<Movie> search = movies.search(new SearchRequest(null).setLimit(10));
		assertEquals(10, search.getHits().size());
	}
}
