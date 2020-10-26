package com.meilisearch.sdk.api.index;

import com.meilisearch.sdk.ServiceTemplate;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.request.HttpRequest;

import java.util.Collections;

public class SettingsService {

	private final ServiceTemplate serviceTemplate;

	public SettingsService(ServiceTemplate serviceTemplate) {
		this.serviceTemplate = serviceTemplate;
	}

	/**
	 * @param index the indexname
	 * @return the index settings
	 * @throws Exception in case something went wrong (http error, json exceptions, etc)
	 */
	public Settings getSettings(String index) throws Exception {
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.GET, "/indexes/" + index + "/settings", Collections.emptyMap(), null);
		return serviceTemplate.execute(
			request,
			Settings.class
		);
	}

	/**
	 * @param index    the indexname
	 * @param settings the new Settings
	 * @return the updated settings
	 * @throws Exception in case something went wrong (http error, json exceptions, etc)
	 */
	public UpdateStatus updateSettings(String index, Settings settings) throws Exception {
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.POST, "/indexes/" + index + "/settings", Collections.emptyMap(), null);
		return serviceTemplate.execute(
			request,
			UpdateStatus.class
		);
	}

	/**
	 * @param index the indexname
	 * @return the settings
	 * @throws Exception in case something went wrong (http error, json exceptions, etc)
	 */
	public UpdateStatus resetSettings(String index) throws Exception {
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.DELETE, "/indexes/" + index + "/settings", Collections.emptyMap(), null);
		return serviceTemplate.execute(
			request,
			UpdateStatus.class
		);
	}
}
