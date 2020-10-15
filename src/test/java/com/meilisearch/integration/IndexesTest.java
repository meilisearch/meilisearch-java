package com.meilisearch.integration;

import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.UpdateStatus;
import com.meilisearch.sdk.utils.Movie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
public class IndexesTest extends AbstractIT {

	String primaryKey = "id";
	private TestData<Movie> testData;

	@BeforeEach
	public void initialize() {
		setUp();
		if (testData == null)
			testData = this.getTestData(MOVIES_INDEX, Movie.class);
	}

	@AfterAll
	static void cleanMeiliSearch() {
		cleanup();
	}

	/**
	 * Test Index creation without PrimaryKey
	 */
	@Test
	public void testCreateIndexWithoutPrimaryKey() throws Exception {
		String indexUid = "CreateIndexWithoutPrimaryKey";
		Index index = client.createIndex(indexUid);
		assertEquals(index.getUid(), indexUid);
		assertNull(index.getPrimaryKey());
		client.deleteIndex(index.getUid());
	}

	/**
	 * Test Index creation with PrimaryKey
	 */
	@Test
	public void testCreateIndexWithPrimaryKey() throws Exception {
		String indexUid = "CreateIndexWithPrimaryKey";
		Index index = client.createIndex(indexUid, this.primaryKey);
		assertEquals(index.getUid(), indexUid);
		assertEquals(index.getPrimaryKey(), this.primaryKey);
		client.deleteIndex(index.getUid());
	}

	/**
	 * Test update Index PrimaryKey
	 */
	@Test
	public void testUpdateIndexPrimaryKey() throws Exception {
		String indexUid = "UpdateIndexPrimaryKey";
		Index index = client.createIndex(indexUid);
		assertEquals(index.getUid(), indexUid);
		assertNull(index.getPrimaryKey());
		client.updateIndex(indexUid, this.primaryKey);
		index = client.getIndex(indexUid);
		assertEquals(index.getUid(), indexUid);
		assertEquals(index.getPrimaryKey(), this.primaryKey);
		client.deleteIndex(index.getUid());
	}

	/**
	 * Test getIndexList
	 */
	@Test
	public void testGetIndexList() throws Exception {
		String[] indexUids = {"GetIndexList", "GetIndexList2"};
		Index index1 = client.createIndex(indexUids[0]);
		Index index2 = client.createIndex(indexUids[1], this.primaryKey);
		Index[] indexes = client.getIndexList();
		assertEquals(2, indexes.length);
		assert (Arrays.asList(indexUids).contains(indexUids[0]));
		assert (Arrays.asList(indexUids).contains(indexUids[1]));
		client.deleteIndex(indexUids[0]);
		client.deleteIndex(indexUids[1]);
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

		assertThrows(
			Exception.class,
			() -> index.waitForPendingUpdate(updateInfo.getUpdateId(), 0, 50)
		);

		client.deleteIndex(index.getUid());
	}

}
