package com.meilisearch.integration;

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

	}

	/**
	 * Test Get Updates
	 */
	@Test
	public void testGetUpdates() throws Exception {

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

		UpdateStatus updateStatus = this.gson.fromJson(
			index.getUpdate(updateInfo.getUpdateId()),
			UpdateStatus.class
		);

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

		assertThrows(
			Exception.class,
			() -> index.waitForPendingUpdate(updateInfo.getUpdateId(), 0, 50)
		);

		client.deleteIndex(index.getUid());
	}

	
}
