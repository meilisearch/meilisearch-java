package com.meilisearch.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.json.JSONArray;
import org.json.JSONObject;

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
	
	/**
	 * Test waitForPendingUpdate
	 */
	@Test
	public void testWaitForPendingUpdate() throws Exception {
		String indexUid = "IndexesTest2";
		ms.createIndex(indexUid);
		Index index = ms.getIndex(indexUid);

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;

		jsonObject  = new JSONObject()
		.put("id", "1111")
		.put("title", "Alice in wonderland");
		jsonArray.put(jsonObject);

		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments(jsonArray.toString()), 
			UpdateStatus.class
		);

		index.waitForPendingUpdate(updateInfo.getUpdateId());

		UpdateStatus updateStatus = this.gson.fromJson(
			index.getUpdate(updateInfo.getUpdateId()), 
			UpdateStatus.class
		);

		assertEquals("processed", updateStatus.getStatus());

		ms.deleteIndex(index.uid);
	}

	/**
	 * Test waitForPendingUpdate timeoutInMs
	 */
	@Test
	public void testWaitForPendingUpdateTimoutInMs() throws Exception {
		String indexUid = "IndexesTest2";
		ms.createIndex(indexUid);
		Index index = ms.getIndex(indexUid);

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;

		jsonObject  = new JSONObject()
		.put("id", "1111")
		.put("title", "Alice in wonderland");
		jsonArray.put(jsonObject);

		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments(jsonArray.toString()), 
			UpdateStatus.class
		);

		assertThrows(
			Exception.class,
			() -> index.waitForPendingUpdate(updateInfo.getUpdateId(), 0, 50)
		);

		ms.deleteIndex(index.uid);
	}
}
