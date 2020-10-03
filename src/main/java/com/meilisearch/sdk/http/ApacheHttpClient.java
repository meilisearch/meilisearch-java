package com.meilisearch.sdk.http;

import com.meilisearch.sdk.http.request.BasicHttpRequest;
import com.meilisearch.sdk.http.response.BasicHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ApacheHttpClient implements HttpClient<BasicHttpRequest, BasicHttpResponse> {

	private final CloseableHttpAsyncClient client;

	public ApacheHttpClient() {
		final IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
			.setSoTimeout(Timeout.ofSeconds(5))
			.build();

		client = HttpAsyncClients.custom()
			.setIOReactorConfig(ioReactorConfig)
			.build();
	}

	public ApacheHttpClient(CloseableHttpAsyncClient client) {
		this.client = client;
	}


	private BasicHttpResponse execute(BasicHttpRequest request) throws ExecutionException, InterruptedException {
		CompletableFuture<SimpleHttpResponse> response = new CompletableFuture<>();
		client.execute(mapRequest(request), getCallback(response));
		return response.thenApply(this::mapResponse).get();
	}

	@Override
	public BasicHttpResponse get(BasicHttpRequest request) throws Exception {
		return execute(request);
	}

	@Override
	public BasicHttpResponse post(BasicHttpRequest request) throws Exception {
		return execute(request);
	}

	@Override
	public BasicHttpResponse put(BasicHttpRequest request) throws Exception {
		return execute(request);
	}

	@Override
	public BasicHttpResponse delete(BasicHttpRequest request) throws Exception {
		return execute(request);
	}

	private SimpleHttpRequest mapRequest(BasicHttpRequest request) {
		SimpleHttpRequest httpRequest = new SimpleHttpRequest(request.getMethod().name(), request.getPath());
		httpRequest.setBody(request.getContent(), ContentType.APPLICATION_JSON);
		return httpRequest;
	}

	private BasicHttpResponse mapResponse(SimpleHttpResponse response) {
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
