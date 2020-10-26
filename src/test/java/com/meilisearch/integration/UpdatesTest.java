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
		client.index().create(indexUid);
		DocumentHandler<Movie> documents = client.documents(indexUid, Movie.class);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = documents.addDocuments(testData.getData());

		UpdateStatus updateStatus = client.index().getUpdate(indexUid, updateInfo.getUpdateId());
		assertNotNull(updateStatus.getStatus());
		assertNotEquals("", updateStatus.getStatus());
		assertTrue(updateStatus.getUpdateId() >= 0);
	}

	/**
	 * Test Get Updates
	 */
	@Test
	public void testGetUpdates() throws Exception {
		String indexUid = "GetUpdates";
		client.index().create(indexUid);
		DocumentHandler<Movie> documents = client.documents(indexUid, Movie.class);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = documents.addDocuments(testData.getData());
		updateInfo = documents.addDocuments(testData.getData());

		List<UpdateStatus> updateStatus = client.index().getUpdates(indexUid);
		assertEquals(2, updateStatus.size());
	}

	/**
	 * Test waitForPendingUpdate
	 */
	@Test
	public void testWaitForPendingUpdate() throws Exception {
		String indexUid = "WaitForPendingUpdate";
		client.index().create(indexUid);
		DocumentHandler<Movie> documents = client.documents(indexUid, Movie.class);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = documents.addDocuments(testData.getData());

		client.index().waitForPendingUpdate(indexUid, updateInfo.getUpdateId());

		UpdateStatus updateStatus = client.index().getUpdate(indexUid, updateInfo.getUpdateId());

		assertEquals("processed", updateStatus.getStatus());

		client.index().delete(indexUid);
	}

	/**
	 * Test waitForPendingUpdate timeoutInMs
	 */
	@Test
	public void testWaitForPendingUpdateTimoutInMs() throws Exception {
		String indexUid = "WaitForPendingUpdateTimoutInMs";
		client.index().create(indexUid);
		DocumentHandler<Movie> documents = client.documents(indexUid, Movie.class);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = documents.addDocuments(testData.getData());

		client.index().waitForPendingUpdate(indexUid, updateInfo.getUpdateId());

		client.index().delete(indexUid);
	}
}
