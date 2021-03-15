package com.meilisearch.sdk.api.documents;

import com.meilisearch.sdk.ServiceTemplate;
import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;
import com.meilisearch.sdk.http.factory.RequestFactory;
import com.meilisearch.sdk.http.request.HttpMethod;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DocumentHandler<T> {
	private final ServiceTemplate serviceTemplate;
	private final RequestFactory requestFactory;
	private final String indexName;
	private final Class<?> indexModel;

	public DocumentHandler(ServiceTemplate serviceTemplate, RequestFactory requestFactory, String indexName, Class<?> indexModel) {
		this.serviceTemplate = serviceTemplate;
		this.requestFactory = requestFactory;
		this.indexName = indexName;
		this.indexModel = indexModel;
	}

	public Class<?> getIndexModel() {
		return indexModel;
	}

	/**
	 * Retrieve a document with a specific identifier
	 *
	 * @param identifier the identifier of the document you are looking for
	 * @return the document specified by the identifier
	 * @throws com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public T getDocument(String identifier) throws MeiliSearchRuntimeException {
		String requestQuery = "/indexes/" + indexName + "/documents/" + identifier;
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
			indexModel
		);
	}

	/**
	 * Retrieve a list of documents
	 *
	 * @return a list of Documents from the index.
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public List<T> getDocuments() throws MeiliSearchRuntimeException {
		return getDocuments(0);
	}

	/**
	 * Retrieve a list of documents
	 *
	 * @param limit maximum number of documents to be returned
	 * @return a list of Documents from the index.
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public List<T> getDocuments(int limit) throws MeiliSearchRuntimeException {
		String requestQuery = "/indexes/" + indexName + "/documents";
		if (limit > 0) {
			requestQuery += "?limit=" + limit;
		}
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
			List.class,
			indexModel
		);
	}

	/**
	 * Add or replace a document
	 *
	 * @param data an already serialized document
	 * @return an Update object with the updateId
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Update addDocuments(String data) throws MeiliSearchRuntimeException {
		String requestQuery = "/indexes/" + indexName + "/documents";
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.POST, requestQuery, Collections.emptyMap(), data),
			Update.class
		);
	}

	/**
	 * Add or replace a batch of documents
	 *
	 * @param data a list of document objects
	 * @return an Update object with the updateId
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Update addDocuments(List<T> data) throws MeiliSearchRuntimeException {
		try {
			String dataString = serviceTemplate.getProcessor().encode(data);
			return addDocuments(dataString);
		} catch (Exception e) {
			throw new MeiliSearchRuntimeException(e);
		}
	}

	/**
	 * Add or replace a document
	 *
	 * @param data the serialized document
	 * @return an Update object with the updateId
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Update replaceDocuments(String data) throws MeiliSearchRuntimeException {
		return addDocuments(data);
	}

	/**
	 * Add or replace a batch of documents
	 *
	 * @param data a list of document objects
	 * @return an Update object with the updateId
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Update replaceDocuments(List<T> data) throws MeiliSearchRuntimeException {
		try {
			String dataString = serviceTemplate.getProcessor().encode(data);
			return replaceDocuments(dataString);
		} catch (Exception e) {
			throw new MeiliSearchRuntimeException(e);
		}
	}

	/**
	 * Add or update a document
	 *
	 * @param data the serialized document
	 * @return an Update object with the updateId
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Update updateDocuments(String data) throws MeiliSearchRuntimeException {
		String requestQuery = "/indexes/" + indexName + "/documents";
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.PUT, requestQuery, Collections.emptyMap(), data),
			Update.class
		);
	}

	/**
	 * Add or update a document
	 *
	 * @param data a list of document objects
	 * @return an Update object with the updateId
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Update updateDocuments(List<T> data) throws MeiliSearchRuntimeException {
		try {
			String dataString = serviceTemplate.getProcessor().encode(data);
			return updateDocuments(dataString);
		} catch (Exception e) {
			throw new MeiliSearchRuntimeException(e);
		}
	}

	/**
	 * Delete a document with a specific identifier
	 *
	 * @param identifier the id of the document
	 * @return an Update object with the updateId
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Update deleteDocument(String identifier) throws MeiliSearchRuntimeException {
		String requestQuery = "/indexes/" + indexName + "/documents/" + identifier;
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null),
			Update.class
		);
	}

	/**
	 * Delete a batch of documents
	 *
	 * @return an Update object with the updateId
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Update deleteDocuments() throws MeiliSearchRuntimeException {
		String requestQuery = "/indexes/" + indexName + "/documents";
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null),
			Update.class
		);
	}

	/**
	 * @param data a list of model to delete
	 * @return an Update object with the updateId
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Update deleteDocuments(List<Integer> data) throws MeiliSearchRuntimeException {
		String requestQuery = "/indexes/" + indexName + "/documents/delete-batch";
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null),
			Update.class
		);
	}

	/**
	 * @param data a list of model to delete
	 * @param uidResolverFunc a function to resolve the id
	 * @return an Update object with the updateId
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Update deleteDocuments(List<T> data, Function<T, Integer> uidResolverFunc) throws MeiliSearchRuntimeException {
		List<Integer> documentsToDelete = data.stream().map(uidResolverFunc).collect(Collectors.toList());
		String requestQuery = "/indexes/" + indexName + "/documents/delete-batch";
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.DELETE, requestQuery, Collections.emptyMap(), documentsToDelete),
			Update.class
		);
	}

	/**
	 * @param q the Querystring
	 * @return a SearchResponse with the Hits represented by the mapped Class for this index
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public SearchResponse<T> search(String q) throws MeiliSearchRuntimeException {
		try {
			String requestQuery = "/indexes/" + indexName + "/search";
			SearchRequest sr = new SearchRequest(q);
			return serviceTemplate.execute(
				requestFactory.create(HttpMethod.POST, requestQuery, Collections.emptyMap(), serviceTemplate.getProcessor().encode(sr)),
				SearchResponse.class,
				indexModel
			);
		} catch (Exception e) {
			throw new MeiliSearchRuntimeException(e);
		}
	}

	/**
	 * @param sr SearchRequest
	 * @return a SearchResponse with the Hits represented by the mapped Class for this index
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public SearchResponse<T> search(SearchRequest sr) throws MeiliSearchRuntimeException {
		try {
			String requestQuery = "/indexes/" + indexName + "/search";
			return serviceTemplate.execute(
				requestFactory.create(HttpMethod.POST, requestQuery, Collections.emptyMap(), serviceTemplate.getProcessor().encode(sr)),
				SearchResponse.class,
				indexModel
			);
		} catch (Exception e) {
			throw new MeiliSearchRuntimeException(e);
		}
	}

	/**
	 * Retrieve an update with a specific updated
	 *
	 * @param updateId the updateId
	 * @return the update belonging to the updateID
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public Update getUpdate(int updateId) throws MeiliSearchRuntimeException {
		String requestQuery = "/indexes/" + indexName + "/updates/" + updateId;
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
			Update.class
		);
	}

	/**
	 * Retrieve a list containing all the updates
	 *
	 * @return a List of Updates
	 * @throws MeiliSearchRuntimeException in case something went wrong (http error, json exceptions, etc)
	 */
	public List<Update> getUpdates() throws MeiliSearchRuntimeException {
		String requestQuery = "/indexes/" + indexName + "/updates";
		return serviceTemplate.execute(
			requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
			List.class,
			Update.class
		);
	}

	/**
	 * Waits for a pending update to be processed
	 *
	 * @param updateId ID of the index update
	 * @throws TimeoutException if timeout is reached
	 */
	public void waitForPendingUpdate(int updateId) throws TimeoutException {
		this.waitForPendingUpdate(updateId, 5000, 50);
	}

	/**
	 * Waits for a pending update to be processed
	 *
	 * @param updateId     ID of the index update
	 * @param timeoutInMs  number of milliseconds before throwing an Exception
	 * @param intervalInMs number of milliseconds before requesting the status again
	 * @throws TimeoutException if timeout is reached
	 */
	public void waitForPendingUpdate(int updateId, int timeoutInMs, int intervalInMs) throws TimeoutException {
		Update update = new Update();
		long startTime = new Date().getTime();
		long elapsedTime = 0;

		while (!"processed".equalsIgnoreCase(update.getStatus())) {
			if (elapsedTime >= timeoutInMs) {
				throw new TimeoutException();
			}
			update = this.getUpdate(updateId);
			try {
				Thread.sleep(intervalInMs);
			} catch (InterruptedException e) {
				throw new TimeoutException();
			}
			elapsedTime = new Date().getTime() - startTime;
		}
	}
}
