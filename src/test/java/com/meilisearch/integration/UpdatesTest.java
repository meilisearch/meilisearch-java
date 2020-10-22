package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.UpdateStatus;
import com.meilisearch.sdk.utils.Movie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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
		Index index = client.createIndex(indexUid);

		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments(this.testData.getRaw()),
			UpdateStatus.class
		);

		UpdateStatus updateStatus =	index.getUpdate(updateInfo.getUpdateId());
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
		Index index = client.createIndex(indexUid);

		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments(this.testData.getRaw()),
			UpdateStatus.class
		);
		updateInfo = this.gson.fromJson(
			index.addDocuments(this.testData.getRaw()),
			UpdateStatus.class
		);

		UpdateStatus[] updateStatus = index.getUpdates();
		assertTrue(updateStatus.length == 2);
	}

	/**
	 * Test waitForPendingUpdate
	 */
	@Test
	public void testWaitForPendingUpdate() throws Exception {
		String indexUid = "WaitForPendingUpdate";
		Index index = client.createIndex(indexUid);

		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments(this.testData.getRaw()),
			UpdateStatus.class
		);
		index.waitForPendingUpdate(updateInfo.getUpdateId());

		UpdateStatus updateStatus = index.getUpdate(updateInfo.getUpdateId());

		assertEquals("processed", updateStatus.getStatus());

		client.deleteIndex(index.getUid());
	}

	/**
	 * Test waitForPendingUpdate timeoutInMs
	 */
	@Test
	public void testWaitForPendingUpdateTimoutInMs() throws Exception {
		String indexUid = "WaitForPendingUpdateTimoutInMs";
		Index index = client.createIndex(indexUid);

		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments(this.testData.getRaw()),
			UpdateStatus.class
		);
		index.waitForPendingUpdate(updateInfo.getUpdateId());

		assertThrows(
			Exception.class,
			() -> index.waitForPendingUpdate(updateInfo.getUpdateId(), 0, 50)
		);

		client.deleteIndex(index.getUid());
	}

	
}
