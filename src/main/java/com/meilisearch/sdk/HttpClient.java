package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.APIError;
import com.meilisearch.sdk.exceptions.MeilisearchApiException;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.http.CustomOkHttpClient;
import com.meilisearch.sdk.http.request.BasicRequest;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.http.response.BasicResponse;
import com.meilisearch.sdk.http.response.HttpResponse;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.json.JsonHandler;
import java.util.Collections;

/** The HTTP requests for the different functions to be done through Meilisearch */
public class HttpClient {
    private final CustomOkHttpClient client;
    private final BasicRequest request;
    private final BasicResponse response;
    protected final JsonHandler jsonHandler;

    /**
     * Constructor for the HttpClient
     *
     * @param config Meilisearch configuration
     */
    public HttpClient(Config config) {
        this.client = new CustomOkHttpClient(config);
        this.jsonHandler = config.jsonHandler;
        this.request = new BasicRequest(jsonHandler);
        this.response = new BasicResponse(jsonHandler);
    }

    /**
     * Constructor for the HttpClient
     *
     * @param client HttpClient for making calls to server
     * @param request BasicRequest for generating calls to server
     */
    public HttpClient(CustomOkHttpClient client, BasicRequest request) {
        this.client = client;
        this.request = request;
        this.jsonHandler = new GsonJsonHandler();
        this.response = new BasicResponse(jsonHandler);
    }

    /**
     * Gets a document at the specified path
     *
     * @param api Path to document
     * @return document that was requested
     * @throws MeilisearchException if the response is an error
     */
    <T> T get(String api, Class<T> targetClass, Class<?>... parameters)
            throws MeilisearchException {
        return this.get(api, "", targetClass, parameters);
    }

    /**
     * Gets a document at the specified path with a given parameter
     *
     * @param api Path to document
     * @param param Parameter to be passed
     * @return document that was requested
     * @throws MeilisearchException if the response is an error
     */
    <T> T get(String api, String param, Class<T> targetClass, Class<?>... parameters)
            throws MeilisearchException {
        HttpRequest requestConfig =
                request.create(HttpMethod.GET, api + param, Collections.emptyMap(), null);
        HttpResponse<T> httpRequest = this.client.get(requestConfig);
        HttpResponse<T> httpResponse = response.create(httpRequest, targetClass, parameters);

        if (httpResponse.getStatusCode() >= 400) {
            throw new MeilisearchApiException(
                    jsonHandler.decode(httpResponse.getContent(), APIError.class));
        }
        return httpResponse.getContent();
    }

    /**
     * Adds a document to the specified path
     *
     * @param api Path to server
     * @param body Query for search
     * @return results of the search
     * @throws MeilisearchException if the response is an error
     */
    <T> String post(String api, T body) throws MeilisearchException {
        HttpResponse httpResponse =
                this.client.post(
                        request.create(HttpMethod.POST, api, Collections.emptyMap(), body));
        if (httpResponse.getStatusCode() >= 400) {
            throw new MeilisearchApiException(
                    jsonHandler.decode(httpResponse.getContent(), APIError.class));
        }
        return new String(httpResponse.getContentAsBytes());
    }

    /**
     * Replaces the requested resource with new data
     *
     * @param api Path to the requested resource
     * @param body Replacement data for the requested resource
     * @return updated resource
     * @throws MeilisearchException if the response is an error
     */
    <T> String put(String api, T body) throws MeilisearchException {
        HttpResponse httpResponse =
                this.client.put(request.create(HttpMethod.PUT, api, Collections.emptyMap(), body));
        if (httpResponse.getStatusCode() >= 400) {
            throw new MeilisearchApiException(
                    jsonHandler.decode(httpResponse.getContent(), APIError.class));
        }
        return new String(httpResponse.getContentAsBytes());
    }

    /**
     * Deletes the specified resource
     *
     * @param api Path to the requested resource
     * @return deleted resource
     * @throws MeilisearchException if the response is an error
     */
    String delete(String api) throws MeilisearchException {
        HttpResponse httpResponse =
                this.client.put(
                        request.create(HttpMethod.DELETE, api, Collections.emptyMap(), null));
        if (httpResponse.getStatusCode() >= 400) {
            throw new MeilisearchApiException(
                    jsonHandler.decode(httpResponse.getContent(), APIError.class));
        }
        return new String(httpResponse.getContentAsBytes());
    }
}
