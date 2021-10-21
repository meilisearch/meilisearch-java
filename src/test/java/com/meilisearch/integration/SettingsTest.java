package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.Settings;
import com.meilisearch.sdk.UpdateStatus;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.utils.Movie;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
public class SettingsTest extends AbstractIT {

    private TestData<Movie> testData;
    private final GsonJsonHandler jsonGson = new GsonJsonHandler();

    @BeforeEach
    public void initialize() {
        this.setUp();
        if (testData == null) testData = this.getTestData(MOVIES_INDEX, Movie.class);
    }

    @AfterAll
    static void cleanMeiliSearch() {
        cleanup();
    }

    @Test
    @DisplayName("Test get settings from an index by uid")
    public void testGetSettings() throws Exception {
        Index index = createIndex("getSettings");
        Settings settings = index.getSettings();

        assertEquals(6, settings.getRankingRules().length);
    }

    @Test
    @DisplayName("Test update settings changing the ranking rules")
    public void testUpdateSettingsRankingRules() throws Exception {
        Index index = createIndex("updateSettingsRankingRules");
        Settings settings = index.getSettings();
        settings.setRankingRules(
                new String[] {
                    "typo",
                    "words",
                    "sort",
                    "proximity",
                    "attribute",
                    "exactness",
                    "release_date:desc",
                    "rank:desc"
                });
        index.waitForPendingUpdate(index.updateSettings(settings).getUpdateId());
        Settings newSettings = index.getSettings();
        assertEquals(8, newSettings.getRankingRules().length);
    }

    @Test
    @DisplayName("Test update settings changing the synonyms")
    public void testUpdateSettingsSynonyms() throws Exception {
        Index index = createIndex("updateSettingsSynonyms");
        Settings settings = index.getSettings();

        HashMap<String, String[]> synonyms = new HashMap<>();
        synonyms.put("wolverine", new String[] {"xmen", "logan"});
        synonyms.put("logan", new String[] {"wolverine"});
        settings.setSynonyms(synonyms);

        index.waitForPendingUpdate(index.updateSettings(settings).getUpdateId());

        Settings newSettings = index.getSettings();

        assertEquals(2, newSettings.getSynonyms().size());
    }

    @Test
    @DisplayName("Test update settings changing the sort")
    public void testUpdateSettingsSort() throws Exception {
        Index index = createIndex("updateSettingsSort");
        Settings settings = index.getSettings();
        settings.setSortableAttributes(new String[] {"title", "year"});

        index.waitForPendingUpdate(index.updateSettings(settings).getUpdateId());

        Settings newSettings = index.getSettings();

        assertEquals(2, newSettings.getSortableAttributes().length);
    }

