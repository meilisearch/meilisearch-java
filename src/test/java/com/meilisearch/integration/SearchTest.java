package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.UpdateStatus;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.SearchResult;
import com.meilisearch.sdk.utils.Movie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.instanceOf;

import static org.hamcrest.MatcherAssert.assertThat;

@Tag("integration")
public class SearchTest extends AbstractIT {

	private TestData<Movie> testData;

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
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());

		SearchResult searchResult = index.search("batman");

		System.out.println(searchResult.getFacetsDistribution());
		assertEquals(1, searchResult.getHits().size());
		assertEquals(0, searchResult.getOffset());
		assertEquals(20, searchResult.getLimit());
		assertEquals(1, searchResult.getNbHits());
	}

	/**
	 * Test search offset
	 */
	@Test
	public void testSearchOffset() throws Exception {
		String indexUid = "SearchOffset";
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());

		SearchRequest searchRequest = new SearchRequest("a").setOffset(20);
		SearchResult searchResult = index.search(searchRequest);

		assertEquals(10, searchResult.getHits().size());
		assertEquals(30, searchResult.getNbHits());
	}

	/**
	 * Test search limit
	 */
	@Test
	public void testSearchLimit() throws Exception {
		String indexUid = "SearchLimit";
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());

		SearchRequest searchRequest = new SearchRequest("a").setLimit(2);
		SearchResult searchResult = index.search(searchRequest);

		assertEquals(2, searchResult.getHits().size());
		assertEquals(30, searchResult.getNbHits());
	}

	/**
	 * Test search attributesToRetrieve
	 */
	@Test
	public void testSearchAttributesToRetrieve() throws Exception {
		String indexUid = "SearchAttributesToRetrieve";
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());

		SearchRequest searchRequest = new SearchRequest("a")
			.setAttributesToRetrieve(new String[]{"id", "title"});

		SearchResult searchResult = index.search(searchRequest);

		assertEquals(true, searchResult.getHits().size() > 0);
		assertThat(searchResult.getHits().get(0).get("id"), instanceOf(Double.class));
		assertThat(searchResult.getHits().get(0).get("title"), instanceOf(String.class));
	}

	/**
	 * Test search crop
	 */
	@Test
	public void testSearchCrop() throws Exception {
		String indexUid = "SearchCrop";
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());

		SearchRequest searchRequest = new SearchRequest("and")
			.setAttributesToCrop(new String[]{"overview"})
			.setCropLength(5);

		SearchResult searchResult = index.search(searchRequest);

		assertEquals(20, searchResult.getHits().size());
	}

	/**
	 * Test search highlight
	 */
	@Test
	public void testSearchHighlight() throws Exception {
		String indexUid = "SearchHighlight";
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());

		SearchRequest searchRequest = new SearchRequest("and")
			.setAttributesToHighlight(new String[]{"overview"});

		SearchResult searchResult = index.search(searchRequest);

		assertEquals(20, searchResult.getHits().size());
	}

	/**
	 * Test search filters
	 */
	@Test
	public void testSearchFilters() throws Exception {
		String indexUid = "SearchFilters";
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());

		SearchRequest searchRequest = new SearchRequest("and")
			.setFilters("title = \"The Dark Knight\"");
		SearchResult searchResult = index.search(searchRequest);

		assertEquals(1, searchResult.getHits().size());
		assertEquals(155.0, searchResult.getHits().get(0).get("id"));
		assertEquals("The Dark Knight", searchResult.getHits().get(0).get("title"));
	}

	/**
	 * Test search filters complex
	 */
	@Test
	public void testSearchFiltersComplex() throws Exception {
		String indexUid = "SearchFiltersComplex";
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());

		SearchRequest searchRequest = new SearchRequest("and")
			.setFilters("title = \"The Dark Knight\" OR id = 290859");
		SearchResult searchResult = index.search(searchRequest);

		assertEquals(2, searchResult.getHits().size());
		assertEquals(155.0, searchResult.getHits().get(0).get("id"));
		assertEquals(290859.0, searchResult.getHits().get(1).get("id"));
	}

	/**
	 * Test search matches
	 */
	@Test
	public void testSearchMatches() throws Exception {
		String indexUid = "SearchMatches";
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());

		SearchRequest searchRequest = new SearchRequest("and").setMatches(true);
		SearchResult searchResult = index.search(searchRequest);

		assertEquals(20, searchResult.getHits().size());
	}
	/**
	 * Test place holder search
	 */
	@Test
	public void testPlaceHolder() throws Exception {
		String indexUid = "placeHolder";
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());
		SearchResult result = index.search("");

		assertEquals(20, result.getLimit());
	}

	/**
	 * Test place holder search
	 */
	@Test
	public void testPlaceHolderWithLimit() throws Exception {
		String indexUid = "BasicSearch";
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());
		SearchResult searchResult = index.search(new SearchRequest(null).setLimit(10));

		assertEquals(10, searchResult.getHits().size());
	}
}
