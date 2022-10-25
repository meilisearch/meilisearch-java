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
    <S, T> T post(String api, S body, Class<T> targetClass) throws MeilisearchException {
        HttpRequest requestConfig =
                request.create(HttpMethod.POST, api, Collections.emptyMap(), body);
        HttpResponse<T> httpRequest = this.client.post(requestConfig);
        HttpResponse<T> httpResponse = response.create(httpRequest, targetClass);

        if (httpResponse.getStatusCode() >= 400) {
            throw new MeilisearchApiException(
                    jsonHandler.decode(httpResponse.getContent(), APIError.class));
        }
        return httpResponse.getContent();
    }

    /**
     * Replaces the requested resource with new data
     *
     * @param api Path to the requested resource
     * @param body Replacement data for the requested resource
     * @return updated resource
     * @throws MeilisearchException if the response is an error
     */
    <S, T> T put(String api, S body, Class<T> targetClass) throws MeilisearchException {
        HttpRequest requestConfig =
                request.create(HttpMethod.PUT, api, Collections.emptyMap(), body);
        HttpResponse<T> httpRequest = this.client.put(requestConfig);
        HttpResponse<T> httpResponse = response.create(httpRequest, targetClass);

        if (httpResponse.getStatusCode() >= 400) {
            throw new MeilisearchApiException(
                    jsonHandler.decode(httpResponse.getContent(), APIError.class));
        }
        return httpResponse.getContent();
    }

    /**
     * Deletes the specified resource
     *
     * @param api Path to the requested resource
     * @return deleted resource
     * @throws MeilisearchException if the response is an error
     */
    <T> T delete(String api, Class<T> targetClass) throws MeilisearchException {
        HttpRequest requestConfig =
                request.create(HttpMethod.DELETE, api, Collections.emptyMap(), null);
        HttpResponse<T> httpRequest = this.client.delete(requestConfig);
        HttpResponse<T> httpResponse = response.create(httpRequest, targetClass);

        if (httpResponse.getStatusCode() >= 400) {
            throw new MeilisearchApiException(
                    jsonHandler.decode(httpResponse.getContent(), APIError.class));
        }
        return httpResponse.getContent();
    }
}
