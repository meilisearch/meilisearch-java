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
		Index index = client.createIndex(indexUid);
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
		Index index = client.createIndex(indexUid);
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
		Index index = client.createIndex(indexUid);
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
	
}
