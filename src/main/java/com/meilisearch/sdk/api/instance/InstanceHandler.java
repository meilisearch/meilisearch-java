package com.meilisearch.sdk.api.instance;

import com.meilisearch.sdk.ServiceTemplate;
import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;
import com.meilisearch.sdk.http.factory.RequestFactory;
import com.meilisearch.sdk.http.request.HttpMethod;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InstanceHandler {
	private final ServiceTemplate serviceTemplate;
	private final RequestFactory requestFactory;

	public InstanceHandler(ServiceTemplate serviceTemplate, RequestFactory requestFactory) {
		this.serviceTemplate = serviceTemplate;
		this.requestFactory = requestFactory;
	}

	/**
	 * Refer https://docs.meilisearch.com/reference/api/health.html
	 *
	 * @return true if everything is ok, false if meilisearch is in maintenance mode
	 */
	public boolean isHealthy() {
		try {
			serviceTemplate.execute(requestFactory.create(HttpMethod.GET, "/health", Collections.emptyMap(), null), null);
			return true;
		} catch (MeiliSearchRuntimeException e) {
			return false;
		}
	}

	/**
	 * Refer https://docs.meilisearch.com/reference/api/stats.html
	 *
	 * @return a map with version information of meilisearch
	 */
	public Map<String, String> getVersion() {
		try {
			return serviceTemplate.execute(
				requestFactory.create(HttpMethod.GET, "/version", Collections.emptyMap(), null),
				HashMap.class,
				String.class,
				String.class
			);
		} catch (MeiliSearchRuntimeException e) {
			return Collections.emptyMap();
		}
	}

	/**
	 * Get Stats
	 * Refer https://docs.meilisearch.com/reference/api/stats.html
	 *
	 * @return Instance Stats
	 * @throws MeiliSearchRuntimeException if something goes wrong
	 */
	public Stats getStats() {
		String requestQuery = "/stats";
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
			Stats.class
		);
	}

	/**
	 * Creates a dump
	 * Refer https://docs.meilisearch.com/references/dump.html#create-a-dump
	 *
	 * @return Dump status
	 * @throws MeiliSearchRuntimeException if something goes wrong
	 */
	public Dump createDump() {
		String requestQuery = "/dumps";
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.POST, requestQuery, Collections.emptyMap(), null),
			Dump.class
		);
	}

	/**
	 * Gets dump status
	 * Refer https://docs.meilisearch.com/references/dump.html#get-dump-status
	 *
	 * @param uid Unique identifier for correspondent dump
	 * @return dump status
	 * @throws MeiliSearchRuntimeException if something goes wrong
	 */
	public Dump getDumpStatus(String uid) {
		String requestQuery = "/dumps/" + uid + "/status";
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
			Dump.class
		);
	}

}
