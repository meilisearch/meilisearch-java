package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.UpdateStatus;
import com.meilisearch.sdk.utils.Movie;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import com.meilisearch.sdk.exceptions.MeiliSearchApiException;

@Tag("integration")
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

		String indexUid = "AddDocumentsSingle";
		Index index = client.createIndex(indexUid);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		String singleDocument = this.gson.toJson(testData.getData().get(0));
		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments("[" + singleDocument + "]"),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());
		Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);

		assertEquals(1, movies.length);
		assertEquals("419704", movies[0].getId());
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

		String indexUid = "AddDocumentsMultiple";
		Index index = client.createIndex(indexUid);

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

	/**
	 * Test GetDocument
	 */
	@Test
	public void testGetDocument() throws Exception {

		String indexUid = "GetDocument";
		Index index = client.createIndex(indexUid);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());
		Movie movie = this.gson.fromJson(
			index.getDocument(testData.getData().get(0).getId()),
			Movie.class
		);
		assertEquals(movie.getTitle(), testData.getData().get(0).getTitle());
	}

	/**
	 * Test default GetDocuments
	 */
	@Test
	public void testGetDocuments() throws Exception {

		String indexUid = "GetDocuments";
		Index index = client.createIndex(indexUid);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());
		Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
		assertEquals(20, movies.length);
		for (int i = 0; i < movies.length; i++) {
			assertEquals(movies[i].getTitle(), testData.getData().get(i).getTitle());
			assertEquals(movies[i].getId(), testData.getData().get(i).getId());
			String[] expectedGenres = testData.getData().get(i).getGenres();
			String[] foundGenres = movies[i].getGenres();
			for (int x = 0; x < expectedGenres.length; x++) {
				assertEquals(expectedGenres[x], foundGenres[x]);
			}
		}
	}

	/**
	 * Test GetDocuments with limit
	 */
	@Test
	public void testGetDocumentsLimit() throws Exception {

		String indexUid = "GetDocumentsLimit";
		int limit = 24;
		client.createIndex(indexUid);
		Index index = client.getIndex(indexUid);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());
		Movie[] movies = this.gson.fromJson(index.getDocuments(limit), Movie[].class);
		assertEquals(limit, movies.length);
		for (int i = 0; i < movies.length; i++) {
			assertEquals(movies[i].getId(), testData.getData().get(i).getId());
		}
	}

	/**
	 * Test deleteDocument
	 */
	@Test
	public void testDeleteDocument() throws Exception {

		String indexUid = "DeleteDocument";
		client.createIndex(indexUid);
		Index index = client.getIndex(indexUid);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);
		index.waitForPendingUpdate(updateInfo.getUpdateId());

		Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
		Movie toDelete = movies[0];
		updateInfo = this.gson.fromJson(
			index.deleteDocument(toDelete.getId()),
			UpdateStatus.class
		);
		index.waitForPendingUpdate(updateInfo.getUpdateId());

		assertThrows(
			MeiliSearchApiException.class,
			() -> index.getDocument(toDelete.getId())
		);
	}

	/**
	 * Test deleteDocuments
	 */
	@Test
	public void testDeleteDocuments() throws Exception {

		String indexUid = "DeleteDocuments";
		client.createIndex(indexUid);
		Index index = client.getIndex(indexUid);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);
		index.waitForPendingUpdate(updateInfo.getUpdateId());

		Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
		assertEquals(20, movies.length);

		List<String> identifiersToDelete =	getIdentifiersToDelete(movies);

		updateInfo = this.gson.fromJson(
			index.deleteDocuments(identifiersToDelete),
			UpdateStatus.class
		);
		index.waitForPendingUpdate(updateInfo.getUpdateId());

		movies = this.gson.fromJson(index.getDocuments(), Movie[].class);

		boolean containsDeletedMovie =
			Arrays.stream(movies)
				.anyMatch(movie -> identifiersToDelete.contains(movie.getId()));

		assertFalse(containsDeletedMovie);
	}

	@NotNull
	private List<String> getIdentifiersToDelete(Movie[] movies) {
		return Arrays.asList(
			movies[1].getId(),
			movies[4].getId(),
			movies[10].getId(),
			movies[16].getId());
	}

	/**
	 * Test deleteAllDocuments
	 */
	@Test
	public void testDeleteAllDocuments() throws Exception {
		String indexUid = "DeleteAllDocuments";
		client.createIndex(indexUid);
		Index index = client.getIndex(indexUid);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);
		index.waitForPendingUpdate(updateInfo.getUpdateId());

		Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
		assertEquals(20, movies.length);

		updateInfo = this.gson.fromJson(
			index.deleteAllDocuments(),
			UpdateStatus.class
		);
		index.waitForPendingUpdate(updateInfo.getUpdateId());

		movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
		assertEquals(0, movies.length);
	}

}

