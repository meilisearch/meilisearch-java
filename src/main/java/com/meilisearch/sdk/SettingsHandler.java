package com.meilisearch.sdk;

import com.google.gson.Gson;

/**
 * Settings Handler for manipulation of an Index {@link Settings}
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/settings.html
 */
public class SettingsHandler {
    private final MeiliSearchHttpRequest meilisearchHttpRequest;
    private final Gson gson = new Gson();

    /**
     * Constructor for the MeiliSearch Settings object
     *
     * @param config MeiliSearch configuration
     */
    public SettingsHandler(Config config) {
        meilisearchHttpRequest = new MeiliSearchHttpRequest(config);
    }

    /**
     * Gets the settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#get-settings
     *
     * @param uid Index identifier
     * @return settings of a given uid as String
     * @throws Exception if something goes wrong
     */
    public Settings getSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.get("/indexes/" + uid + "/settings"), Settings.class);
    }

    /**
     * Updates the settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#update-settings
     *
     * @param uid Index identifier
     * @param settings the data that contains the new settings
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateSettings(String uid, Settings settings) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.post(
                        "/indexes/" + uid + "/settings", settings.getUpdateQuery()),
                UpdateStatus.class);
    }

    /**
     * Resets the settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#reset-settings
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.delete("/indexes/" + uid + "/settings"), UpdateStatus.class);
    }

    /**
     * Gets the ranking rules settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/ranking_rules.html#get-ranking-rules
     *
     * @param uid Index identifier
     * @return ranking rules settings of a given uid as String
     * @throws Exception if something goes wrong
     */
    public String[] getRankingRuleSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.get("/indexes/" + uid + "/settings/ranking-rules"),
                String[].class);
    }

    /**
     * Updates the ranking rules settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/ranking_rules.html#update-ranking-rules
     *
     * @param uid Index identifier
     * @param rankingRules the data that contains the new settings
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateRankingRuleSettings(String uid, String[] rankingRules)
            throws Exception {
        String rankingRulesAsJson = gson.toJson(rankingRules);
        return this.gson.fromJson(
                meilisearchHttpRequest.post(
                        "/indexes/" + uid + "/settings/ranking-rules", rankingRulesAsJson),
                UpdateStatus.class);
    }

    /**
     * Resets the ranking rules settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/ranking_rules.html#reset-ranking-rules
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetRankingRulesSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.delete("/indexes/" + uid + "/settings/ranking-rules"),
                UpdateStatus.class);
    }
}
