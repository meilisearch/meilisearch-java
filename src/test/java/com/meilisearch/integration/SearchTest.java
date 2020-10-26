package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.api.documents.DocumentHandler;
import com.meilisearch.sdk.api.index.UpdateStatus;
import com.meilisearch.sdk.utils.Movie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("integration")
public class SearchTest extends AbstractIT {

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

	// TODO: Real Search tests after search refactor

	/**
	 * Test basic search
	 */
	@Test
	public void testSearch() throws Exception {
		String indexUid = "BasicSearch";
		client.index().create(indexUid);
		DocumentHandler<Movie> documents = client.documents(indexUid, Movie.class);

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = documents.addDocuments(testData.getData());

		client.index().waitForPendingUpdate(indexUid, updateInfo.getUpdateId());

		Movie s = documents.search(indexUid, "a");
		assertNotNull(s);
	}


}
