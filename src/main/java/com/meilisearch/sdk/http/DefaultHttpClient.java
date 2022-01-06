package com.meilisearch.sdk.http;

import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.http.response.BasicHttpResponse;
import com.meilisearch.sdk.http.response.HttpResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.stream.Collectors;

public class DefaultHttpClient extends AbstractHttpClient {

    public DefaultHttpClient(Config config) {
        super(config);
    }

    /**
     * Create and get a validated HTTP connection to url with method and API key
     *
     * @param url URL to connect to
     * @param method HTTP method to use for the connection
     * @param apiKey API Key to use for the connection
     * @return Validated connection (otherwise, will throw a {@link IOException})
     * @throws IOException if unable to establish connection
     */
    private HttpURLConnection getConnection(
            final URL url, final String method, String dataType, final String apiKey)
            throws IOException {
        if (url == null || "".equals(method))
            throw new IOException("Unable to open an HttpURLConnection with no URL or method");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", dataType);

        // Use API key header only if one is provided
        if (!"".equals(apiKey)) {
            connection.setRequestProperty("X-Meili-API-Key", apiKey);
        }

        return connection;
    }

    private HttpResponse<?> execute(HttpRequest<?> request) throws IOException {
        URL url = new URL(this.config.getHostUrl() + request.getPath());

        HttpURLConnection connection;
        if (request.getHeaders().get("Content-Type") == null) {
            connection =
                    this.getConnection(
                            url,
                            request.getMethod().name(),
                            "application/json",
                            this.config.getApiKey());
        } else {
            connection =
                    this.getConnection(
                            url,
                            request.getMethod().name(),
                            request.getHeaders().get("Content-Type"),
                            this.config.getApiKey());
        }
        if (request.hasContent()) {
            connection.setDoOutput(true);
            connection.getOutputStream().write(request.getContentAsBytes());
        }

        if (connection.getResponseCode() >= 400) {
            return new BasicHttpResponse(
                    Collections.emptyMap(),
                    connection.getResponseCode(),
                    new BufferedReader(new InputStreamReader(connection.getErrorStream()))
                            .lines()
                            .collect(Collectors.joining("\n")));
        }

        return new BasicHttpResponse(
                Collections.emptyMap(),
                connection.getResponseCode(),
                new BufferedReader(new InputStreamReader(connection.getInputStream()))
                        .lines()
                        .collect(Collectors.joining("\n")));
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
}
