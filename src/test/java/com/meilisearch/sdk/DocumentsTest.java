package com.meilisearch.sdk;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.reflect.TypeToken;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DocumentsTest {
	
	Client ms;
	Gson gson = new Gson();
	TestUtils testUtils =  new TestUtils();

	@BeforeEach
	public void initialize() throws Exception {
		ms = new Client(new Config("http://localhost:7700", "masterKey"));
	}

	@AfterAll
	static void cleanMeiliSearch()  throws Exception {
		new TestUtils().deleteAllIndexes();
	}

	/**
	 * Test Add single document
	 */
	@Test
	public void testAddDocumentsSingle() throws Exception {

		String indexUid = "addSingleDocument";
		ms.createIndex(indexUid);
		Index index = ms.getIndex(indexUid);
		
		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments(this.testUtils.movies_data), 
			UpdateStatus.class
		);
		
		index.waitForPendingUpdate(updateInfo.getUpdateId());
		Movie[] movies = this.testUtils.moviesStringToJson(index.getDocuments());
		for (int i=0; i<movies.length; i++) {
			assertEquals(movies[i].title, this.testUtils.movies[i].title);
		}
	}

	/**
	 * Test Add multiple documents
	 */
	@Test
	public void testAddDocumentsMultiple() throws Exception {

		String indexUid = "addMultipleDocuments";
		ms.createIndex(indexUid);
		Index index = ms.getIndex(indexUid);

		UpdateStatus updateInfo = this.gson.fromJson(
			index.addDocuments(this.testUtils.movies_data), 
			UpdateStatus.class
		);
		
		index.waitForPendingUpdate(updateInfo.getUpdateId());
		Movie[] movies = this.testUtils.moviesStringToJson(index.getDocuments());
		for (int i=0; i<movies.length; i++) {
			assertEquals(movies[i].title, this.testUtils.movies[i].title);
		}
	}

}

