package com.meilisearch.sdk;

import com.meilisearch.sdk.http.AbstractHttpClient;
import com.meilisearch.sdk.http.HttpClient;
import com.meilisearch.sdk.http.factory.RequestFactory;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.http.response.HttpResponse;
import com.meilisearch.sdk.json.JsonHandler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GenericServiceTemplate implements ServiceTemplate {
	private final AbstractHttpClient client;
	private final JsonHandler processor;
	private final RequestFactory requestFactory;

	/**
	 * @param client         a {@link HttpClient}
	 * @param processor      a {@link JsonHandler}
	 * @param requestFactory a {@link RequestFactory}
	 */
	public GenericServiceTemplate(AbstractHttpClient client, JsonHandler processor, RequestFactory requestFactory) {
		this.client = client;
		this.processor = processor;
		this.requestFactory = requestFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractHttpClient getClient() {
		return client;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JsonHandler getProcessor() {
		return processor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RequestFactory getRequestFactory() {
		return requestFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T execute(HttpRequest<?> request, Class<?> targetClass, Class<?>... parameter) throws RuntimeException {
		try {
			if (targetClass == null) {
				return (T) makeRequest(request);
			}
			return (T) CompletableFuture
				.completedFuture(makeRequest(request))
				.thenApply(httpResponse -> decodeResponse(httpResponse.getContent(), targetClass, parameter))
				.get();
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			// todo: throw dedicated exception
			throw new RuntimeException(e);
		}
	}

	private <T> T decodeResponse(Object o, Class<?> targetClass, Class<?>... parameters) {
		try {
			return processor.decode(o, targetClass, parameters);
		} catch (Exception e) {
			// todo: throw dedicated exception
			throw new RuntimeException(e);
		}
	}

	/**
	 * Executes the given {@link HttpRequest}
	 *
	 * @param request the {@link HttpRequest}
	 * @return the HttpResponse
	 * @throws Exception in case there are Problems with the Request, including error codes
	 */
	private HttpResponse<?> makeRequest(HttpRequest<?> request) {
		try {
			switch (request.getMethod()) {
				case GET:
					return client.get(request);
				case POST:
					return client.post(request);
				case PUT:
					return client.put(request);
				case DELETE:
					return client.delete(request);
				default:
					throw new IllegalStateException("Unexpected value: " + request.getMethod());
			}
		} catch (Exception e) {
			// todo: throw dedicated exception
			throw new RuntimeException(e);
		}
	}
}
