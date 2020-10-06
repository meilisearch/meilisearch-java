package com.meilisearch.sdk;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.json.JSONArray;
import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DocumentsTest {
	
	Client ms;
	Gson gson = new Gson();

	@BeforeEach
	public void initialize() throws Exception {
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
	 * Test Add single document
	 */
	@Test
	public void testAddDocumentsSingle() throws Exception {

		String indexUid = "addSingleDocument";
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
		assertEquals(index.getDocuments(), jsonArray.toString());
	}

	/**
	 * Test Add multiple documents
	 */
	@Test
	public void testAddDocumentsMultiple() throws Exception {

		String indexUid = "addMultipleDocuments";
		ms.createIndex(indexUid);
		Index index = ms.getIndex(indexUid);

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;

		jsonObject  = new JSONObject()
		.put("id", "1111")
		.put("title", "Alice in wonderland");
		jsonArray.put(jsonObject);

		jsonObject = new JSONObject()
		.put("id", "222")
		.put("title", "Blice in wonderland");
		jsonArray.put(jsonObject);

		jsonObject = new JSONObject()
		.put("id", "333")
		.put("title", "Clice in wonderland");
		jsonArray.put(jsonObject);

		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments(jsonArray.toString()), 
			UpdateStatus.class
		);
		
		index.waitForPendingUpdate(updateInfo.getUpdateId());
		assertEquals(index.getDocuments(), jsonArray.toString());
	}

}

