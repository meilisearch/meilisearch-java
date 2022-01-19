package com.meilisearch.sdk.http;

import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.http.response.BasicHttpResponse;
import com.meilisearch.sdk.http.response.HttpResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
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
    private HttpURLConnection getConnection(final URL url, final String method, final String apiKey)
            throws IOException {
        if (url == null || "".equals(method))
            throw new IOException("Unable to open an HttpURLConnection with no URL or method");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // This condition is link to the workaround for the Won't Fix bug in OpenJDK:
        // https://bugs.openjdk.java.net/browse/JDK-7016595
        // See allowMethods() method and issue
        // https://github.com/meilisearch/meilisearch-java/issues/318
        if (method == "PATCH") {
            allowMethods("PATCH");
        }
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");

        // Use API key header only if one is provided
        if (!"".equals(apiKey)) {
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        }

        return connection;
    }

    private HttpResponse<?> execute(HttpRequest<?> request) throws IOException {
        URL url = new URL(this.config.getHostUrl() + request.getPath());
        HttpURLConnection connection =
                this.getConnection(url, request.getMethod().name(), this.config.getApiKey());

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
    public HttpResponse<?> patch(HttpRequest<?> request) throws Exception {
        return execute(request);
    }

    @Override
    public HttpResponse<?> delete(HttpRequest<?> request) throws Exception {
        return execute(request);
    }

    // This function is a workaround for the Won't Fix bug in OpenJDK:
    // https://bugs.openjdk.java.net/browse/JDK-7016595
    // A better solution should be found if possible see
    // https://github.com/meilisearch/meilisearch-java/issues/318
    private static void allowMethods(String... methods) {
        try {
            Field methodsField = HttpURLConnection.class.getDeclaredField("methods");

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

            methodsField.setAccessible(true);

            String[] oldMethods = (String[]) methodsField.get(null);
            Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
            methodsSet.addAll(Arrays.asList(methods));
            String[] newMethods = methodsSet.toArray(new String[0]);

            methodsField.set(null /*static field*/, newMethods);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
