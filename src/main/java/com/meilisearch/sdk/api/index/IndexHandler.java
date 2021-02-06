package com.meilisearch.sdk.api.index;

import com.meilisearch.sdk.ServiceTemplate;
import com.meilisearch.sdk.api.documents.Update;
import com.meilisearch.sdk.api.instance.IndexStats;
import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;
import com.meilisearch.sdk.http.factory.RequestFactory;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.response.HttpResponse;

import java.util.Collections;
import java.util.HashMap;

public class IndexHandler {
	private final ServiceTemplate serviceTemplate;
	private final RequestFactory requestFactory;
	private final SettingsHandler settingsHandler;

	public IndexHandler(ServiceTemplate serviceTemplate, RequestFactory requestFactory, SettingsHandler settingsHandler) {
		this.serviceTemplate = serviceTemplate;
		this.requestFactory = requestFactory;
		this.settingsHandler = settingsHandler;
	}

	public IndexHandler(ServiceTemplate serviceTemplate, RequestFactory requestFactory) throws MeiliSearchRuntimeException {
		this(serviceTemplate, requestFactory, new SettingsHandler(serviceTemplate, requestFactory));
	}

	/**
	 * @param uid the uid of the index to be created
	 * @return an {@link Index} Object
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Index createIndex(String uid) throws MeiliSearchRuntimeException {
		return this.createIndex(uid, null);
	}

	/**
	 * @param uid        the indexname
	 * @param primaryKey the primaryKey for that index
	 * @return the newly created index
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Index createIndex(String uid, String primaryKey) throws MeiliSearchRuntimeException {
		HashMap<String, String> body = new HashMap<>();
		body.put("uid", uid);
		if (primaryKey != null)
			body.put("primaryKey", primaryKey);
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.POST, "/indexes", Collections.emptyMap(), body),
			Index.class
		);
	}

	/**
	 * @param uid the indexname
	 * @return the index
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Index getIndex(String uid) throws MeiliSearchRuntimeException {
		String requestQuery = "/indexes/" + uid;
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
			Index.class
		);
	}

	/**
	 * @return an array of all indexes
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Index[] getAllIndexes() throws MeiliSearchRuntimeException {
		String requestQuery = "/indexes";
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
			Index[].class
		);
	}

	/**
	 * @param uid        the indexname
	 * @param primaryKey the primaryKey for that index
	 * @return the updated index
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Index updateIndex(String uid, String primaryKey) throws MeiliSearchRuntimeException {
		String requestQuery = "/indexes/" + uid;
		HashMap<String, String> body = new HashMap<>();
		body.put("primaryKey", primaryKey);
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.PUT, requestQuery, Collections.emptyMap(), body),
			Index.class
		);
	}

	/**
	 * @param uid the indexname
	 * @return true if the index was deleted, otherwise false
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public boolean deleteIndex(String uid) throws MeiliSearchRuntimeException {
		String requestQuery = "/indexes/" + uid;
		return ((HttpResponse<?>) serviceTemplate.execute(requestFactory.create(HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null), null)).getStatusCode() == 204;
	}

	/**
	 * @param index the indexname
	 * @return the index settings
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Settings getSettings(String index) throws MeiliSearchRuntimeException {
		return settingsHandler.getSettings(index);
	}

	/**
	 * @param index    the indexname
	 * @param settings the new Settings
	 * @return the updated settings
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Update updateSettings(String index, Settings settings) throws MeiliSearchRuntimeException {
		return settingsHandler.updateSettings(index, settings);
	}

	/**
	 * @param index the indexname
	 * @return the settings
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Update resetSettings(String index) throws MeiliSearchRuntimeException {
		return settingsHandler.resetSettings(index);
	}


	/**
	 * Get Index Stats
	 * Refer https://docs.meilisearch.com/reference/api/stats.html#get-stat-of-an-index
	 *
	 * @return Index Stats
	 * @throws MeiliSearchRuntimeException if something goes wrong
	 */
	public IndexStats getStats(String index) {
		String requestQuery = "/indexes/" + index + "/stats";
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
			IndexStats.class
		);
	}
}
