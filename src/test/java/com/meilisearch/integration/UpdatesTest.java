package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.api.documents.DocumentHandler;
import com.meilisearch.sdk.api.documents.Update;
import com.meilisearch.sdk.api.index.Index;
import com.meilisearch.sdk.utils.Movie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
public class UpdatesTest extends AbstractIT {

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

	/**
	 * Test Get Update
	 */
	@Test
	public void testGetUpdate() throws Exception {
		String indexUid = "GetUpdate";
		Index index = client.index().getOrCreateIndex(indexUid, "");

		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		update = movies.getUpdate(update.getUpdateId());
		assertNotNull(update.getStatus());
		assertNotEquals("", update.getStatus());
		assertTrue(update.getUpdateId() >= 0);
	}

	/**
	 * Test Get Updates
	 */
	@Test
	public void testGetUpdates() throws Exception {
		String indexUid = "GetUpdates";
		Index index = client.index().getOrCreateIndex(indexUid, "");

		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());
		update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());
		update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		List<Update> updates = movies.getUpdates();
		assertEquals(4, updates.size());
	}

	/**
	 * Test waitForPendingUpdate
	 */
	@Test
	public void testWaitForPendingUpdate() throws Exception {
		String indexUid = "WaitForPendingUpdate";
		Index index = client.index().getOrCreateIndex(indexUid, "");

		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());
		update = movies.getUpdate(update.getUpdateId());

		assertEquals("processed", update.getStatus());

		client.index().deleteIndex(index.getUid());
	}

	/**
	 * Test waitForPendingUpdate timeoutInMs
	 */
	@Test
	public void testWaitForPendingUpdateTimoutInMs() throws Exception {
		String indexUid = "WaitForPendingUpdateTimoutInMs";
		Index index = client.index().getOrCreateIndex(indexUid, "");

		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());

		assertThrows(
			Exception.class,
			() -> movies.waitForPendingUpdate(update.getUpdateId(), 0, 50)
		);

		client.index().deleteIndex(index.getUid());
	}
}
