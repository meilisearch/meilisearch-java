package com.meilisearch.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.Faceting;
import com.meilisearch.sdk.model.Pagination;
import com.meilisearch.sdk.model.Settings;
import com.meilisearch.sdk.model.TaskInfo;
import com.meilisearch.sdk.model.TypoTolerance;
import com.meilisearch.sdk.utils.Movie;
import java.util.Arrays;
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

    @BeforeEach
    public void initialize() {
        this.setUp();
        if (testData == null) testData = this.getTestData(MOVIES_INDEX, Movie.class);
    }

    @AfterAll
    static void cleanMeilisearch() {
        cleanup();
    }

    /** Tests of the setting methods */
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
        index.waitForTask(index.updateSettings(settings).getTaskUid());
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

        index.waitForTask(index.updateSettings(settings).getTaskUid());

        Settings newSettings = index.getSettings();

        assertEquals(2, newSettings.getSynonyms().size());
    }

    @Test
    @DisplayName("Test update settings changing the sort")
    public void testUpdateSettingsSort() throws Exception {
        Index index = createIndex("updateSettingsSort");
        Settings settings = index.getSettings();
        settings.setSortableAttributes(new String[] {"title", "year"});

        index.waitForTask(index.updateSettings(settings).getTaskUid());

        Settings newSettings = index.getSettings();

        assertEquals(2, newSettings.getSortableAttributes().length);
    }

    @Test
    @DisplayName("Test update settings changing the typo tolerance")
    public void testUpdateSettingsTypoTolerance() throws Exception {
        Index index = createIndex("updateSettingsTypoTolerance");
        Settings settings = index.getSettings();

        TypoTolerance typoTolerance = new TypoTolerance();
        typoTolerance.setDisableOnWords(new String[] {"and"});
        typoTolerance.setDisableOnAttributes(new String[] {"title"});
        settings.setTypoTolerance(typoTolerance);

        index.waitForTask(index.updateSettings(settings).getTaskUid());

        Settings newSettings = index.getSettings();

        assertEquals(1, newSettings.getTypoTolerance().getDisableOnWords().length);
        assertEquals(1, newSettings.getTypoTolerance().getDisableOnAttributes().length);
        assertTrue(newSettings.getTypoTolerance().isEnabled());
    }

    @Test
    @DisplayName("Test update multiple settings in a row")
    public void testUpdateMultipleSettingsInARow() throws Exception {
        Index index = createIndex("updateMultipleSettingsInARow");
        Settings settingsDisplayedAttr = new Settings();
        settingsDisplayedAttr.setDisplayedAttributes(
                new String[] {"title", "overview", "genres", "release_date"});
        index.waitForTask(index.updateSettings(settingsDisplayedAttr).getTaskUid());
        Settings newSettingsDisplayedAttr = index.getSettings();

        Settings settingsRankingRules = new Settings();
        settingsRankingRules.setRankingRules(
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
        index.waitForTask(index.updateSettings(settingsRankingRules).getTaskUid());
        Settings newSettingsRankingRules = index.getSettings();

        Settings settingsSynonyms = new Settings();
        HashMap<String, String[]> synonyms = new HashMap<>();
        synonyms.put("wolverine", new String[] {"xmen", "logan"});
        synonyms.put("logan", new String[] {"wolverine"});
        settingsSynonyms.setSynonyms(synonyms);
        index.waitForTask(index.updateSettings(settingsSynonyms).getTaskUid());
        Settings newSettingsSynonyms = index.getSettings();

        assertEquals(4, newSettingsDisplayedAttr.getDisplayedAttributes().length);
        assertEquals(6, newSettingsDisplayedAttr.getRankingRules().length);
        assertEquals(0, newSettingsDisplayedAttr.getSynonyms().size());
        assertEquals(4, newSettingsRankingRules.getDisplayedAttributes().length);
        assertEquals(8, newSettingsRankingRules.getRankingRules().length);
        assertEquals(0, newSettingsRankingRules.getSynonyms().size());
        assertEquals(4, newSettingsSynonyms.getDisplayedAttributes().length);
        assertEquals(2, newSettingsSynonyms.getSynonyms().size());
        assertEquals(8, newSettingsSynonyms.getRankingRules().length);
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

        index.waitForTask(index.updateSettings(settingsWithSynonyms).getTaskUid());
        settingsWithSynonyms = index.getSettings();
        assertEquals(2, settingsWithSynonyms.getSynonyms().size());

        index.waitForTask(index.resetSettings().getTaskUid());
        Settings settingsAfterReset = index.getSettings();
        assertEquals(initialSettings.getSynonyms().size(), settingsAfterReset.getSynonyms().size());
    }

    /** Tests of the ranking rules setting methods */
    @Test
    @DisplayName("Test get ranking rules settings by uid")
    public void testGetRankingRulesSettings() throws Exception {
        Index index = createIndex("testGetRankingRulesSettings");
        Settings initialSettings = index.getSettings();
        String[] initialRankingRules = index.getRankingRulesSettings();

        assertEquals(initialSettings.getRankingRules().length, initialRankingRules.length);
        assertArrayEquals(initialSettings.getRankingRules(), initialRankingRules);
    }

    @Test
    @DisplayName("Test update ranking rules settings")
    public void testUpdateRankingRulesSettings() throws Exception {
        Index index = createIndex("testUpdateRankingRulesSettings");
        String[] initialRulesSettings = index.getRankingRulesSettings();
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

        index.waitForTask(index.updateRankingRulesSettings(newRankingRules).getTaskUid());
        String[] updatedRankingRulesSettings = index.getRankingRulesSettings();

        assertEquals(newRankingRules.length, updatedRankingRulesSettings.length);
        assertArrayEquals(newRankingRules, updatedRankingRulesSettings);
        assertNotEquals(initialRulesSettings.length, updatedRankingRulesSettings.length);
    }

    @Test
    @DisplayName("Test reset ranking rules settings")
    public void testResetRankingRulesSettings() throws Exception {
        Index index = createIndex("testResetRankingRulesSettings");
        String[] initialRulesSettings = index.getRankingRulesSettings();
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

        index.waitForTask(index.updateRankingRulesSettings(newRankingRules).getTaskUid());
        String[] updatedRankingRulesSettings = index.getRankingRulesSettings();

        index.waitForTask(index.resetRankingRulesSettings().getTaskUid());
        String[] rankingRulesAfterReset = index.getRankingRulesSettings();

        assertEquals(newRankingRules.length, updatedRankingRulesSettings.length);
        assertArrayEquals(newRankingRules, updatedRankingRulesSettings);
        assertNotEquals(initialRulesSettings.length, updatedRankingRulesSettings.length);

        assertNotEquals(updatedRankingRulesSettings.length, rankingRulesAfterReset.length);
        assertEquals(initialRulesSettings.length, rankingRulesAfterReset.length);
        assertArrayEquals(initialRulesSettings, rankingRulesAfterReset);
    }

    /** Tests of the synonyms setting methods */
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

        index.waitForTask(index.updateSynonymsSettings(newSynonymsSettings).getTaskUid());
        Map<String, String[]> updatedSynonymsSettings = index.getSynonymsSettings();

        assertEquals(newSynonymsSettings.size(), updatedSynonymsSettings.size());
        assertEquals(newSynonymsSettings.keySet(), updatedSynonymsSettings.keySet());
        assertNotEquals(synonymsSettings.size(), updatedSynonymsSettings.size());
        assertNotEquals(synonymsSettings.keySet(), updatedSynonymsSettings.keySet());
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

        index.waitForTask(index.updateSynonymsSettings(newSynonymsSettings).getTaskUid());
        Map<String, String[]> updatedSynonymsSettings = index.getSynonymsSettings();

        index.waitForTask(index.resetSynonymsSettings().getTaskUid());
        Map<String, String[]> synonymsSettingsAfterReset = index.getSynonymsSettings();

        assertEquals(newSynonymsSettings.size(), updatedSynonymsSettings.size());
        assertEquals(newSynonymsSettings.keySet(), updatedSynonymsSettings.keySet());
        assertNotEquals(synonymsSettings.size(), updatedSynonymsSettings.size());
        assertNotEquals(synonymsSettings.keySet(), updatedSynonymsSettings.keySet());

        assertNotEquals(updatedSynonymsSettings.size(), synonymsSettingsAfterReset.size());
        assertEquals(synonymsSettings.size(), synonymsSettingsAfterReset.size());
        assertEquals(synonymsSettings.keySet(), synonymsSettingsAfterReset.keySet());
    }

    /** Tests of the stop words setting methods */
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

        index.waitForTask(index.updateStopWordsSettings(newStopWords).getTaskUid());
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

        index.waitForTask(index.updateStopWordsSettings(newStopWords).getTaskUid());
        String[] updatedStopWordsSettings = index.getStopWordsSettings();

        index.waitForTask(index.resetStopWordsSettings().getTaskUid());
        String[] stopWordsAfterReset = index.getStopWordsSettings();

        assertEquals(newStopWords.length, updatedStopWordsSettings.length);
        assertArrayEquals(newStopWords, updatedStopWordsSettings);
        assertNotEquals(initialStopWords.length, updatedStopWordsSettings.length);

        assertNotEquals(updatedStopWordsSettings.length, stopWordsAfterReset.length);
        assertEquals(initialStopWords.length, stopWordsAfterReset.length);
        assertArrayEquals(initialStopWords, stopWordsAfterReset);
    }

    /** Tests of the searchable attributes setting methods */
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

        index.waitForTask(
                index.updateSearchableAttributesSettings(newSearchableAttributes).getTaskUid());
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

        index.waitForTask(
                index.updateSearchableAttributesSettings(newSearchableAttributes).getTaskUid());
        String[] updatedSearchableAttributes = index.getSearchableAttributesSettings();

        index.waitForTask(index.resetSearchableAttributesSettings().getTaskUid());
        String[] searchableAttributesAfterReset = index.getSearchableAttributesSettings();

        assertEquals(newSearchableAttributes.length, updatedSearchableAttributes.length);
        assertArrayEquals(newSearchableAttributes, updatedSearchableAttributes);
        assertNotEquals(initialSearchableAttributes.length, updatedSearchableAttributes.length);

        assertNotEquals(updatedSearchableAttributes.length, searchableAttributesAfterReset.length);
        assertEquals(initialSearchableAttributes.length, searchableAttributesAfterReset.length);
        assertArrayEquals(initialSearchableAttributes, searchableAttributesAfterReset);
    }

    /** Tests of the display attributes setting methods */
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

        index.waitForTask(
                index.updateDisplayedAttributesSettings(newDisplayedAttributes).getTaskUid());
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

        index.waitForTask(
                index.updateDisplayedAttributesSettings(newDisplayedAttributes).getTaskUid());
        String[] updatedDisplayedAttributes = index.getDisplayedAttributesSettings();

        index.waitForTask(index.resetDisplayedAttributesSettings().getTaskUid());
        String[] displayedAttributesAfterReset = index.getDisplayedAttributesSettings();

        assertEquals(newDisplayedAttributes.length, updatedDisplayedAttributes.length);
        assertArrayEquals(newDisplayedAttributes, updatedDisplayedAttributes);
        assertNotEquals(initialDisplayedAttributes.length, updatedDisplayedAttributes.length);

        assertNotEquals(updatedDisplayedAttributes.length, displayedAttributesAfterReset.length);
    }

    /** Tests of the filterable attributes setting methods */
    @Test
    @DisplayName("Test get filterable attributes settings by uid")
    public void testGetFilterableAttributesSettings() throws Exception {
        Index index = createIndex("testGetDisplayedAttributesSettings");
        Settings initialSettings = index.getSettings();
        String[] initialFilterableAttributes = index.getFilterableAttributesSettings();

        assertEquals(
                initialSettings.getFilterableAttributes().length,
                initialFilterableAttributes.length);
        assertArrayEquals(initialSettings.getFilterableAttributes(), initialFilterableAttributes);
    }

    @Test
    @DisplayName("Test update filterable attributes settings")
    public void testUpdateFilterableAttributesSettings() throws Exception {
        Index index = createIndex("testUpdateDisplayedAttributesSettings");
        String[] initialFilterableAttributes = index.getFilterableAttributesSettings();
        String[] newFilterableAttributes = {"title", "description", "genre", "release_date"};

        index.waitForTask(
                index.updateFilterableAttributesSettings(newFilterableAttributes).getTaskUid());
        String[] updatedFilterableAttributes = index.getFilterableAttributesSettings();

        assertEquals(newFilterableAttributes.length, updatedFilterableAttributes.length);
        assertThat(
                Arrays.asList(newFilterableAttributes),
                containsInAnyOrder(updatedFilterableAttributes));
        assertNotEquals(initialFilterableAttributes.length, updatedFilterableAttributes.length);
    }

    @Test
    @DisplayName("Test reset filterable attributes settings")
    public void testResetFilterableAttributesSettings() throws Exception {
        Index index = createIndex("testUpdateDisplayedAttributesSettings");
        String[] initialFilterableAttributes = index.getFilterableAttributesSettings();
        String[] newFilterableAttributes = {
            "title", "description", "genres", "director", "release_date"
        };

        index.waitForTask(
                index.updateFilterableAttributesSettings(newFilterableAttributes).getTaskUid());
        String[] updatedFilterableAttributes = index.getFilterableAttributesSettings();

        index.waitForTask(index.resetFilterableAttributesSettings().getTaskUid());
        String[] filterableAttributesAfterReset = index.getFilterableAttributesSettings();

        assertEquals(newFilterableAttributes.length, updatedFilterableAttributes.length);
        assertThat(
                Arrays.asList(newFilterableAttributes),
                containsInAnyOrder(updatedFilterableAttributes));
        assertNotEquals(initialFilterableAttributes.length, updatedFilterableAttributes.length);

        assertNotEquals(updatedFilterableAttributes.length, filterableAttributesAfterReset.length);
        assertNotEquals(initialFilterableAttributes.length, updatedFilterableAttributes.length);
    }

    /** Tests of the sortable attributes setting methods* */
    @Test
    @DisplayName("Test get sortable attributes settings by uid")
    public void testGetSortableAttributesSettings() throws Exception {
        Index index = createIndex("testGetSortableAttributesSettings");
        Settings initialSettings = index.getSettings();
        String[] initialSortableAttributes = index.getSortableAttributesSettings();

        assertEquals(
                initialSettings.getSortableAttributes().length, initialSortableAttributes.length);
        assertArrayEquals(initialSettings.getSortableAttributes(), initialSortableAttributes);
    }

    @Test
    @DisplayName("Test update sortable attributes settings")
    public void testUpdateSortableAttributesSettings() throws Exception {
        Index index = createIndex("testUpdateSortableAttributesSettings");
        String[] initialSortableAttributes = index.getSortableAttributesSettings();
        String[] newSortableAttributes = {"title", "description", "genre", "release_date"};

        index.waitForTask(
                index.updateSortableAttributesSettings(newSortableAttributes).getTaskUid());
        String[] updatedSortableAttributes = index.getSortableAttributesSettings();

        assertEquals(newSortableAttributes.length, updatedSortableAttributes.length);
        assertThat(
                Arrays.asList(newSortableAttributes),
                containsInAnyOrder(updatedSortableAttributes));
        assertNotEquals(initialSortableAttributes.length, updatedSortableAttributes.length);
    }

    @Test
    @DisplayName("Test reset sortable attributes settings")
    public void testResetSortableAttributesSettings() throws Exception {
        Index index = createIndex("testUpdateSortableAttributesSettings");
        String[] initialSortableAttributes = index.getSortableAttributesSettings();
        String[] newSortableAttributes = {
            "title", "description", "genres", "director", "release_date"
        };

        index.waitForTask(
                index.updateSortableAttributesSettings(newSortableAttributes).getTaskUid());
        String[] updatedSortableAttributes = index.getSortableAttributesSettings();

        index.waitForTask(index.resetFilterableAttributesSettings().getTaskUid());
        String[] filterableAttributesAfterReset = index.getFilterableAttributesSettings();

        assertEquals(newSortableAttributes.length, updatedSortableAttributes.length);
        assertThat(
                Arrays.asList(newSortableAttributes),
                containsInAnyOrder(updatedSortableAttributes));
        assertNotEquals(initialSortableAttributes.length, updatedSortableAttributes.length);

        assertNotEquals(updatedSortableAttributes.length, filterableAttributesAfterReset.length);
        assertNotEquals(initialSortableAttributes.length, updatedSortableAttributes.length);
    }

    /** Tests of the distinct attributes setting methods */
    @Test
    @DisplayName("Test get distinct attribute settings by uid")
    public void testGetDistinctAttributeSettings() throws Exception {
        Index index = createIndex("testGetDistinctAttributeSettings");
        Settings initialSettings = index.getSettings();
        String initialDistinctAttribute = index.getDistinctAttributeSettings();

        assertEquals(initialSettings.getDistinctAttribute(), initialDistinctAttribute);
    }

    @Test
    @DisplayName("Test update distinct attribute settings")
    public void testUpdateDistinctAttributeSettings() throws Exception {
        Index index = createIndex("testUpdateDistinctAttributeSettings");
        String initialDistinctAttribute = index.getDistinctAttributeSettings();
        String newDistinctAttribute = "title";

        index.waitForTask(index.updateDistinctAttributeSettings(newDistinctAttribute).getTaskUid());
        String updatedDistinctAttribute = index.getDistinctAttributeSettings();

        assertEquals(newDistinctAttribute, updatedDistinctAttribute);
        assertNotEquals(initialDistinctAttribute, updatedDistinctAttribute);
    }

    @Test
    @DisplayName("Test reset distinct attribute settings")
    public void testResetDistinctAttributeSettings() throws Exception {
        Index index = createIndex("testResetDistinctAttributeSettings");
        String initialDistinctAttribute = index.getDistinctAttributeSettings();
        String newDistinctAttribute = "title";

        index.waitForTask(index.updateDistinctAttributeSettings(newDistinctAttribute).getTaskUid());
        String updatedDistinctAttribute = index.getDistinctAttributeSettings();

        index.waitForTask(index.resetDistinctAttributeSettings().getTaskUid());
        String distinctAttributeAfterReset = index.getDistinctAttributeSettings();

        assertEquals(newDistinctAttribute, updatedDistinctAttribute);
        assertNotEquals(initialDistinctAttribute, updatedDistinctAttribute);

        assertNotEquals(updatedDistinctAttribute, distinctAttributeAfterReset);
        assertNotEquals(initialDistinctAttribute, updatedDistinctAttribute);
    }

    /** Tests of the typo tolerance setting methods */
    @Test
    @DisplayName("Test get typo tolerance settings by uid")
    public void testGetTypoTolerance() throws Exception {
        Index index = createIndex("testGetTypoTolerance");
        Settings initialSettings = index.getSettings();
        TypoTolerance initialTypoTolerance = index.getTypoToleranceSettings();

        assertEquals(initialSettings.getTypoTolerance().getDisableOnWords().length, 0);
        assertEquals(initialTypoTolerance.getDisableOnWords().length, 0);
        assertEquals(initialSettings.getTypoTolerance().getDisableOnAttributes().length, 0);
        assertEquals(initialTypoTolerance.getDisableOnAttributes().length, 0);
        assertEquals(
                initialSettings.getTypoTolerance().isEnabled(), initialTypoTolerance.isEnabled());
        assertNotNull(initialTypoTolerance.getMinWordSizeForTypos().containsKey("oneTypo"));
        assertNotNull(initialTypoTolerance.getMinWordSizeForTypos().get("oneTypo"));
        assertNotNull(initialTypoTolerance.getMinWordSizeForTypos().containsKey("twoTypos"));
        assertNotNull(initialTypoTolerance.getMinWordSizeForTypos().get("twoTypos"));
    }

    @Test
    @DisplayName("Test update typo tolerance settings")
    public void testUpdateTypoTolerance() throws Exception {
        Index index = createIndex("testUpdateTypoTolerance");
        TypoTolerance newTypoTolerance = new TypoTolerance();
        newTypoTolerance.setEnabled(true);
        newTypoTolerance.setDisableOnWords(new String[] {"and"});
        newTypoTolerance.setDisableOnAttributes(new String[] {"title"});

        HashMap<String, Integer> minWordSizeTypos =
                new HashMap<String, Integer>() {
                    {
                        put("oneTypo", 7);
                        put("twoTypos", 10);
                    }
                };
        newTypoTolerance.setMinWordSizeForTypos(minWordSizeTypos);
        index.waitForTask(index.updateTypoToleranceSettings(newTypoTolerance).getTaskUid());
        TypoTolerance updatedTypoTolerance = index.getTypoToleranceSettings();

        assertEquals(
                newTypoTolerance.getDisableOnWords()[0],
                updatedTypoTolerance.getDisableOnWords()[0]);
        assertEquals(
                newTypoTolerance.getDisableOnAttributes()[0],
                updatedTypoTolerance.getDisableOnAttributes()[0]);
        assertTrue(updatedTypoTolerance.isEnabled());
        assertTrue(
                updatedTypoTolerance.getMinWordSizeForTypos().containsKey("oneTypo")
                        && updatedTypoTolerance.getMinWordSizeForTypos().get("oneTypo") == 7);
        assertTrue(
                updatedTypoTolerance.getMinWordSizeForTypos().containsKey("twoTypos")
                        && updatedTypoTolerance.getMinWordSizeForTypos().get("twoTypos") == 10);
    }

    @Test
    @DisplayName("Test update typo tolerance settings")
    public void testPartialUpdateTypoTolerance() throws Exception {
        Index index = createIndex("testUpdateTypoTolerance");
        TypoTolerance newTypoTolerance = new TypoTolerance();
        newTypoTolerance.setDisableOnWords(new String[] {"the"});
        newTypoTolerance.setDisableOnAttributes(new String[] {"title"});

        index.waitForTask(index.updateTypoToleranceSettings(newTypoTolerance).getTaskUid());
        TypoTolerance updatedTypoTolerance = index.getTypoToleranceSettings();

        assertEquals(
                newTypoTolerance.getDisableOnWords()[0],
                updatedTypoTolerance.getDisableOnWords()[0]);
        assertEquals(
                newTypoTolerance.getDisableOnAttributes()[0],
                updatedTypoTolerance.getDisableOnAttributes()[0]);
        assertTrue(updatedTypoTolerance.isEnabled());
        assertTrue(
                updatedTypoTolerance.getMinWordSizeForTypos().containsKey("oneTypo")
                        && updatedTypoTolerance.getMinWordSizeForTypos().get("oneTypo") == 5);
        assertTrue(
                updatedTypoTolerance.getMinWordSizeForTypos().containsKey("twoTypos")
                        && updatedTypoTolerance.getMinWordSizeForTypos().get("twoTypos") == 9);
    }

    @Test
    @DisplayName("Test reset typo tolerance settings")
    public void testResetTypoTolerance() throws Exception {
        Index index = createIndex("testResetTypoTolerance");

        TypoTolerance initialTypoTolerance = index.getTypoToleranceSettings();
        TypoTolerance newTypoTolerance = new TypoTolerance();
        newTypoTolerance.setEnabled(true);
        newTypoTolerance.setDisableOnWords(new String[] {"and"});
        newTypoTolerance.setDisableOnAttributes(new String[] {"title"});
        HashMap<String, Integer> minWordSizeTypos =
                new HashMap<String, Integer>() {
                    {
                        put("oneTypo", 7);
                        put("twoTypos", 10);
                    }
                };
        newTypoTolerance.setMinWordSizeForTypos(minWordSizeTypos);

        index.waitForTask(index.updateTypoToleranceSettings(newTypoTolerance).getTaskUid());
        TypoTolerance updatedTypoTolerance = index.getTypoToleranceSettings();

        index.waitForTask(index.resetTypoToleranceSettings().getTaskUid());
        TypoTolerance typoToleranceAfterReset = index.getTypoToleranceSettings();

        assertEquals(
                newTypoTolerance.getDisableOnWords().length,
                updatedTypoTolerance.getDisableOnWords().length);
        assertEquals(
                newTypoTolerance.getDisableOnAttributes().length,
                updatedTypoTolerance.getDisableOnAttributes().length);
        assertEquals(newTypoTolerance.isEnabled(), updatedTypoTolerance.isEnabled());

        assertEquals(
                initialTypoTolerance.getDisableOnWords().length,
                typoToleranceAfterReset.getDisableOnWords().length);
        assertEquals(
                initialTypoTolerance.getDisableOnAttributes().length,
                typoToleranceAfterReset.getDisableOnAttributes().length);
        assertEquals(initialTypoTolerance.isEnabled(), typoToleranceAfterReset.isEnabled());
        assertTrue(
                typoToleranceAfterReset.getMinWordSizeForTypos().containsKey("oneTypo")
                        && typoToleranceAfterReset.getMinWordSizeForTypos().get("oneTypo") != null);
        assertTrue(
                typoToleranceAfterReset.getMinWordSizeForTypos().containsKey("twoTypos")
                        && typoToleranceAfterReset.getMinWordSizeForTypos().get("twoTypos")
                                != null);
    }

    /** Tests of all the specifics setting methods when null is passed */
    @Test
    @DisplayName("Test update synonyms settings when null is passed")
    public void testUpdateSynonymsSettingsUsingNull() throws Exception {
        Index index = createIndex("testUpdateSynonymsSettingsUsingNull");
        Map<String, String[]> initialSynonymsSettings = index.getSynonymsSettings();
        HashMap<String, String[]> newSynonymsSettings = new HashMap<>();
        newSynonymsSettings.put("007", new String[] {"james bond", "bond"});
        newSynonymsSettings.put("ironman", new String[] {"tony stark", "iron man"});

        index.waitForTask(index.updateSynonymsSettings(newSynonymsSettings).getTaskUid());
        Map<String, String[]> updatedSynonymsSettings = index.getSynonymsSettings();

        index.waitForTask(index.updateSynonymsSettings(null).getTaskUid());
        Map<String, String[]> resetSynonymsSettings = index.getSynonymsSettings();

        assertNotEquals(initialSynonymsSettings.size(), updatedSynonymsSettings.size());
        assertNotEquals(updatedSynonymsSettings.size(), resetSynonymsSettings.size());
        assertEquals(initialSynonymsSettings.size(), resetSynonymsSettings.size());
        assertEquals(initialSynonymsSettings.keySet(), resetSynonymsSettings.keySet());
    }

    @Test
    @DisplayName("Test update stop-words settings when null is passed")
    public void testUpdateStopWordsSettingsUsingNull() throws Exception {
        Index index = createIndex("testUpdateStopWordsSettingsUsingNull");
        String[] initialStopWords = index.getStopWordsSettings();
        String[] newStopWords = {"the", "to", "in", "on"};

        index.waitForTask(index.updateStopWordsSettings(newStopWords).getTaskUid());
        String[] updatedStopWords = index.getStopWordsSettings();

        index.waitForTask(index.updateStopWordsSettings(null).getTaskUid());
        String[] resetStopWords = index.getStopWordsSettings();

        assertNotEquals(initialStopWords.length, updatedStopWords.length);
        assertNotEquals(updatedStopWords.length, resetStopWords.length);
        assertEquals(initialStopWords.length, resetStopWords.length);
        assertArrayEquals(initialStopWords, resetStopWords);
    }

    @Test
    @DisplayName("Test reset ranking rules when null value is passed")
    public void testUpdateRankingRulesSettingsUsingNull() throws Exception {
        Index index = createIndex("testUpdateRankingRulesSettingsUsingNull");
        String[] initialRankingRule = index.getRankingRulesSettings();
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

        index.waitForTask(index.updateRankingRulesSettings(newRankingRules).getTaskUid());
        String[] newRankingRule = index.getRankingRulesSettings();

        index.waitForTask(index.updateRankingRulesSettings(null).getTaskUid());
        String[] resetRankingRule = index.getRankingRulesSettings();

        assertNotEquals(newRankingRule.length, resetRankingRule.length);
        assertEquals(initialRankingRule.length, resetRankingRule.length);
        assertArrayEquals(initialRankingRule, resetRankingRule);
    }

    @Test
    @DisplayName("Test update searchable attributes settings when null is passed")
    public void testUpdateSearchableAttributesSettingssUsingNull() throws Exception {
        Index index = createIndex("testUpdateSearchableAttributesSettingssUsingNull");
        String[] initialSearchableAttributes = index.getSearchableAttributesSettings();
        String[] newSearchableAttributes = {"title", "release_date", "cast"};

        index.waitForTask(
                index.updateSearchableAttributesSettings(newSearchableAttributes).getTaskUid());

        String[] updatedSearchableAttributes = index.getSearchableAttributesSettings();
        index.waitForTask(index.updateSearchableAttributesSettings(null).getTaskUid());
        String[] resetSearchableAttributes = index.getSearchableAttributesSettings();

        assertNotEquals(initialSearchableAttributes.length, updatedSearchableAttributes.length);
        assertNotEquals(updatedSearchableAttributes.length, resetSearchableAttributes.length);
        assertEquals(initialSearchableAttributes.length, resetSearchableAttributes.length);
        assertArrayEquals(initialSearchableAttributes, resetSearchableAttributes);
    }

    @Test
    @DisplayName("Test update display attributes settings when null is passed")
    public void testUpdateDisplayedAttributesSettingsUsingNull() throws Exception {
        Index index = createIndex("testUpdateDisplayedAttributesSettingsUsingNull");
        String[] initialDisplayedAttributes = index.getDisplayedAttributesSettings();
        String[] newDisplayedAttributes = {"title", "genre", "release_date"};

        index.waitForTask(
                index.updateDisplayedAttributesSettings(newDisplayedAttributes).getTaskUid());
        String[] updatedDisplayedAttributes = index.getDisplayedAttributesSettings();

        index.waitForTask(index.updateDisplayedAttributesSettings(null).getTaskUid());
        String[] resetDisplayedAttributes = index.getDisplayedAttributesSettings();

        assertNotEquals(initialDisplayedAttributes.length, updatedDisplayedAttributes.length);
        assertNotEquals(updatedDisplayedAttributes.length, resetDisplayedAttributes.length);
        assertEquals(initialDisplayedAttributes.length, resetDisplayedAttributes.length);
        assertArrayEquals(initialDisplayedAttributes, resetDisplayedAttributes);
    }

    @Test
    @DisplayName("Test update filterable attributes settings when null is passed")
    public void testUpdateFilterableAttributesSettingsUsingNull() throws Exception {
        Index index = createIndex("testUpdateFilterableAttributesSettingsUsingNull");
        String[] initialFilterableAttributes = index.getFilterableAttributesSettings();
        String[] newFilterableAttributes = {"title", "genres", "cast", "release_date"};

        index.waitForTask(
                index.updateFilterableAttributesSettings(newFilterableAttributes).getTaskUid());
        String[] updatedFilterableAttributes = index.getFilterableAttributesSettings();

        index.waitForTask(index.updateFilterableAttributesSettings(null).getTaskUid());
        String[] resetFilterableAttributes = index.getFilterableAttributesSettings();

        assertNotEquals(updatedFilterableAttributes.length, resetFilterableAttributes.length);
        assertNotEquals(initialFilterableAttributes.length, updatedFilterableAttributes.length);
        assertEquals(initialFilterableAttributes.length, resetFilterableAttributes.length);
        assertArrayEquals(initialFilterableAttributes, resetFilterableAttributes);
    }

    @Test
    @DisplayName("Test update distinct attribute settings when null is passed")
    public void testUpdateDistinctAttributeSettingsUsingNull() throws Exception {
        Index index = createIndex("testUpdateDistinctAttributeSettingsUsingNull");
        String initialDistinctAttribute = index.getDistinctAttributeSettings();
        String newDistinctAttribute = "genres";

        index.waitForTask(index.updateDistinctAttributeSettings(newDistinctAttribute).getTaskUid());
        String updatedDistinctAttribute = index.getDistinctAttributeSettings();

        index.waitForTask(index.updateDistinctAttributeSettings(null).getTaskUid());
        String resetDistinctAttribute = index.getDistinctAttributeSettings();

        assertNotEquals(updatedDistinctAttribute, resetDistinctAttribute);
        assertNotEquals(initialDistinctAttribute, updatedDistinctAttribute);
        assertEquals(initialDistinctAttribute, resetDistinctAttribute);
    }

    /** Tests of the pagination setting methods */
    @Test
    @DisplayName("Test get pagination settings by uid")
    public void testGetPaginationSettings() throws Exception {
        Index index = createIndex("testGetPaginationSettings");
        Settings initialSettings = index.getSettings();
        Pagination initialPagination = index.getPaginationSettings();

        assertEquals(initialSettings.getPagination().getMaxTotalHits(), 1000);
        assertNotNull(initialPagination.getMaxTotalHits());
    }

    @Test
    @DisplayName("Test update pagination settings")
    public void testUpdatePaginationSettings() throws Exception {
        Index index = createIndex("testUpdatePaginationSettings");
        Pagination newPagination = new Pagination();

        Integer MaxTotalHitsTypos = 100;

        newPagination.setMaxTotalHits(MaxTotalHitsTypos);
        index.waitForTask(index.updatePaginationSettings(newPagination).getTaskUid());
        Pagination updatedPagination = index.getPaginationSettings();

        assertEquals(100, updatedPagination.getMaxTotalHits());
    }

    @Test
    @DisplayName("Test reset pagination settings")
    public void testResetPaginationSettings() throws Exception {
        Index index = createIndex("testResetPaginationSettings");

        Pagination initialPagination = index.getPaginationSettings();
        Pagination newPagination = new Pagination();

        Integer MaxTotalHitsTypos = 100;
        newPagination.setMaxTotalHits(MaxTotalHitsTypos);
        index.waitForTask(index.updatePaginationSettings(newPagination).getTaskUid());
        Pagination updatedPagination = index.getPaginationSettings();

        index.waitForTask(index.resetPaginationSettings().getTaskUid());
        Pagination paginationAfterReset = index.getPaginationSettings();

        assertEquals(1000, initialPagination.getMaxTotalHits());
        assertEquals(100, updatedPagination.getMaxTotalHits());
        assertEquals(1000, paginationAfterReset.getMaxTotalHits());
    }

    /** Tests of the faceting setting methods */
    @Test
    @DisplayName("Test get faceting settings by uid")
    public void testGetFacetingSettings() throws Exception {
        Index index = createIndex("testGetFacetingSettings");
        Settings initialSettings = index.getSettings();
        Faceting initialFaceting = index.getFacetingSettings();

        assertEquals(initialSettings.getFaceting().getMaxValuesPerFacet(), 100);
        assertNotNull(initialFaceting.getMaxValuesPerFacet());
    }

    @Test
    @DisplayName("Test update faceting settings")
    public void testUpdateFacetingSettings() throws Exception {
        Index index = createIndex("testUpdateFacetingSettings");
        Faceting newFaceting = new Faceting();

        Integer MaxValuesPerFacetTypos = 200;

        newFaceting.setMaxValuesPerFacet(MaxValuesPerFacetTypos);
        index.waitForTask(index.updateFacetingSettings(newFaceting).getTaskUid());
        Faceting updatedFaceting = index.getFacetingSettings();

        assertEquals(200, updatedFaceting.getMaxValuesPerFacet());
    }

    @Test
    @DisplayName("Test reset faceting settings")
    public void testResetFacetingSettings() throws Exception {
        Index index = createIndex("testResetFacetingSettings");

        Faceting initialFaceting = index.getFacetingSettings();
        Faceting newFaceting = new Faceting();

        Integer MaxValuesPerFacetTypos = 200;
        newFaceting.setMaxValuesPerFacet(MaxValuesPerFacetTypos);
        index.waitForTask(index.updateFacetingSettings(newFaceting).getTaskUid());
        Faceting updatedFaceting = index.getFacetingSettings();

        index.waitForTask(index.resetFacetingSettings().getTaskUid());
        Faceting facetingAfterReset = index.getFacetingSettings();

        assertEquals(100, initialFaceting.getMaxValuesPerFacet());
        assertEquals(200, updatedFaceting.getMaxValuesPerFacet());
        assertEquals(100, facetingAfterReset.getMaxValuesPerFacet());
    }

    private Index createIndex(String indexUid) throws Exception {
        Index index = client.index(indexUid);
        TaskInfo updateInfo = index.addDocuments(testData.getRaw());
        index.waitForTask(updateInfo.getTaskUid());

        return index;
    }
}
