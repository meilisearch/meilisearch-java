package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.api.documents.DocumentHandler;
import com.meilisearch.sdk.api.documents.Update;
import com.meilisearch.sdk.api.index.Index;
import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;
import com.meilisearch.sdk.utils.Movie;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

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
		Index index = client.index().getOrCreateIndex(indexUid,"");

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		Movie singleDocument = testData.getData().get(0);
		DocumentHandler<Movie> movieHandler = client.documents("movies");
		Update update = movieHandler.addDocument(Collections.singletonList(singleDocument));
		movieHandler.waitForPendingUpdate(update.getUpdateId());
		Movie[] movies = movieHandler.getDocuments().toArray(new Movie[0]);

		assertEquals(20, movies.length);
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
		Index index = client.index().getOrCreateIndex(indexUid, "");

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movieHandler = client.documents("movies");
		Update update = movieHandler.addDocument(testData.getData());
		movieHandler.waitForPendingUpdate(update.getUpdateId());

		Movie[] movies = movieHandler.getDocuments().toArray(new Movie[0]);
		for (int i = 0; i < movies.length; i++) {
			assertEquals(movies[i].getTitle(), testData.getData().get(i).getTitle());
		}
	}

	/**
	 * Test Update a document
	 */
	@Test
	public void testUpdateDocument() throws Exception {

		String indexUid = "UpdateDocument";
		Index index = client.index().createIndex(indexUid);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movieHandler = client.documents("movies");
		Update update = movieHandler.addDocument(testData.getData());
		movieHandler.waitForPendingUpdate(update.getUpdateId());
		Movie[] movies = movieHandler.getDocuments().toArray(new Movie[0]);
		Movie toUpdate = movies[0];
		toUpdate.setTitle("The Perks of Being a Wallflower");
		toUpdate.setOverview("The best movie I've ever seen");

		update = movieHandler.updateDocuments(Collections.singletonList(toUpdate));
		movieHandler.waitForPendingUpdate(update.getUpdateId());
		Movie responseUpdate = movieHandler.getDocument(toUpdate.getId());

		assertEquals(toUpdate.getTitle(), responseUpdate.getTitle());
		assertEquals(toUpdate.getOverview(), responseUpdate.getOverview());
	}

	/**
	 * Test Update multiple documents
	 */
	@Test
	public void testUpdateDocuments() throws Exception {
		String indexUid = "UpdateDocuments";
		Index index = client.index().createIndex(indexUid);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movieHandler = client.documents("movies");
		Update update = movieHandler.addDocument(testData.getData());
		movieHandler.waitForPendingUpdate(update.getUpdateId());
		Movie[] movies = movieHandler.getDocuments().toArray(new Movie[0]);
		List<Movie> toUpdate = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			movies[i].setTitle("Star wars episode: " + i);
			movies[i].setOverview("This star wars movie is for the episode: " + i);
			toUpdate.add(movies[i]);
		}

		update = movieHandler.updateDocuments(toUpdate);


		movieHandler.waitForPendingUpdate(update.getUpdateId());
		for (int j = 0; j < 5; j++) {
			Movie responseUpdate = movieHandler.getDocument(toUpdate.get(j).getId());
			assertEquals(toUpdate.get(j).getTitle(), responseUpdate.getTitle());
			assertEquals(toUpdate.get(j).getOverview(), responseUpdate.getOverview());
		}
	}

	/**
	 * Test GetDocument
	 */
	@Test
	public void testGetDocument() throws Exception {

		String indexUid = "GetDocument";
		Index index = client.index().getOrCreateIndex(indexUid, "");

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movieHandler = client.documents("movies");
		Update update = movieHandler.addDocument(testData.getData());
		movieHandler.waitForPendingUpdate(update.getUpdateId());
		Movie movie = movieHandler.getDocument(testData.getData().get(0).getId());
		assertEquals(movie.getTitle(), testData.getData().get(0).getTitle());
	}

	/**
	 * Test default GetDocuments
	 */
	@Test
	public void testGetDocuments() throws Exception {

		String indexUid = "GetDocuments";
		Index index = client.index().getOrCreateIndex(indexUid, "");

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movieHandler = client.documents("movies");
		Update update = movieHandler.addDocument(testData.getData());
		movieHandler.waitForPendingUpdate(update.getUpdateId());
		Movie[] movies = movieHandler.getDocuments().toArray(new Movie[0]);
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
		Index index = client.index().getOrCreateIndex(indexUid, "");

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movieHandler = client.documents("movies");
		Update update = movieHandler.addDocument(testData.getData());
		movieHandler.waitForPendingUpdate(update.getUpdateId());
		Movie[] movies = movieHandler.getDocuments(limit).toArray(new Movie[0]);
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
		Index index = client.index().getOrCreateIndex(indexUid, "");

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movieHandler = client.documents("movies");
		Update update = movieHandler.addDocument(testData.getData());
		movieHandler.waitForPendingUpdate(update.getUpdateId());

		Movie[] movies = movieHandler.getDocuments().toArray(new Movie[0]);
		Movie toDelete = movies[0];

		update = movieHandler.deleteDocument(toDelete.getId());
		movieHandler.waitForPendingUpdate(update.getUpdateId());

		assertNotNull(toDelete);
		assertNotNull(toDelete.getId());
		assertThrows(
			MeiliSearchRuntimeException.class,
			() -> movieHandler.getDocument(toDelete.getId())
		);
	}

	/**
	 * Test deleteDocuments
	 */
	@Test
	public void testDeleteDocuments() throws Exception {

		String indexUid = "DeleteDocuments";
		Index index = client.index().getOrCreateIndex(indexUid, "");

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movieHandler = client.documents("movies");
		Update update = movieHandler.addDocument(testData.getData());
		movieHandler.waitForPendingUpdate(update.getUpdateId());

		Movie[] movies = movieHandler.getDocuments().toArray(new Movie[0]);
		assertEquals(20, movies.length);

		List<String> identifiersToDelete = getIdentifiersToDelete(movies);

		identifiersToDelete.forEach(s -> {
			try {
				Update u = movieHandler.deleteDocument(s);
				movieHandler.waitForPendingUpdate(u.getUpdateId());
			} catch (TimeoutException e) {
				fail(e);
			}
		});

		movies = movieHandler.getDocuments().toArray(new Movie[0]);

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
		Index index = client.index().getOrCreateIndex(indexUid, "");

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movieHandler = client.documents("movies");
		Update update = movieHandler.addDocument(testData.getData());
		movieHandler.waitForPendingUpdate(update.getUpdateId());

		Movie[] movies = movieHandler.getDocuments().toArray(new Movie[0]);
		assertEquals(20, movies.length);

		update = movieHandler.deleteDocuments();
		movieHandler.waitForPendingUpdate(update.getUpdateId());

		movies = movieHandler.getDocuments().toArray(new Movie[0]);
		assertEquals(0, movies.length);
	}

}

