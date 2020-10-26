package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.api.documents.DocumentHandler;
import com.meilisearch.sdk.api.index.UpdateStatus;
import com.meilisearch.sdk.utils.Movie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
public class DocumentHandlerTest extends AbstractIT {

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
		client.index().create(indexUid);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		String singleDocument = this.gson.toJson(testData.getData().get(0));
		DocumentHandler<Movie> documents = client.documents(indexUid, Movie.class);
		UpdateStatus updateInfo = documents.addDocuments("[" + singleDocument + "]");

		client.index().waitForPendingUpdate(indexUid, updateInfo.getUpdateId());
		List<Movie> movies = documents.getDocuments();

		assertEquals(1, movies.size());
		assertEquals("419704", movies.get(0).getId());
		assertEquals("Ad Astra", movies.get(0).getTitle());
		assertEquals("https://image.tmdb.org/t/p/original/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg", movies.get(0).getPoster());
		assertEquals("The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown.", movies.get(0).getOverview());
		assertEquals("2019-09-17", movies.get(0).getRelease_date());
		assertEquals("en", movies.get(0).getLanguage());
		assertNotNull(movies.get(0).getGenres());
		assertEquals(2, movies.get(0).getGenres().length);
		assertEquals("Science Fiction", movies.get(0).getGenres()[0]);
		assertEquals("Drama", movies.get(0).getGenres()[1]);
	}

	/**
	 * Test Add multiple documents
	 */
	@Test
	public void testAddDocumentsMultiple() throws Exception {

		String indexUid = "AddDocumentsMultiple";
		client.index().create(indexUid);
		DocumentHandler<Movie> documents = client.documents(indexUid, Movie.class);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateStatus = documents.addDocuments(testData.getData());

		client.index().waitForPendingUpdate(indexUid, updateStatus.getUpdateId());
		List<Movie> movies = documents.getDocuments();
		for (int i = 0; i < movies.size(); i++) {
			assertEquals(movies.get(i).getTitle(), testData.getData().get(i).getTitle());
		}
	}

	/**
	 * Test GetDocument
	 */
	@Test
	public void testGetDocument() throws Exception {

		String indexUid = "GetDocument";
		client.index().create(indexUid);
		DocumentHandler<Movie> documents = client.documents(indexUid, Movie.class);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = documents.addDocuments(testData.getData());

		client.index().waitForPendingUpdate(indexUid, updateInfo.getUpdateId());

		Movie movie = documents.getDocument(testData.getData().get(0).getId());
		assertEquals(movie.getTitle(), testData.getData().get(0).getTitle());
	}

	/**
	 * Test default GetDocuments
	 */
	@Test
	public void testGetDocuments() throws Exception {

		String indexUid = "GetDocuments";
		client.index().create(indexUid);
		DocumentHandler<Movie> documents = client.documents(indexUid, Movie.class);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = documents.addDocuments(testData.getData());

		client.index().waitForPendingUpdate(indexUid, updateInfo.getUpdateId());
		List<Movie> movies = documents.getDocuments();
		assertEquals(20, movies.size());
		for (int i = 0; i < movies.size(); i++) {
			assertEquals(movies.get(i).getTitle(), testData.getData().get(i).getTitle());
			assertEquals(movies.get(i).getId(), testData.getData().get(i).getId());
			String[] expectedGenres = testData.getData().get(i).getGenres();
			String[] foundGenres = movies.get(i).getGenres();
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
		client.index().create(indexUid);
		DocumentHandler<Movie> documents = client.documents(indexUid, Movie.class);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = documents.addDocuments(testData.getData());

		client.index().waitForPendingUpdate(indexUid, updateInfo.getUpdateId());
		Map<String, String> params = new HashMap<>();
		params.put("limit", String.valueOf(limit));
		List<Movie> movies = documents.getDocuments(params);
		assertEquals(limit, movies.size());
		for (int i = 0; i < movies.size(); i++) {
			assertEquals(movies.get(i).getId(), testData.getData().get(i).getId());
		}
	}

	/**
	 * Test deleteDocument
	 */
	@Test
	public void testDeleteDocument() throws Exception {

		String indexUid = "DeleteDocument";
		client.index().create(indexUid);
		DocumentHandler<Movie> documents = client.documents(indexUid, Movie.class);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = documents.addDocuments(testData.getData());

		client.index().waitForPendingUpdate(indexUid, updateInfo.getUpdateId());

		List<Movie> movies = documents.getDocuments();
		Movie toDelete = movies.get(0);

		UpdateStatus updateStatus = documents.deleteDocument(toDelete.getId());
		client.index().waitForPendingUpdate(indexUid, updateStatus.getUpdateId());
		assertDoesNotThrow(() -> documents.getDocument(toDelete.getId()));
	}

	/**
	 * Test deleteDocuments
	 */
	@Test
	public void testDeleteDocuments() throws Exception {

		String indexUid = "DeleteDocuments";
		client.index().create(indexUid);
		DocumentHandler<Movie> documents = client.documents(indexUid, Movie.class);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = documents.addDocuments(testData.getData());

		client.index().waitForPendingUpdate(indexUid, updateInfo.getUpdateId());

		List<Movie> movies = documents.getDocuments();
		assertEquals(20, movies.size());

		updateInfo = assertDoesNotThrow(documents::deleteDocuments);
		client.index().waitForPendingUpdate(indexUid, updateInfo.getUpdateId());

		movies = documents.getDocuments();
		assertEquals(0, movies.size());
	}

}