    @Test
    @DisplayName("Test reset settings")
    public void testResetSettings() throws Exception {
        Index index = createIndex("testResetSettings");
        Settings initialSettings = index.getSettings();

        Settings settingsWithSynonyms = new Settings();
        HashMap<String, String[]> synonyms = new HashMap<>();
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

    @Test
    @DisplayName("Test get ranking rules settings by uid")
    public void testGetRankingRuleSettings() throws Exception {
        Index index = createIndex("testGetRankingRuleSettings");
        Settings initialSettings = index.getSettings();
        String[] initialRankingRules = index.getRankingRuleSettings();

        assertEquals(initialSettings.getRankingRules().length, initialRankingRules.length);
        assertArrayEquals(initialSettings.getRankingRules(), initialRankingRules);
    }

    @Test
    @DisplayName("Test update ranking rules settings")
    public void testUpdateRankingRuleSettings() throws Exception {
        Index index = createIndex("testUpdateRankingRuleSettings");
        String[] initialRuleSettings = index.getRankingRuleSettings();
        String[] newRankingRules = {
            "typo",
            "words",
            "sort",
            "proximity",
            "attribute",
            "exactness",
            "release_date:desc",
            "rank:desc"
        };

        index.waitForPendingUpdate(index.updateRankingRuleSettings(newRankingRules).getUpdateId());
        String[] updatedRankingRuleSettings = index.getRankingRuleSettings();

        assertEquals(newRankingRules.length, updatedRankingRuleSettings.length);
        assertArrayEquals(newRankingRules, updatedRankingRuleSettings);
        assertNotEquals(initialRuleSettings.length, updatedRankingRuleSettings.length);
    }

    @Test
    @DisplayName("Test reset ranking rules settings")
    public void testResetRankingRuleSettings() throws Exception {
        Index index = createIndex("testResetRankingRuleSettings");
        String[] initialRuleSettings = index.getRankingRuleSettings();
        String[] newRankingRules = {
            "typo",
            "words",
            "sort",
            "proximity",
            "attribute",
            "exactness",
            "release_date:desc",
            "rank:desc"
        };

        index.waitForPendingUpdate(index.updateRankingRuleSettings(newRankingRules).getUpdateId());
        String[] updatedRankingRuleSettings = index.getRankingRuleSettings();

        assertEquals(newRankingRules.length, updatedRankingRuleSettings.length);
        assertArrayEquals(newRankingRules, updatedRankingRuleSettings);
        assertNotEquals(initialRuleSettings.length, updatedRankingRuleSettings.length);

        index.waitForPendingUpdate(index.resetRankingRuleSettings().getUpdateId());
        String[] rankingRulesAfterReset = index.getRankingRuleSettings();

        assertNotEquals(updatedRankingRuleSettings.length, rankingRulesAfterReset.length);
        assertEquals(initialRuleSettings.length, rankingRulesAfterReset.length);
        assertArrayEquals(initialRuleSettings, rankingRulesAfterReset);
    }

	@Test
	@DisplayName("Test get synonyms settings by uid")
	public void testGetSynonymsSettings() throws Exception {
		Index index = createIndex("testGetSynonymsSettings");
		Settings initialSettings = index.getSettings();
		Map<String, String[]> synonymsSettings = index.getSynonymsSettings();

		assertEquals(initialSettings.getSynonyms().size(), synonymsSettings.size());
		assertEquals(initialSettings.getSynonyms(), synonymsSettings);
	}

	@Test
	@DisplayName("Test update synonyms settings")
	public void testUpdateSynonymsSettings() throws Exception {
		Index index = createIndex("testUpdateSynonymsSettings");
		Map<String, String[]> synonymsSettings = index.getSynonymsSettings();
		HashMap<String, String[]> newSynonymsSettings = new HashMap<>();
		newSynonymsSettings.put("wolverine", new String[] {"xmen", "logan"});
		newSynonymsSettings.put("logan", new String[] {"wolverine", "xmen"});
		newSynonymsSettings.put("wow", new String[] {"world of warcraft"});

		index.waitForPendingUpdate(index.updateSynonymsSettings(newSynonymsSettings).getUpdateId());
		Map<String, String[]> updatedRankingRuleSettings = index.getSynonymsSettings();

		assertEquals(newSynonymsSettings.size(), updatedRankingRuleSettings.size());
		assertEquals(newSynonymsSettings.keySet(), updatedRankingRuleSettings.keySet());
		assertNotEquals(synonymsSettings.size(), updatedRankingRuleSettings.size());
		assertNotEquals(synonymsSettings.keySet(), updatedRankingRuleSettings.keySet());
	}

	@Test
	@DisplayName("Test reset synonyms settings")
	public void testResetSynonymsSettings() throws Exception {
		Index index = createIndex("testResetSynonymsSettings");
		Map<String, String[]> synonymsSettings = index.getSynonymsSettings();
		HashMap<String, String[]> newSynonymsSettings = new HashMap<>();
		newSynonymsSettings.put("wolverine", new String[] {"xmen", "logan"});
		newSynonymsSettings.put("logan", new String[] {"wolverine", "xmen"});
		newSynonymsSettings.put("wow", new String[] {"world of warcraft"});

		index.waitForPendingUpdate(index.updateSynonymsSettings(newSynonymsSettings).getUpdateId());
		Map<String, String[]> updatedRankingRuleSettings = index.getSynonymsSettings();

		assertEquals(newSynonymsSettings.size(), updatedRankingRuleSettings.size());
		assertEquals(newSynonymsSettings.keySet(), updatedRankingRuleSettings.keySet());
		assertNotEquals(synonymsSettings.size(), updatedRankingRuleSettings.size());
		assertNotEquals(synonymsSettings.keySet(), updatedRankingRuleSettings.keySet());

		index.waitForPendingUpdate(index.resetSynonymsSettings().getUpdateId());
		Map<String, String[]> synonymsSettingsAfterReset = index.getSynonymsSettings();

		assertNotEquals(updatedRankingRuleSettings.size(), synonymsSettingsAfterReset.size());
		assertEquals(synonymsSettings.size(), synonymsSettingsAfterReset.size());
		assertEquals(synonymsSettings.keySet(), synonymsSettingsAfterReset.keySet());
	}

    @Test
    @DisplayName("Test get stop-words settings by uid")
    public void testGetStopWordsSettings() throws Exception {
        Index index = createIndex("testGetStopWordsSettings");
        Settings initialSettings = index.getSettings();
        String[] initialStopWords = index.getStopWordsSettings();

        assertEquals(initialSettings.getStopWords().length, initialStopWords.length);
        assertArrayEquals(initialSettings.getStopWords(), initialStopWords);
    }

    @Test
    @DisplayName("Test update stop-words settings")
    public void testUpdateStopWordsSettings() throws Exception {
        Index index = createIndex("testUpdateStopWordsSettings");
        String[] initialStopWords = index.getStopWordsSettings();
        String[] newStopWords = {"of", "the", "to"};

        index.waitForPendingUpdate(index.updateStopWordsSettings(newStopWords).getUpdateId());
        String[] updatedStopWordsSettings = index.getStopWordsSettings();

        assertEquals(newStopWords.length, updatedStopWordsSettings.length);
        assertArrayEquals(newStopWords, updatedStopWordsSettings);
        assertNotEquals(initialStopWords.length, updatedStopWordsSettings.length);
    }

    @Test
    @DisplayName("Test reset stop-words settings")
    public void testResetStopWordsSettings() throws Exception {
        Index index = createIndex("testResetStopWordsSettings");
        String[] initialStopWords = index.getStopWordsSettings();
        String[] newStopWords = {"of", "the", "to"};

        index.waitForPendingUpdate(index.updateStopWordsSettings(newStopWords).getUpdateId());
        String[] updatedStopWordsSettings = index.getStopWordsSettings();

        assertEquals(newStopWords.length, updatedStopWordsSettings.length);
        assertArrayEquals(newStopWords, updatedStopWordsSettings);
        assertNotEquals(initialStopWords.length, updatedStopWordsSettings.length);

        index.waitForPendingUpdate(index.resetStopWordsSettings().getUpdateId());
        String[] stopWordsAfterReset = index.getStopWordsSettings();

        assertNotEquals(updatedStopWordsSettings.length, stopWordsAfterReset.length);
        assertEquals(initialStopWords.length, stopWordsAfterReset.length);
        assertArrayEquals(initialStopWords, stopWordsAfterReset);
    }

    @Test
    @DisplayName("Test get searchable attributes settings by uid")
    public void testGetSearchableAttributesSettings() throws Exception {
        Index index = createIndex("testGetSearchableAttributesSettings");
        Settings initialSettings = index.getSettings();
        String[] initialSearchableAttributes = index.getSearchableAttributesSettings();

        assertEquals(
                initialSettings.getSearchableAttributes().length,
                initialSearchableAttributes.length);
        assertArrayEquals(initialSettings.getSearchableAttributes(), initialSearchableAttributes);
    }

    @Test
    @DisplayName("Test update searchable attributes settings")
    public void testUpdateSearchableAttributesSettings() throws Exception {
        Index index = createIndex("testUpdateSearchableAttributesSettings");
        String[] initialSearchableAttributes = index.getSearchableAttributesSettings();
        String[] newSearchableAttributes = {"title", "description", "genre"};

        index.waitForPendingUpdate(
                index.updateSearchableAttributesSettings(newSearchableAttributes).getUpdateId());
        String[] updatedSearchableAttributes = index.getSearchableAttributesSettings();

        assertEquals(newSearchableAttributes.length, updatedSearchableAttributes.length);
        assertArrayEquals(newSearchableAttributes, updatedSearchableAttributes);
        assertNotEquals(initialSearchableAttributes.length, updatedSearchableAttributes.length);
    }

    @Test
    @DisplayName("Test reset searchable attributes settings")
    public void testResetSearchableAttributesSettings() throws Exception {
        Index index = createIndex("testUpdateSearchableAttributesSettings");
        String[] initialSearchableAttributes = index.getSearchableAttributesSettings();
        String[] newSearchableAttributes = {"title", "description", "genre"};

        index.waitForPendingUpdate(
                index.updateSearchableAttributesSettings(newSearchableAttributes).getUpdateId());
        String[] updatedSearchableAttributes = index.getSearchableAttributesSettings();

        assertEquals(newSearchableAttributes.length, updatedSearchableAttributes.length);
        assertArrayEquals(newSearchableAttributes, updatedSearchableAttributes);
        assertNotEquals(initialSearchableAttributes.length, updatedSearchableAttributes.length);

        index.waitForPendingUpdate(index.resetSearchableAttributesSettings().getUpdateId());
        String[] searchableAttributesAfterReset = index.getSearchableAttributesSettings();

        assertNotEquals(updatedSearchableAttributes.length, searchableAttributesAfterReset.length);
        assertEquals(initialSearchableAttributes.length, searchableAttributesAfterReset.length);
        assertArrayEquals(initialSearchableAttributes, searchableAttributesAfterReset);
    }

    @Test
    @DisplayName("Test get display attributes settings by uid")
    public void testGetDisplayedAttributesSettings() throws Exception {
        Index index = createIndex("testGetDisplayedAttributesSettings");
        Settings initialSettings = index.getSettings();
        String[] initialDisplayedAttributes = index.getDisplayedAttributesSettings();

        assertEquals(
                initialSettings.getSearchableAttributes().length,
                initialDisplayedAttributes.length);
        assertArrayEquals(initialSettings.getDisplayedAttributes(), initialDisplayedAttributes);
    }

    @Test
    @DisplayName("Test update display attributes settings")
    public void testUpdateDisplayedAttributesSettings() throws Exception {
        Index index = createIndex("testUpdateDisplayedAttributesSettings");
        String[] initialDisplayedAttributes = index.getDisplayedAttributesSettings();
        String[] newDisplayedAttributes = {"title", "description", "genre", "release_date"};

        index.waitForPendingUpdate(
                index.updateDisplayedAttributesSettings(newDisplayedAttributes).getUpdateId());
        String[] updatedDisplayedAttributes = index.getDisplayedAttributesSettings();

        assertEquals(newDisplayedAttributes.length, updatedDisplayedAttributes.length);
        assertArrayEquals(newDisplayedAttributes, updatedDisplayedAttributes);
        assertNotEquals(initialDisplayedAttributes.length, updatedDisplayedAttributes.length);
    }

    @Test
    @DisplayName("Test reset display attributes settings")
    public void testResetDisplayedAttributesSettings() throws Exception {
        Index index = createIndex("testUpdateDisplayedAttributesSettings");
        String[] initialDisplayedAttributes = index.getDisplayedAttributesSettings();
        String[] newDisplayedAttributes = {"title", "description", "genre", "release_date", "cast"};

        index.waitForPendingUpdate(
                index.updateDisplayedAttributesSettings(newDisplayedAttributes).getUpdateId());
        String[] updatedDisplayedAttributes = index.getDisplayedAttributesSettings();

        assertEquals(newDisplayedAttributes.length, updatedDisplayedAttributes.length);
        assertArrayEquals(newDisplayedAttributes, updatedDisplayedAttributes);
        assertNotEquals(initialDisplayedAttributes.length, updatedDisplayedAttributes.length);

        index.waitForPendingUpdate(index.resetDisplayedAttributesSettings().getUpdateId());
        String[] displayedAttributesAfterReset = index.getDisplayedAttributesSettings();

        assertNotEquals(updatedDisplayedAttributes.length, displayedAttributesAfterReset.length);
    }

    private Index createIndex(String indexUid) throws Exception {
        Index index = client.index(indexUid);
        UpdateStatus updateInfo =
                jsonGson.decode(index.addDocuments(testData.getRaw()), UpdateStatus.class);
        index.waitForPendingUpdate(updateInfo.getUpdateId());

        return index;
    }
}
