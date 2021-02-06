package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.api.index.Index;
import com.meilisearch.sdk.api.instance.Dump;
import com.meilisearch.sdk.utils.Movie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;

@Tag("integration")
public class ClientTest extends AbstractIT {

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
		Index index = client.index().createIndex(indexUid);
		assertEquals(index.getUid(), indexUid);
		assertNull(index.getPrimaryKey());
		client.index().deleteIndex(index.getUid());
	}

	/**
	 * Test Index creation with PrimaryKey
	 */
	@Test
	public void testCreateIndexWithPrimaryKey() throws Exception {
		String indexUid = "CreateIndexWithPrimaryKey";
		Index index = client.index().createIndex(indexUid, this.primaryKey);
		assertEquals(index.getUid(), indexUid);
		assertEquals(index.getPrimaryKey(), this.primaryKey);
		client.index().deleteIndex(index.getUid());
	}

	@Test
	public void testCreateIndexWithPrimaryKeyIfIndexDoesNotExists() throws Exception {
		String indexUid = "dummyIndexUid";
		Index index = client.index().getOrCreateIndex(indexUid, this.primaryKey);
		assertEquals(index.getUid(), indexUid);
		assertEquals(index.getPrimaryKey(), this.primaryKey);
		client.index().deleteIndex(index.getUid());
	}

	@Test
	public void testGetIndexWithPrimaryKeyIfIndexAlreadyExists() throws Exception {
		String indexUid = "dummyIndexUid";
		Index createdIndex = client.index().getOrCreateIndex(indexUid, this.primaryKey);
		assertEquals(createdIndex.getUid(), indexUid);

		Index retrievedIndex = client.index().getOrCreateIndex(indexUid, this.primaryKey);
		assertEquals(retrievedIndex.getUid(), indexUid);
		assertEquals(createdIndex.getUid(), retrievedIndex.getUid());

		client.index().deleteIndex(createdIndex.getUid());
	}

	@Test
	public void testGetOrCreateIndexShouldNotThrowAnyException() throws Exception {
		String indexUid = "dummyIndexUid";
		Index createdIndex = null;
		try {
			createdIndex = client.index().getOrCreateIndex(indexUid, this.primaryKey);
			Index retrievedIndex = client.index().getOrCreateIndex(indexUid, this.primaryKey);
		} catch (Exception e) {
			client.index().deleteIndex(createdIndex.getUid());
			fail("Should Not Throw Any Exception");
		}
		client.index().deleteIndex(createdIndex.getUid());
	}

	/**
	 * Test Index creation error: already exists
	 */
	@Test
	public void testCreateIndexAlreadyExists() throws Exception {
		String indexUid = "CreateIndexAlreadyExists";
		Index index = client.index().createIndex(indexUid, this.primaryKey);
		assertEquals(index.getUid(), indexUid);
		assertEquals(index.getPrimaryKey(), this.primaryKey);
		assertThrows(
			MeiliSearchRuntimeException.class,
			() -> client.index().createIndex(indexUid, this.primaryKey)
		);
		client.index().deleteIndex(index.getUid());
	}

	/**
	 * Test update Index PrimaryKey
	 */
	@Test
	public void testUpdateIndexPrimaryKey() throws Exception {
		String indexUid = "UpdateIndexPrimaryKey";
		Index index = client.index().createIndex(indexUid);
		assertEquals(index.getUid(), indexUid);
		assertNull(index.getPrimaryKey());
		index = client.index().updateIndex(indexUid, this.primaryKey);
		assertTrue(index instanceof Index);
		assertEquals(index.getUid(), indexUid);
		assertEquals(index.getPrimaryKey(), this.primaryKey);
		client.index().deleteIndex(index.getUid());
	}

	/**
	 * Test getIndex
	 */
	@Test
	public void testGetIndex() throws Exception {
		String indexUid = "GetIndex";
		Index index = client.index().createIndex(indexUid);
		Index getIndex = client.index().getIndex(indexUid);
		assertEquals(index.getUid(), getIndex.getUid());
		assertEquals(index.getPrimaryKey(), getIndex.getPrimaryKey());
		client.index().deleteIndex(index.getUid());
	}

	/**
	 * Test getIndexList
	 */
	@Test
	public void testGetIndexList() throws Exception {
		String[] indexUids = {"GetIndexList", "GetIndexList2"};
		Index index1 = client.index().createIndex(indexUids[0]);
		Index index2 = client.index().createIndex(indexUids[1], this.primaryKey);
		Index[] indexes = client.index().getAllIndexes();
		assertEquals(2, indexes.length);
		assert (Arrays.asList(indexUids).contains(indexUids[0]));
		assert (Arrays.asList(indexUids).contains(indexUids[1]));
		client.index().deleteIndex(indexUids[0]);
		client.index().deleteIndex(indexUids[1]);
	}

	/**
	 * Test deleteIndex
	 */
	@Test
	public void testDeleteIndex() throws Exception {
		String indexUid = "DeleteIndex";
		Index index = client.index().createIndex(indexUid);
		client.index().deleteIndex(index.getUid());
		assertThrows(
			MeiliSearchRuntimeException.class,
			() -> client.index().getIndex(indexUid)
		);
	}

	/**
	 * Test call to index method with an inexistent index
	 */
	@Test
	public void testIndexMethodCallInexistentIndex() throws Exception {
		String indexUid = "IndexMethodCallInexistentIndex";
		assertThrows(
			MeiliSearchRuntimeException.class,
			() -> client.index().getIndex(indexUid)
		);
	}

	/**
	 * Test call to index method with an existing index
	 */
	@Test
	public void testIndexMethodCallExistingIndex() throws Exception {
		String indexUid = "IndexMethodCallExistingIndex";
		Index createdIndex = client.index().createIndex(indexUid);
		Index index = client.index().getIndex(indexUid);
		assertEquals(createdIndex.getUid(), index.getUid());
		assertEquals(null, index.getPrimaryKey());
	}

	/**
	 * Test call to index method with an existing index with primary key
	 */
	@Test
	public void testIndexMethodCallExistingIndexWithPrimaryKey() throws Exception {
		String indexUid = "IndexMethodCallExistingIndexWithPrimaryKey";
		String primaryKey = "PrimaryKey";
		Index createdIndex = client.index().createIndex(indexUid, primaryKey);
		Index index = client.index().getIndex(indexUid);
		assertEquals(createdIndex.getUid(), index.getUid());
		assertEquals(primaryKey, index.getPrimaryKey());
	}

	/**
	 * Test call to create dump
	 */
	@Test
	public void testCreateDump() throws Exception {
		Dump dump = client.instance().createDump();
		String status = dump.getStatus();
		assertEquals(status, "in_progress");
	}

	/**
	 * Test call to get dump status by uid
	 */
	@Test
	public void testGetDumpStatus() throws Exception {
		Dump dump = client.instance().createDump();
		String uid = dump.getUid();
		Dump status = client.instance().getDumpStatus(uid);
		assertNotNull(status);
		assertNotNull(uid);
	}
}
