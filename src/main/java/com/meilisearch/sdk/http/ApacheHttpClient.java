package com.meilisearch.sdk.http;

import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.http.response.BasicHttpResponse;
import com.meilisearch.sdk.http.response.HttpResponse;
import org.apache.hc.client5.http.async.HttpAsyncClient;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestProducer;
import org.apache.hc.client5.http.async.methods.SimpleResponseConsumer;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


public class ApacheHttpClient extends AbstractHttpClient {

	private final HttpAsyncClient client;

	public ApacheHttpClient() {
		final IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
			.setSoTimeout(Timeout.ofSeconds(5))
			.build();

		client = HttpAsyncClients.custom()
			.setIOReactorConfig(ioReactorConfig)
			.build();
	}

	public ApacheHttpClient(HttpAsyncClient client) {
		this.client = client;
	}


	private HttpResponse<?> execute(HttpRequest<?> request) throws ExecutionException, InterruptedException {
		CompletableFuture<SimpleHttpResponse> response = new CompletableFuture<>();
		client.execute(
			SimpleRequestProducer.create(mapRequest(request)),
			SimpleResponseConsumer.create(),
			null,
			HttpClientContext.create(),
			getCallback(response)
		);
		return response.thenApply(this::mapResponse).get();
	}

	@Override
	public HttpResponse<?> get(HttpRequest<?> request) throws Exception {
		return execute(request);
	}

	@Override
	public HttpResponse<?> post(HttpRequest<?> request) throws Exception {
		return execute(request);
	}

	@Override
	public HttpResponse<?> put(HttpRequest<?> request) throws Exception {
		return execute(request);
	}

	@Override
	public HttpResponse<?> delete(HttpRequest<?> request) throws Exception {
		return execute(request);
	}

	private SimpleHttpRequest mapRequest(HttpRequest<?> request) {
		SimpleHttpRequest httpRequest = new SimpleHttpRequest(request.getMethod().name(), request.getPath());
		httpRequest.setBody(request.getContentAsBytes(), ContentType.APPLICATION_JSON);
		return httpRequest;
	}

	private HttpResponse<?> mapResponse(SimpleHttpResponse response) {
		return new BasicHttpResponse(
			Arrays.stream(response.getHeaders()).collect(Collectors.toConcurrentMap(NameValuePair::getName, NameValuePair::getValue)),
			response.getCode(),
			response.getBodyText()
		);
	}

	private FutureCallback<SimpleHttpResponse> getCallback(CompletableFuture<SimpleHttpResponse> completableFuture) {
		return new FutureCallback<>() {
			@Override
			public void completed(SimpleHttpResponse result) {
				completableFuture.complete(result);
			}

			@Override
			public void failed(Exception ex) {
				completableFuture.completeExceptionally(ex);
			}

			@Override
			public void cancelled() {
				completableFuture.cancel(true);
			}
		};
	}
}
