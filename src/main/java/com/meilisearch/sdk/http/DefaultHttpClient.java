package com.meilisearch.sdk.http;

import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.http.request.BasicHttpRequest;
import com.meilisearch.sdk.http.response.BasicHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultHttpClient implements HttpClient<BasicHttpRequest, BasicHttpResponse> {

	private final Config config;

	public DefaultHttpClient(Config config) {
		this.config = config;
	}

	/**
	 * Create and get a validated HTTP connection to url with method and API key
	 *
	 * @param url    URL to connect to
	 * @param method HTTP method to use for the connection
	 * @param apiKey API Key to use for the connection
	 * @return Validated connection (otherwise, will throw a {@link IOException})
	 * @throws IOException If unable to establish connection
	 */
	private HttpURLConnection getConnection(final URL url, final String method, final String apiKey) throws IOException {
		if (url == null || "".equals(method)) throw new IOException("Unable to open an HttpURLConnection with no URL or method");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(method);
		connection.setRequestProperty("Content-Type", "application/json");

		// Use API key header only if one is provided
		if (!"".equals(apiKey)) {
			connection.setRequestProperty("X-Meili-API-Key", apiKey);
		}

		// Ensure connection is set
		Optional<HttpURLConnection> connectionOptional = Optional.of(connection);
		return connectionOptional.orElseThrow(IOException::new);
	}

	private BasicHttpResponse execute(BasicHttpRequest request) throws IOException {
		URL url = new URL(this.config.getHostUrl() + request.getPath());
		HttpURLConnection connection = this.getConnection(url, request.getMethod().name(), this.config.getApiKey());

		return new BasicHttpResponse(
			Collections.emptyMap(),
			connection.getResponseCode(),
			new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining("\n"))
		);
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
}
