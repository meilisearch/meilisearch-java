package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.*;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.utils.Movie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);
		index.waitForPendingUpdate(updateInfo.getUpdateId());

		Settings settings = index.getSettings();

		assertEquals(5, settings.getRankingRules().length);
	}

	/**
	 * Test update settings changing the ranking rules
	 */
	@Test
	public void testUpdateSettingsRankingRules() throws Exception {
		String indexUid = "updateSettingsRankingRules";
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);
		index.waitForPendingUpdate(updateInfo.getUpdateId());

		Settings settings = index.getSettings();

		settings.setRankingRules(new String[]{
			"typo",
			"words",
			"proximity",
			"attribute",
			"exactness",
			"desc(release_date)",
			"desc(rank)"});
		index.waitForPendingUpdate(index.updateSettings(settings).getUpdateId());
		Settings newSettings = index.getSettings();
		assertEquals(7, newSettings.getRankingRules().length);
	}

	/**
	 * Test update settings changing the synonyms
	 */
	@Test
	public void testUpdateSettingsSynonyms() throws Exception {
		String indexUid = "updateSettingsSynonyms";
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);
		index.waitForPendingUpdate(updateInfo.getUpdateId());

		Settings settings = index.getSettings();

		HashMap<String, String[]> synonyms = new HashMap();
		synonyms.put("wolverine", new String[] {"xmen", "logan"});
		synonyms.put("logan", new String[] {"wolverine"});
		settings.setSynonyms(synonyms);

		index.waitForPendingUpdate(index.updateSettings(settings).getUpdateId());

		Settings newSettings = index.getSettings();

		assertEquals(2, newSettings.getSynonyms().size());
	}

	/**
	 * Test reset settings
	 */
	@Test
	public void testResetSettings() throws Exception {
		String indexUid = "testResetSettings";
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);
		index.waitForPendingUpdate(updateInfo.getUpdateId());

		Settings initialSettings = index.getSettings();

		Settings settingsWithSynonyms = new Settings();
		HashMap<String, String[]> synonyms = new HashMap();
		synonyms.put("wolverine", new String[] {"xmen", "logan"});
		synonyms.put("logan", new String[] {"wolverine"});
		settingsWithSynonyms.setSynonyms(synonyms);

		index.waitForPendingUpdate(index.updateSettings(settingsWithSynonyms).getUpdateId());
		settingsWithSynonyms = index.getSettings();
		assertEquals(2, settingsWithSynonyms.getSynonyms().size());

		index.waitForPendingUpdate(index.resetSettings().getUpdateId());
		Settings settingsAfterReset = index.getSettings();
		assertEquals(initialSettings.getSynonyms().size(), settingsAfterReset.getSynonyms().size());
	}
}
