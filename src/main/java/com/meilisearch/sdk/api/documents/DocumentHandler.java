package com.meilisearch.sdk.api.documents;

import com.meilisearch.sdk.ServiceTemplate;
import com.meilisearch.sdk.api.index.UpdateStatus;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.request.HttpRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Wrapper around MeilisearchHttpRequest class to use for Meilisearch documents
 */
public class DocumentHandler<T> {
	private final ServiceTemplate serviceTemplate;
	private final String indexName;
	private final Class<T> model;

	public DocumentHandler(String indexName, Class<T> model, ServiceTemplate serviceTemplate) {
		this.serviceTemplate = serviceTemplate;
		this.indexName = indexName;
		this.model = model;
	}

	public T getDocument(String identifier) {
		String requestQuery = "/indexes/" + indexName + "/documents/" + identifier;
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null);
		return serviceTemplate.execute(request, model);
	}

	public List<T> getDocuments() throws Exception {
		return getDocuments(Collections.emptyMap());
	}

	public List<T> getDocuments(Map<String, String> params) {
		String query = params.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
		String requestQuery = "/indexes/" + indexName + "/documents?" + query;
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null);
		return serviceTemplate.execute(request, List.class, model);
	}

	public UpdateStatus addDocuments(String document) {
		String requestQuery = "/indexes/" + indexName + "/documents";
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.POST, requestQuery, Collections.emptyMap(), document);
		return serviceTemplate.execute(request, UpdateStatus.class);
	}

	public UpdateStatus addDocuments(List<T> document) {
		String requestQuery = "/indexes/" + indexName + "/documents";
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.POST, requestQuery, Collections.emptyMap(), document);
		return serviceTemplate.execute(request, UpdateStatus.class);
	}

	public UpdateStatus deleteDocument(String identifier) {
		String requestQuery = "/indexes/" + indexName + "/documents/" + identifier;
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null);
		return serviceTemplate.execute(request, UpdateStatus.class);
	}

	public UpdateStatus deleteDocuments() {
		String requestQuery = "/indexes/" + indexName + "/documents";
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null);
		return serviceTemplate.execute(request, UpdateStatus.class);
	}

	public T search(String uid, String q) {
		String requestQuery = "/indexes/" + uid + "/search";
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.POST, requestQuery, Collections.emptyMap(), new SearchRequest(q));
		return serviceTemplate.execute(request, model);
	}

	public T search(String uid,
					String q,
					int offset,
					int limit,
					String attributesToRetrieve,
					String attributesToCrop,
					int cropLength,
					String attributesToHighlight,
					String filters,
					boolean matches
	) {
		String requestQuery = "/indexes/" + uid + "/search";
		SearchRequest sr = new SearchRequest(q, offset, limit, attributesToRetrieve, attributesToCrop, cropLength, attributesToHighlight, filters, matches);
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.POST, requestQuery, Collections.emptyMap(), sr);
		return serviceTemplate.execute(request, model);
	}
}
