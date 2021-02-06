package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.api.documents.DocumentHandler;
import com.meilisearch.sdk.api.documents.Update;
import com.meilisearch.sdk.api.index.Index;
import com.meilisearch.sdk.api.index.Settings;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.utils.Movie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("integration")
public class SettingsTest extends AbstractIT {

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
	 * Test get settings from an index by uid
	 */
	@Test
	public void testGetSettings() throws Exception {
		String indexUid = "getSettings";
		Index index = client.index().getOrCreateIndex(indexUid, "");

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		Settings settings = client.index().getSettings(indexUid);

		assertEquals(6, settings.getRankingRules().length);
	}

	/**
	 * Test update settings changing the ranking rules
	 */
	@Test
	public void testUpdateSettingsRankingRules() throws Exception {
		String indexUid = "updateSettingsRankingRules";
		Index index = client.index().getOrCreateIndex(indexUid, "");
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		Settings settings = new Settings();
		settings.setRankingRules(new String[]{
			"typo",
			"words",
			"proximity",
			"attribute",
			"wordsPosition",
			"exactness",
			"desc(release_date)",
			"desc(rank)"});

		update = client.index().updateSettings(indexUid, settings);
		movies.waitForPendingUpdate(update.getUpdateId());
		Settings newSettings = client.index().getSettings(indexUid);
		assertEquals(8, newSettings.getRankingRules().length);
	}

	/**
	 * Test update settings changing the synonyms
	 */
	@Test
	public void testUpdateSettingsSynonyms() throws Exception {
		String indexUid = "updateSettingsSynonyms";
		Index index = client.index().getOrCreateIndex(indexUid, "");
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		Settings settings = new Settings();

		HashMap<String, String[]> synonyms = new HashMap<>();
		synonyms.put("wolverine", new String[]{"xmen", "logan"});
		synonyms.put("logan", new String[]{"wolverine"});
		settings.setSynonyms(synonyms);

		update = client.index().updateSettings(indexUid, settings);
		movies.waitForPendingUpdate(update.getUpdateId());
		Settings newSettings = client.index().getSettings(indexUid);

		assertEquals(2, newSettings.getSynonyms().size());
	}

	/**
	 * Test reset settings
	 */
	@Test
	public void testResetSettings() throws Exception {
		String indexUid = "testResetSettings";
		Index index = client.index().getOrCreateIndex(indexUid, "");
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		DocumentHandler<Movie> movies = client.documents("movies", Movie.class);
		Update update = movies.addDocument(testData.getData());
		movies.waitForPendingUpdate(update.getUpdateId());

		Settings initialSettings = new Settings();

		Settings settingsWithSynonyms = new Settings();
		HashMap<String, String[]> synonyms = new HashMap();
		synonyms.put("wolverine", new String[]{"xmen", "logan"});
		synonyms.put("logan", new String[]{"wolverine"});
		settingsWithSynonyms.setSynonyms(synonyms);
		update = client.index().updateSettings(indexUid, settingsWithSynonyms);
		movies.waitForPendingUpdate(update.getUpdateId());

		settingsWithSynonyms = client.index().getSettings(indexUid);
		assertEquals(2, settingsWithSynonyms.getSynonyms().size());

		Update resetUpdate = client.index().resetSettings(indexUid);
		movies.waitForPendingUpdate(resetUpdate.getUpdateId());
		Settings settingsAfterReset = client.index().getSettings(indexUid);
		assertEquals(initialSettings.getSynonyms().size(), settingsAfterReset.getSynonyms().size());
	}
}
