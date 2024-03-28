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
import java.util.Map;

/** HTTP client used for API calls to Meilisearch */
public class HttpClient {
    private final CustomOkHttpClient client;
    private final BasicRequest request;
    private final BasicResponse response;
    private final Map<String, String> headers;
    protected final JsonHandler jsonHandler;

    /**
     * Constructor for the HttpClient
     *
     * @param config Meilisearch configuration
     */
    public HttpClient(Config config) {
        this.client = new CustomOkHttpClient(config);
        this.jsonHandler = config.jsonHandler;
        this.headers = config.headers;
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
        this.headers = Collections.<String, String>emptyMap();
        this.jsonHandler = new GsonJsonHandler();
        this.response = new BasicResponse(jsonHandler);
    }

    /**
     * Gets the specified resource from the specified path with a given parameter
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
     * Gets the specified resource from the specified path with a given parameter
     *
     * @param api Path to document
     * @param param Parameter to be passed
     * @return document that was requested
     * @throws MeilisearchApiException if the response is an error
     */
    <T> T get(String api, String param, Class<T> targetClass, Class<?>... parameters)
            throws MeilisearchApiException {
        HttpRequest requestConfig = request.create(HttpMethod.GET, api + param, this.headers, null);
        HttpResponse<T> httpRequest = this.client.get(requestConfig);
        HttpResponse<T> httpResponse = response.create(httpRequest, targetClass, parameters);

        if (httpResponse.getStatusCode() >= 400) {
            throw new MeilisearchApiException(
                    jsonHandler.decode(httpRequest.getContent(), APIError.class));
        }
        return httpResponse.getContent();
    }

    /**
     * Adds the specified resource to the specified path
     *
     * @param api Path to server
     * @param body Query for search
     * @return results of the search
     * @throws MeilisearchApiException if the response is an error
     */
    <S, T> T post(String api, S body, Class<T> targetClass, Class<?>... parameters)
            throws MeilisearchApiException {
        HttpRequest requestConfig = request.create(HttpMethod.POST, api, this.headers, body);
        HttpResponse<T> httpRequest = this.client.post(requestConfig);
        HttpResponse<T> httpResponse = response.create(httpRequest, targetClass, parameters);

        if (httpResponse.getStatusCode() >= 400) {
            throw new MeilisearchApiException(
                    jsonHandler.decode(httpRequest.getContent(), APIError.class));
        }
        return httpResponse.getContent();
    }

    /**
     * Replaces the specified resource with new data to the specified path
     *
     * @param api Path to the requested resource
     * @param body Replacement data for the requested resource
     * @return updated resource
     * @throws MeilisearchApiException if the response is an error
     */
    <S, T> T put(String api, S body, Class<T> targetClass) throws MeilisearchApiException {
        HttpRequest requestConfig = request.create(HttpMethod.PUT, api, this.headers, body);
        HttpResponse<T> httpRequest = this.client.put(requestConfig);
        HttpResponse<T> httpResponse = response.create(httpRequest, targetClass);

        if (httpResponse.getStatusCode() >= 400) {
            throw new MeilisearchApiException(
                    jsonHandler.decode(httpRequest.getContent(), APIError.class));
        }
        return httpResponse.getContent();
    }

    /**
     * Patch the specified resource with new data to the specified path
     *
     * @param api Path to server
     * @param body Query for search
     * @return results of the search
     * @throws MeilisearchApiException if the response is an error
     */
    <S, T> T patch(String api, S body, Class<T> targetClass) throws MeilisearchApiException {
        HttpRequest requestConfig = request.create(HttpMethod.PATCH, api, this.headers, body);
        HttpResponse<T> httpRequest = this.client.patch(requestConfig);
        HttpResponse<T> httpResponse = response.create(httpRequest, targetClass);

        if (httpResponse.getStatusCode() >= 400) {
            throw new MeilisearchApiException(
                    jsonHandler.decode(httpResponse.getContent(), APIError.class));
        }
        return httpResponse.getContent();
    }

    /**
     * Deletes the specified resource to the specified path
     *
     * @param api Path to the requested resource
     * @return deleted resource
     * @throws MeilisearchApiException if the response is an error
     */
    <T> T delete(String api, Class<T> targetClass) throws MeilisearchApiException {
        HttpRequest requestConfig = request.create(HttpMethod.DELETE, api, this.headers, null);
        HttpResponse<T> httpRequest = this.client.delete(requestConfig);
        HttpResponse<T> httpResponse = response.create(httpRequest, targetClass);

        if (httpResponse.getStatusCode() >= 400) {
            throw new MeilisearchApiException(
                    jsonHandler.decode(httpRequest.getContent(), APIError.class));
        }
        return httpResponse.getContent();
    }
}
