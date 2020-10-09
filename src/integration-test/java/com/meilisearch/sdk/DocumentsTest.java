package com.meilisearch.sdk;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DocumentsTest extends AbstractIT {

	@BeforeEach
	public void initialize() {
		this.setUp();
	}

	@AfterAll
	static void cleanMeiliSearch() {
		cleanup();
	}

	/**
	 * Test Add single document
	 */
	@Test
	public void testAddDocumentsSingle() throws Exception {

		String indexUid = "addSingleDocument";
		client.createIndex(indexUid);
		Index index = client.getIndex(indexUid);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		String singleDocument = this.gson.toJson(testData.getData().get(0));
		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments("[" + singleDocument + "]"),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());
		Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);

		assertEquals(1, movies.length);
		assertEquals(419704, movies[0].getId());
		assertEquals("Ad Astra", movies[0].getTitle());
		assertEquals("https://image.tmdb.org/t/p/original/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg", movies[0].getPoster());
		assertEquals("The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown.", movies[0].getOverview());
		assertEquals("2019-09-17", movies[0].getRelease_date());
		assertEquals("en", movies[0].getLanguage());
		assertNotNull(movies[0].getGenres());
		assertEquals(2, movies[0].getGenres().length);
		assertEquals("Science Fiction", movies[0].getGenres()[0]);
		assertEquals("Drama", movies[0].getGenres()[1]);
	}

	/**
	 * Test Add multiple documents
	 */
	@Test
	public void testAddDocumentsMultiple() throws Exception {

		String indexUid = "addMultipleDocuments";
		client.createIndex(indexUid);
		Index index = client.getIndex(indexUid);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());
		Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
		for (int i = 0; i < movies.length; i++) {
			assertEquals(movies[i].getTitle(), testData.getData().get(i).getTitle());
		}
	}

}

