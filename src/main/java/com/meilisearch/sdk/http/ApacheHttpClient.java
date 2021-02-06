package com.meilisearch.sdk.http;

import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.http.response.BasicHttpResponse;
import com.meilisearch.sdk.http.response.HttpResponse;
import org.apache.hc.client5.http.async.HttpAsyncClient;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestProducer;
import org.apache.hc.client5.http.async.methods.SimpleResponseConsumer;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;

import java.util.Arrays;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


public class ApacheHttpClient extends AbstractHttpClient {

	private final HttpAsyncClient client;

	public ApacheHttpClient(Config config) {
		super(config);
		final IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
			.setSoTimeout(Timeout.ofSeconds(5))
			.build();

		CloseableHttpAsyncClient build = HttpAsyncClients.custom()
			.setIOReactorConfig(ioReactorConfig)
			.build();
		build.start();
		this.client = build;
	}

	public ApacheHttpClient(Config config, HttpAsyncClient client) {
		super(config);
		this.client = client;
	}


	private HttpResponse<?> execute(HttpRequest<?> request) throws Exception {
		CompletableFuture<SimpleHttpResponse> response = new CompletableFuture<>();
		client.execute(
			SimpleRequestProducer.create(mapRequest(request)),
			SimpleResponseConsumer.create(),
			null,
			HttpClientContext.create(),
			getCallback(response)
		);
		try {
			return response.thenApply(this::mapResponse).get();
		} catch (CancellationException | ExecutionException e) {
			// todo: throw dedicated exception
			throw new Exception(e);
		}
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
		if (request.hasContent())
			httpRequest.setBody(request.getContentAsBytes(), ContentType.APPLICATION_JSON);
		httpRequest.addHeader("X-Meili-API-Key", this.config.getApiKey());
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
		return new FutureCallback<SimpleHttpResponse>() {
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
				completableFuture.cancel(false);
			}
		};
	}
}
