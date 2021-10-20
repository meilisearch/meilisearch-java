package com.meilisearch.sdk.api.index;

import com.meilisearch.sdk.ServiceTemplate;
import com.meilisearch.sdk.api.documents.Update;
import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;
import com.meilisearch.sdk.http.factory.RequestFactory;
import com.meilisearch.sdk.http.request.HttpMethod;
import java.util.Collections;

public class SettingsHandler {
    private final ServiceTemplate serviceTemplate;
    private final RequestFactory requestFactory;

    public SettingsHandler(ServiceTemplate serviceTemplate, RequestFactory requestFactory) {
        this.serviceTemplate = serviceTemplate;
        this.requestFactory = requestFactory;
    }

    /**
     * Gets the settings of a given index Refer
     * https://docs.meilisearch.com/references/settings.html#get-settings
     *
     * @param uid Index identifier
     * @return settings of a given uid as String
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Settings getSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings";
        return serviceTemplate.execute(
                requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
                Settings.class);
    }

    /**
     * Updates the settings of a given index Refer
     * https://docs.meilisearch.com/references/settings.html#update-settings
     *
     * @param uid Index identifier
     * @param settings the data that contains the new settings
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update updateSettings(String uid, Settings settings) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.POST, requestQuery, Collections.emptyMap(), settings),
                Update.class);
    }

    /**
     * Resets the settings of a given index Refer
     * https://docs.meilisearch.com/references/settings.html#reset-settings
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update resetSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null),
                Update.class);
    }

    /**
     * Gets the ranking rules settings of a given index Refer
     * https://docs.meilisearch.com/references/settings.html#get-settings
     *
     * @param uid Index identifier
     * @return ranking rules settings of a given uid as String
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Settings getRankingRulesSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/ranking-rules";
        return serviceTemplate.execute(
                requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
                Settings.class);
    }

    /**
     * Updates the ranking rules settings of a given index Refer
     * https://docs.meilisearch.com/references/settings.html#update-settings
     *
     * @param uid Index identifier
     * @param rankingRulesSettings the data that contains the new ranking rules settings
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update updateRankingRulesSettings(String uid, String[] rankingRulesSettings)
            throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/ranking-rules";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.POST,
                        requestQuery,
                        Collections.emptyMap(),
                        rankingRulesSettings),
                Update.class);
    }

    /**
     * Resets the ranking rules settings of a given index Refer
     * https://docs.meilisearch.com/references/settings.html#reset-settings
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update resetRankingRulesSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/ranking-rules";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null),
                Update.class);
    }
}
