/*
 * Unofficial Java client for MeiliSearch
 */
package com.meilisearch.sdk;

import com.meilisearch.sdk.api.documents.DocumentHandler;
import com.meilisearch.sdk.api.index.IndexesHandler;
import com.meilisearch.sdk.http.ApacheHttpClient;
import com.meilisearch.sdk.http.factory.BasicRequestFactory;
import com.meilisearch.sdk.json.GsonJsonHandler;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Meilisearch client
 */
public class Client {
	private final Config config;
	private final IndexesHandler indexesHandler;
	private final Map<String, DocumentHandler<?>> handlerMap;
	private final GenericServiceTemplate serviceTemplate;

	/**
	 * Call instance for MeiliSearch client
	 *
	 * @param config Configuration to connect to Meilisearch instance
	 */
	public Client(Config config) {
		this.config = config;
		GsonJsonHandler jsonHandler = new GsonJsonHandler();
		this.serviceTemplate = new GenericServiceTemplate(new ApacheHttpClient(config), jsonHandler, new BasicRequestFactory(jsonHandler));
		this.indexesHandler = new IndexesHandler(serviceTemplate);
		this.handlerMap = config.getModelMapping()
			.entrySet()
			.stream()
			.collect(
				Collectors.toMap(
					Map.Entry::getKey,
					entry -> new DocumentHandler<>(entry.getKey(), entry.getValue(), serviceTemplate)
				)
			);
	}

	public IndexesHandler index() {
		return this.indexesHandler;
	}

	@SuppressWarnings("unchecked")
	public <T> DocumentHandler<T> documents(String uid) {
		return (DocumentHandler<T>) this.handlerMap.get(uid);
	}

	public <T> DocumentHandler<T> documents(String uid, Class<T> model) {
		return new DocumentHandler<>(uid, model, serviceTemplate);
	}
}
