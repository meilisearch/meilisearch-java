package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.api.index.Index;
import com.meilisearch.sdk.utils.Movie;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
		Index index = client.index().create(indexUid);
		assertEquals(index.getUid(), indexUid);
		assertNull(index.getPrimaryKey());
		client.index().delete(index.getUid());
	}

	/**
	 * Test Index creation with PrimaryKey
	 */
	@Test
	public void testCreateIndexWithPrimaryKey() throws Exception {
		String indexUid = "CreateIndexWithPrimaryKey";
		Index index = client.index().create(indexUid, this.primaryKey);
		assertEquals(index.getUid(), indexUid);
		assertEquals(index.getPrimaryKey(), this.primaryKey);
		client.index().delete(index.getUid());
	}

	/**
	 * Test update Index PrimaryKey
	 */
	@Test
	public void testUpdateIndexPrimaryKey() throws Exception {
		String indexUid = "UpdateIndexPrimaryKey";
		Index index = client.index().create(indexUid);
		assertEquals(index.getUid(), indexUid);
		assertNull(index.getPrimaryKey());
		client.index().updatePrimaryKey(indexUid, this.primaryKey);
		index = client.index().get(indexUid);
		assertEquals(index.getUid(), indexUid);
		assertEquals(index.getPrimaryKey(), this.primaryKey);
		client.index().delete(index.getUid());
	}

	/**
	 * Test getIndex
	 */
	@Test
	public void testGetIndex() throws Exception {
		String indexUid = "GetIndex";
		Index index = client.index().create(indexUid);
		Index getIndex = client.index().get(indexUid);
		assertEquals(index.getUid(), getIndex.getUid());
		assertEquals(index.getPrimaryKey(), getIndex.getPrimaryKey());
		client.index().delete(index.getUid());
	}

	/**
	 * Test getIndexList
	 */
	@Test
	public void testGetIndexList() throws Exception {
		List<String> indexUids = new ArrayList<String>() {{
			add("GetIndexList");
			add("GetIndexList2");
		}};
		Index index1 = client.index().create(indexUids.get(0));
		Index index2 = client.index().create(indexUids.get(1), this.primaryKey);
		List<Index> indexes = client.index().getAll();
		Set<String> collect = indexes.stream().map(Index::getUid).filter(indexUids::contains).collect(Collectors.toSet());
		assertThat(String.join(";", indexUids), Matchers.equalTo(String.join(";", collect)));
		client.index().delete(indexUids.get(0));
		client.index().delete(indexUids.get(1));
	}

	/**
	 * Test deleteIndex
	 */
	@Test
	public void testDeleteIndex() throws Exception {
		String indexUid = "DeleteIndex";
		Index index = client.index().create(indexUid);
		client.index().delete(index.getUid());
	}
}
