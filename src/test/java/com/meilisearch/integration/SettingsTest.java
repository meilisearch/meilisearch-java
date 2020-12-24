package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.*;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.utils.Movie;
import okhttp3.internal.http2.Settings;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

	// TODO: Real Search tests after search refactor

	/**
	 * Test get settings from an index by uid
	 */
	@Test
	public void testGetSettings() throws Exception {
		String indexUid = "updateSettings";
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);
		index.waitForPendingUpdate(updateInfo.getUpdateId());

		SettingsRequest settingsRequest = jsonGson.decode(
			index.getSettings(),
			SettingsRequest.class
		);

		assertEquals(6, settingsRequest.getRankingRules().length);
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

		SettingsRequest settings = jsonGson.decode(
			index.getSettings(),
			SettingsRequest.class
		);

		settings.setRankingRules(new String[]{
			"typo",
			"words",
			"proximity",
			"attribute",
			"wordsPosition",
			"exactness",
			"desc(release_date)",
			"desc(rank)"});

		index.updateSettings(settings);
		Thread.sleep(1000);

		SettingsRequest newSettings = jsonGson.decode(
			index.getSettings(),
			SettingsRequest.class
		);

		assertEquals(8, newSettings.getRankingRules().length);
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

		SettingsRequest settings = jsonGson.decode(
			index.getSettings(),
			SettingsRequest.class
		);

		HashMap<String, String[]> synonyms = new HashMap();
		synonyms.put("wolverine", new String[] {"xmen", "logan"});
		synonyms.put("logan", new String[] {"wolverine"});
		settings.setSynonyms(synonyms);

		index.updateSettings(settings);
		Thread.sleep(1000);

		SettingsRequest newSettings = jsonGson.decode(
			index.getSettings(),
			SettingsRequest.class
		);

		assertEquals(2, newSettings.getSynonyms().size());
	}

	/**
	 * Test update settings changing the synonyms
	 */
	@Test
	public void testResetSettings() throws Exception {
		String indexUid = "updateSettingsSynonyms";
		Index index = client.index(indexUid);
		GsonJsonHandler jsonGson = new GsonJsonHandler();

		TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
		UpdateStatus updateInfo = jsonGson.decode(
			index.addDocuments(testData.getRaw()),
			UpdateStatus.class
		);
		index.waitForPendingUpdate(updateInfo.getUpdateId());

		SettingsRequest settings = jsonGson.decode(
			index.getSettings(),
			SettingsRequest.class
		);

		HashMap<String, String[]> synonyms = new HashMap();
		synonyms.put("wolverine", new String[] {"xmen", "logan"});
		synonyms.put("logan", new String[] {"wolverine"});
		settings.setSynonyms(synonyms);

		index.updateSettings(settings);
		Thread.sleep(1000);
		SettingsRequest newSettings = jsonGson.decode(
			index.getSettings(),
			SettingsRequest.class
		);

		assertEquals(2, newSettings.getSynonyms().size());

		index.resetSettings();
		Thread.sleep(1000);
		SettingsRequest settingsAfterReset = jsonGson.decode(
			index.getSettings(),
			SettingsRequest.class
		);
		assertEquals(0, settingsAfterReset.getSynonyms().size());
	}

}
