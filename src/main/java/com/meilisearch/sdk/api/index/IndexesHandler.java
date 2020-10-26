package com.meilisearch.sdk.api.index;

import com.meilisearch.sdk.ServiceTemplate;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.http.response.HttpResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Wrapper around the MeilisearchHttpRequest class to ease usage for Meilisearch indexes
 */
public class IndexesHandler {
	private final ServiceTemplate serviceTemplate;

	/**
	 * Create and setup an instance of IndexesHandler to simplify Meilisearch API calls to manage indexes
	 *
	 * @param serviceTemplate a {@link ServiceTemplate}
	 */
	public IndexesHandler(ServiceTemplate serviceTemplate) {
		this.serviceTemplate = serviceTemplate;
	}

	/**
	 * Create an index with a unique identifier
	 *
	 * @param uid Unique identifier to create the index with
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public Index create(String uid) throws Exception {
		return this.create(uid, null);
	}

	/**
	 * Create an index with a unique identifier
	 *
	 * @param uid        Unique identifier to create the index with
	 * @param primaryKey Field to use as the primary key for documents in that index
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public Index create(String uid, String primaryKey) throws Exception {
		HashMap<String, String> body = new HashMap<>();
		body.put("uid", uid);
		if (primaryKey != null) {
			body.put("primaryKey", primaryKey);
		}
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.POST, "/indexes", Collections.emptyMap(), body);
		return serviceTemplate.execute(request, Index.class);
	}

	/**
	 * Get an index from its unique identifier
	 *
	 * @param uid Unique identifier of the index to get
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public Index get(String uid) throws Exception {
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.GET, "/indexes/" + uid, Collections.emptyMap(), null);
		return serviceTemplate.execute(request, Index.class);
	}

	/**
	 * Get all indexes in the current Meilisearch instance
	 *
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public List<Index> getAll() throws Exception {
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.GET, "/indexes", Collections.emptyMap(), null);
		return serviceTemplate.execute(request, List.class, Index.class);
	}

	/**
	 * Update the primary key of an index in the Meilisearch instance
	 *
	 * @param uid        Unique identifier of the index to update
	 * @param primaryKey New primary key field to use for documents in that index
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public Index updatePrimaryKey(String uid, String primaryKey) throws Exception {
		HashMap<String, String> body = new HashMap<>();
		body.put("primaryKey", primaryKey);

		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.PUT , "/indexes/" + uid, Collections.emptyMap(), body);
		return serviceTemplate.execute(request, Index.class);
	}

	/**
	 * Delete an index in the Meilisearch instance
	 *
	 * @param uid Unique identifier of the index to delete
	 * @return Meilisearch API response
	 * @throws Exception If something goes wrong
	 */
	public boolean delete(String uid) throws Exception {
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.GET, "/indexes/" + uid, Collections.emptyMap(), null);
		return ((HttpResponse<?>) serviceTemplate.execute(request, null)).getStatusCode() == 204;
	}

	/**
	 * Get an index update by its id
	 *
	 * @param updateId ID of the index update
	 * @return UpdateStatus
	 * @throws Exception If something goes wrong
	 */
	public UpdateStatus getUpdate(String indexName, int updateId) throws RuntimeException {
		String requestQuery = "/indexes/" + indexName + "/updates/" + updateId;
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null);
		return serviceTemplate.execute(request, UpdateStatus.class);
	}

	/**
	 * Get all updates of the index
	 *
	 * @return List of updates in the index
	 * @throws Exception If something goes wrong
	 */
	public List<UpdateStatus> getUpdates(String indexName) throws Exception {
		String requestQuery = "/indexes/" + indexName + "/updates/";
		HttpRequest<?> request = serviceTemplate.getRequestFactory().create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null);
		return serviceTemplate.execute(request, List.class, UpdateStatus.class);
	}

	/**
	 * Wait for a pending update to be processed
	 *
	 * @param updateId ID of the index update
	 * @return UpdateStatus
	 * @throws Exception If timeout is reached
	 */
	public UpdateStatus waitForPendingUpdate(String indexName, int updateId) {
		return this.waitForPendingUpdate(indexName, updateId, 1000, 50);
	}

	/**
	 * Wait for a pending update to be processed
	 *
	 * @param updateId     ID of the index update
	 * @param timeoutInMs  number of milliseconds before throwing an Exception
	 * @param intervalInMs number of milliseconds before requesting the status again
	 * @return UpdateStatus
	 * @throws Exception if timeout is reached
	 */
	public UpdateStatus waitForPendingUpdate(String indexName, int updateId, int timeoutInMs, int intervalInMs) {
		UpdateStatus updateStatus = this.getUpdate(indexName, updateId);
		while (!"processed".equalsIgnoreCase(updateStatus.getStatus())) {
			try {
				Thread.sleep(intervalInMs);
			} catch (InterruptedException e) { /* noop */}
			updateStatus = this.getUpdate(indexName, updateId);
		}
		return updateStatus;
	}
}
