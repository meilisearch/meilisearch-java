package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.UpdateStatus;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.utils.Movie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.HashMap;
import com.google.gson.Gson;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.containsString;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());

		Results res_gson = jsonGson.decode(
			index.search("batman"), 
			Results.class
		);
		assertEquals(1, res_gson.hits.length);
		assertEquals(0, res_gson.offset);
		assertEquals(20, res_gson.limit);
		assertEquals(false, res_gson.exhaustiveNbHits);
		assertEquals(1, res_gson.nbHits);
		assertNotEquals(0, res_gson.processingTimeMs);
		assertEquals("batman", res_gson.query);
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
		Results res_gson = jsonGson.decode(
			index.search(searchRequest),
			Results.class
		);
		assertEquals(10, res_gson.hits.length);
		assertEquals(30, res_gson.nbHits);
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
		Results res_gson = jsonGson.decode(
			index.search(searchRequest),
			Results.class
		);
		assertEquals(2, res_gson.hits.length);
		assertEquals(30, res_gson.nbHits);
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
		Results res_gson = jsonGson.decode(
			index.search(searchRequest),
			Results.class
		);
		assertEquals(20, res_gson.hits.length);
		assertThat(res_gson.hits[0].getId(), instanceOf(String.class));
		assertThat(res_gson.hits[0].getTitle(), instanceOf(String.class));
		assertEquals(null, res_gson.hits[0].getPoster());
		assertEquals(null, res_gson.hits[0].getOverview());
		assertEquals(null, res_gson.hits[0].getRelease_date());
		assertEquals(null, res_gson.hits[0].getLanguage());
		assertEquals(null, res_gson.hits[0].getGenres());
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
			.setCropLength(5)
		;
		Results res_gson = jsonGson.decode(
			index.search(searchRequest),
			Results.class
		);
		assertEquals(20, res_gson.hits.length);
		assertEquals("aunt and uncle", res_gson.hits[0].getFormatted().getOverview());
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
		Results res_gson = jsonGson.decode(
			index.search(searchRequest),
			Results.class
		);
		assertEquals(20, res_gson.hits.length);
		assertTrue(res_gson.hits[0].getFormatted().getOverview().contains("<em>"));
		assertTrue(res_gson.hits[0].getFormatted().getOverview().contains("</em>"));
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
		Results res_gson = jsonGson.decode(
			index.search(searchRequest),
			Results.class
		);
		assertEquals(1, res_gson.hits.length);
		assertEquals("155", res_gson.hits[0].getId());
		assertEquals("The Dark Knight", res_gson.hits[0].getTitle());
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
		Results res_gson = jsonGson.decode(
			index.search(searchRequest),
			Results.class
		);
		assertEquals(2, res_gson.hits.length);
		assertEquals("155", res_gson.hits[0].getId());
		assertEquals("290859", res_gson.hits[1].getId());
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

		SearchRequest searchRequest = new SearchRequest("and")
			.setMatches(true);
		// Can't use GsonJsonHandler.decode, bug in deserialization of _matchesInfo
		String searchResult = index.search(searchRequest);
		Results res_gson = new Gson().fromJson(
			searchResult,
			Results.class
		);
		assertEquals(20, res_gson.hits.length);
		assertEquals(52, res_gson.hits[0].getMatchesInfo().get("overview").get(0).start);
		assertEquals(3, res_gson.hits[0].getMatchesInfo().get("overview").get(0).length);
		assertEquals(214, res_gson.hits[0].getMatchesInfo().get("overview").get(1).start);
		assertEquals(3, res_gson.hits[0].getMatchesInfo().get("overview").get(1).length);
		assertEquals(375, res_gson.hits[0].getMatchesInfo().get("overview").get(2).start);
		assertEquals(3, res_gson.hits[0].getMatchesInfo().get("overview").get(2).length);
	}
	
}
