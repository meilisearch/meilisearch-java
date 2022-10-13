package com.meilisearch.sdk.http;

import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.exceptions.MeilisearchCommunicationException;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.exceptions.MeilisearchTimeoutException;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.http.response.HttpResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.*;

public class CustomOkHttpClient extends AbstractHttpClient {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final RequestBody EMPTY_REQUEST_BODY = RequestBody.create("".getBytes());
    private final OkHttpClient client;

    public CustomOkHttpClient(Config config, OkHttpClient client) {
        super(config);
        this.client = client;
    }

    public CustomOkHttpClient(Config config) {
        super(config);
        this.client = new OkHttpClient();
    }

    public HttpResponse execute(HttpRequest request) throws MeilisearchException {
        try {
            Request okRequest = buildRequest(request);
            Response response = client.newCall(okRequest).execute();

            return buildResponse(response);
        } catch (MalformedURLException e) {
            throw new MeilisearchException(e);
        } catch (SocketTimeoutException e) {
            throw new MeilisearchTimeoutException(e);
        } catch (IOException e) {
            throw new MeilisearchCommunicationException(e);
        }
    }

    private RequestBody getBodyFromRequest(HttpRequest request) {
        if (request.hasContent()) return RequestBody.create(request.getContentAsBytes(), JSON);
        return EMPTY_REQUEST_BODY;
    }

    private Request buildRequest(HttpRequest request) throws MalformedURLException {
        URL url = new URL(this.config.getHostUrl() + request.getPath());
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (this.config.getApiKey() != null)
            builder.addHeader("Authorization", this.config.getBearerApiKey());
        switch (request.getMethod()) {
            case GET:
                builder.get();
                break;
            case POST:
                builder.post(getBodyFromRequest(request));
                break;
            case PUT:
                builder.put(getBodyFromRequest(request));
                break;
            case DELETE:
                if (request.hasContent()) builder.delete(getBodyFromRequest(request));
                else builder.delete();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + request.getMethod());
        }

        return builder.build();
    }

    private HttpResponse buildResponse(Response response) throws IOException {
        String body = null;
        ResponseBody responseBody = response.body();
        if (responseBody != null) body = responseBody.string();

        return new HttpResponse(
                parseHeaders(response.headers().toMultimap()), response.code(), body);
    }

    private Map<String, String> parseHeaders(Map<String, List<String>> headers) {
        HashMap<String, String> headerMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            headerMap.put(entry.getKey(), String.join("; ", entry.getValue()));
        }
        return headerMap;
    }

    public HttpResponse get(HttpRequest request) throws MeilisearchException {
        return execute(request);
    }

    public HttpResponse post(HttpRequest request) throws MeilisearchException {
        return execute(request);
    }

    public HttpResponse put(HttpRequest request) throws MeilisearchException {
        return execute(request);
    }

    public HttpResponse delete(HttpRequest request) throws MeilisearchException {
        return execute(request);
    }
}
