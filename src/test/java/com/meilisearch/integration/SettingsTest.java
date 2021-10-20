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

    private Index createIndex(String indexUid) throws Exception {
        Index index = client.index(indexUid);
        UpdateStatus updateInfo =
                jsonGson.decode(index.addDocuments(testData.getRaw()), UpdateStatus.class);
        index.waitForPendingUpdate(updateInfo.getUpdateId());

        return index;
    }
}
