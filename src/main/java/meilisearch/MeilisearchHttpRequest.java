package meilisearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

class MeilisearchHttpRequest {
	private final MeilisearchConfig config;

	protected MeilisearchHttpRequest(MeilisearchConfig config) {
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


	private String parseConnectionResponse(HttpURLConnection connection) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();
		String responseLine;

		while ((responseLine = br.readLine()) != null) {
			sb.append(responseLine);
		}

		br.close();

		return sb.toString();
	}


	String get(String api) throws Exception {
		return this.get(api, "");
	}


	String get(String api, String param) throws Exception {
		StringBuilder urlBuilder = new StringBuilder(config.hostUrl + api);
		if (!param.equals("")) {
			urlBuilder.append(param);
		}

		URL url = new URL(urlBuilder.toString());
		HttpURLConnection connection = this.getConnection(url, "GET", this.config.apiKey);

		return this.parseConnectionResponse(connection);
	}


	String post(String api, String params) throws IOException {
		URL url = new URL(config.hostUrl + api);

		HttpURLConnection connection = this.getConnection(url, "POST", config.apiKey);
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length", String.valueOf(params.length()));
		connection.getOutputStream().write(params.getBytes(StandardCharsets.UTF_8));
		connection.connect();

		return this.parseConnectionResponse(connection);
	}


	String put(String api, String params) throws Exception {
		URL url = new URL(config.hostUrl + api);

		HttpURLConnection connection = this.getConnection(url, "PUT", config.apiKey);
		connection.setDoOutput(true);
		connection.getOutputStream().write(params.getBytes());
		connection.connect();

		return this.parseConnectionResponse(connection);
	}


	String delete(String api) throws Exception {
		URL url = new URL(config.hostUrl + api);

		HttpURLConnection connection = this.getConnection(url, "DELETE", config.apiKey);
		connection.connect();
		return this.parseConnectionResponse(connection);
	}
}
