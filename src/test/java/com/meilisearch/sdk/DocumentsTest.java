package com.meilisearch.sdk;

import com.google.gson.Gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONArray;
import org.json.JSONObject;


public class DocumentsTest {
	
	Client ms;
	Index index;
	Gson gson = new Gson();
	
	String primaryKey = "id";

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
		this.index = ms.getIndex(indexUid);

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
		
		// TODO: Replace by WaitForPendingUpdate()
		String status = "";
		while (!status.equals("processed")){
			UpdateStatus updateStatus = this.gson.fromJson(
				index.getUpdate(updateInfo.getUpdateId()), 
				UpdateStatus.class
			);
			status = updateStatus.getStatus();
			Thread.sleep(20);
		}
		assertEquals(index.getDocuments(), jsonArray.toString());
	}

	/**
	 * Test Add multiple documents
	 */
	@Test
	public void testAddDocumentsMultiple() throws Exception {

		String indexUid = "addMultipleDocuments";
		ms.createIndex(indexUid);
		this.index = ms.getIndex(indexUid);

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
		
		// TODO: Replace by WaitForPendingUpdate()
		String status = "";
		while (!status.equals("processed")){
			UpdateStatus updateStatus = this.gson.fromJson(
				index.getUpdate(updateInfo.getUpdateId()), 
				UpdateStatus.class
			);
			status = updateStatus.getStatus();
			Thread.sleep(20);
		}
		assertEquals(index.getDocuments(), jsonArray.toString());
	}

}

