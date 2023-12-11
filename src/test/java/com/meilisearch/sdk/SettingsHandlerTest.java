package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.model.Settings;
import com.meilisearch.sdk.model.TaskInfo;
import com.meilisearch.sdk.utils.Movie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SettingsHandlerTest extends AbstractIT {

    private TestData<Movie> testData;

    @BeforeEach
    public void initialize() {
        setUp();
        setUpJacksonClient();
        if (testData == null) testData = this.getTestData(MOVIES_INDEX, Movie.class);
        cleanup();
    }

    @AfterAll
    static void cleanMeilisearch() {
        cleanup();
    }

    /** Tests of the dictionary setting methods */
    @Test
    @DisplayName("Test get dictionary settings by uid")
    public void testGetDictionarySettings() throws Exception {
        Index index = createIndex("testGetDictionarySettings");
        Settings initialSettings = index.getSettings();
        String[] initialDictionary = index.getDictionarySettings();

        assertThat(initialDictionary, is(arrayWithSize(initialSettings.getDictionary().length)));
        assertThat(initialDictionary, is(equalTo(initialSettings.getDictionary())));
    }

    @Test
    @DisplayName("Test update dictionary settings")
    public void testUpdateDictionarySettings() throws Exception {
        Index index = createIndex("testUpdateDictionarySettings");
        String[] initialDictionary = index.getDictionarySettings();
        String[] newDictionary = {"J. R. R.", "W. E. B."};

        index.waitForTask(index.updateDictionarySettings(newDictionary).getTaskUid());
        String[] updatedDictionarySettings = index.getDictionarySettings();

        assertThat(updatedDictionarySettings, is(arrayWithSize(newDictionary.length)));
        assertThat(updatedDictionarySettings, is(equalTo(newDictionary)));
        assertThat(updatedDictionarySettings, is(not(arrayWithSize(initialDictionary.length))));
    }

    @Test
    @DisplayName("Test reset dictionary settings")
    public void testResetDictionarySettings() throws Exception {
        Index index = createIndex("testResetDictionarySettings");
        String[] initialDictionary = index.getDictionarySettings();
        String[] newDictionary = {"J. R. R.", "W. E. B."};

        index.waitForTask(index.updateDictionarySettings(newDictionary).getTaskUid());
        String[] updatedDictionarySettings = index.getDictionarySettings();

        index.waitForTask(index.resetDictionarySettings().getTaskUid());
        String[] dictionaryAfterReset = index.getDictionarySettings();

        assertThat(updatedDictionarySettings, is(arrayWithSize(newDictionary.length)));
        assertThat(updatedDictionarySettings, is(equalTo(newDictionary)));
        assertThat(updatedDictionarySettings, is(not(arrayWithSize(initialDictionary.length))));
        assertThat(dictionaryAfterReset, is(not(arrayWithSize(updatedDictionarySettings.length))));
        assertThat(dictionaryAfterReset, is(arrayWithSize(initialDictionary.length)));
        assertThat(dictionaryAfterReset, is(equalTo(initialDictionary)));
    }

    private Index createIndex(String indexUid) throws Exception {
        Index index = client.index(indexUid);
        TaskInfo updateInfo = index.addDocuments(testData.getRaw());
        index.waitForTask(updateInfo.getTaskUid());

        return index;
    }
}
