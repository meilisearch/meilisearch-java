/*
 * Unofficial Java client for MeiliSearch
 */
package com.meilisearch.sdk;

import com.meilisearch.sdk.api.documents.DocumentHandler;
import com.meilisearch.sdk.api.index.IndexHandler;
import com.meilisearch.sdk.api.instance.InstanceHandler;
import com.meilisearch.sdk.api.keys.KeysHandler;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * MeiliSearch client
 */
public class Client {
	private final IndexHandler indexHandler;
	private final InstanceHandler instanceHandler;
	private final KeysHandler keysHandler;
	private final Map<String, DocumentHandler<?>> handlerMap;
	private final ServiceTemplate serviceTemplate;

	Client(Config config, ServiceTemplate serviceTemplate) {
		this.serviceTemplate = serviceTemplate;
		this.indexHandler = new IndexHandler(serviceTemplate, serviceTemplate.getRequestFactory());
		this.instanceHandler = new InstanceHandler(serviceTemplate, serviceTemplate.getRequestFactory());
		this.keysHandler = new KeysHandler(serviceTemplate, serviceTemplate.getRequestFactory());
		this.handlerMap = config.getModelMapping()
			.entrySet()
			.stream()
			.collect(
				Collectors.toMap(
					Map.Entry::getKey,
					entry -> new DocumentHandler<>(serviceTemplate, serviceTemplate.getRequestFactory(), entry.getKey(), entry.getValue())
				)
			);
	}

	public IndexHandler index() {
		return this.indexHandler;
	}

	@SuppressWarnings("unchecked")
	public <T> DocumentHandler<T> documents(String uid) {
		return (DocumentHandler<T>) this.handlerMap.get(uid);
	}

	public <T> DocumentHandler<T> documents(String uid, Class<T> model) {
		DocumentHandler<T> handler = documents(uid);
		if (handler != null) {
			if (handler.getIndexModel() == model) {
				return handler;
			}
		}
		handler = new DocumentHandler<>(serviceTemplate, serviceTemplate.getRequestFactory(), uid, model);
		handlerMap.put(uid, handler);

		return handler;
	}

	public InstanceHandler instance() {
		return instanceHandler;
	}

	public KeysHandler keys() {
		return keysHandler;
	}
}
