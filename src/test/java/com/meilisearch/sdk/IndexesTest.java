package com.meilisearch.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class IndexesTest {

	Client ms;
	Gson gson = new Gson();
	String primaryKey = "id";

	@BeforeEach
	public void initializeClient() throws Exception {
		ms = new Client(new Config("http://localhost:7700", "masterKey"));
	}

	@AfterAll
	static void cleanMeiliSearch()  throws Exception {
		Client ms = new Client(new Config("http://localhost:7700", "masterKey"));
		Index[] indexes = ms.getIndexList();
		for (int i = 0; i < indexes.length; i++) {
			ms.deleteIndex(indexes[i].uid);
		}
	}

	/**
	 * Test Index creation without PrimaryKey
	 */
	@Test
	public void testCreateIndexWithoutPrimaryKey() throws Exception {
		String indexUid = "IndexesTest";
		ms.createIndex(indexUid);
		Index index = ms.getIndex(indexUid);
		assertEquals(index.uid, indexUid);
		assertEquals(index.primaryKey, null);
		ms.deleteIndex(index.uid);
	}

	/**
	 * Test Index creation with PrimaryKey
	 */
	@Test
	public void testCreateIndexWithPrimaryKey() throws Exception {
		String indexUid = "IndexesTest";
		ms.createIndex(indexUid, this.primaryKey);
		Index index = ms.getIndex(indexUid);
		assertEquals(index.uid, indexUid);
		assertEquals(index.primaryKey, this.primaryKey);
		ms.deleteIndex(index.uid);
	}

	/**
	 * Test update Index PrimaryKey
	 */
	@Test
	public void testUpdateIndexPrimaryKey() throws Exception {
		String indexUid = "IndexesTest";
		ms.createIndex(indexUid);
		Index index = ms.getIndex(indexUid);
		assertEquals(index.uid, indexUid);
		assertEquals(index.primaryKey, null);
		ms.updateIndex(indexUid, this.primaryKey);
		index = ms.getIndex(indexUid);
		assertEquals(index.uid, indexUid);
		assertEquals(index.primaryKey, this.primaryKey);
		ms.deleteIndex(index.uid);
	}

	/**
	 * Test getIndexList
	 */
	@Test
	public void testGetIndexList() throws Exception {
		String[] indexUids = {"IndexesTest", "IndexesTest2"};
		ms.createIndex(indexUids[0]);
		ms.createIndex(indexUids[1], this.primaryKey);
		Index index1 = ms.getIndex(indexUids[0]);
		Index index2 = ms.getIndex(indexUids[1]);
		Index[] indexes = ms.getIndexList();
		assertEquals(indexes.length, 2);
		assert(Arrays.stream(indexUids).anyMatch(indexUids[0]::equals));
		assert(Arrays.stream(indexUids).anyMatch(indexUids[1]::equals));
		ms.deleteIndex(indexUids[0]);
		ms.deleteIndex(indexUids[1]);
	}	
}
