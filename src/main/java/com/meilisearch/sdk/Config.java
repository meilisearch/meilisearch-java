package com.meilisearch.sdk;

import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.json.JsonHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;

/** Meilisearch configuration */
@Getter
@Setter
public class Config {
    protected final String hostUrl;
    protected final String apiKey;
    protected final HttpClient httpClient;
    protected final Map<String, String> headers;
    protected JsonHandler jsonHandler;
    protected final OkHttpClient okHttpClient;

    /**
     * Creates a configuration without an API key
     *
     * @param hostUrl URL of the Meilisearch instance
     */
    public Config(String hostUrl) {
        this(hostUrl, "");
    }

    /**
     * Creates a configuration with an API key and no customized headers.
     *
     * @param hostUrl URL of the Meilisearch instance
     * @param apiKey API key to pass to the header of requests sent to Meilisearch
     */
    public Config(String hostUrl, String apiKey) {
        this(hostUrl, apiKey, defaultJsonHandler(), new String[0]);
    }

    /**
     * Creates a configuration with an API key and a provided OkHttpClient, and no customized headers.
     *
     * @param hostUrl URL of the Meilisearch instance
     * @param apiKey API key to pass to the header of requests sent to Meilisearch
     * @param okHttpClient The OkHttpClient instance to used by the SDK for making requests
     */
    public Config(String hostUrl, String apiKey, OkHttpClient okHttpClient) {
        this(hostUrl, apiKey, defaultJsonHandler(), okHttpClient, new String[0]);
    }

    /**
     * Creates a configuration with an API key and no customized headers.
     *
     * @param hostUrl URL of the Meilisearch instance
     * @param apiKey API key to pass to the header of requests sent to Meilisearch
     * @param jsonHandler JsonHandler to parse or write JSON
     */
    public Config(String hostUrl, String apiKey, JsonHandler jsonHandler) {
        this(hostUrl, apiKey, jsonHandler, new String[0]);
    }

    /**
     * Creates a configuration with an API key
     *
     * @param hostUrl URL of the Meilisearch instance
     * @param apiKey API key to pass to the header of requests sent to Meilisearch
     * @param clientAgents List of customized agents to be passed to User-Agent header.
     */
    public Config(String hostUrl, String apiKey, String[] clientAgents) {
        this(hostUrl, apiKey, defaultJsonHandler(), clientAgents);
    }

    /**
     * Creates a configuration with an API key
     *
     * @param hostUrl URL of the Meilisearch instance
     * @param apiKey API key to pass to the header of requests sent to Meilisearch
     * @param jsonHandler JsonHandler to parse or write JSON
     * @param clientAgents List of customized agents to be passed to User-Agent header.
     */
    public Config(String hostUrl, String apiKey, JsonHandler jsonHandler, String[] clientAgents) {
        this(hostUrl, apiKey, jsonHandler, defaultOkHttpClient(), clientAgents);
    }

    /**
     * Creates a configuration with an API key, client agent header value, a JSON handler and an OkHttpClient
     *
     * @param hostUrl URL of the Meilisearch instance
     * @param apiKey API key to pass to the header of requests sent to Meilisearch
     * @param jsonHandler JsonHandler to parse or write JSON
     * @param okHttpClient The OkHttpClient instance to use for the SDK's HTTP requests
     * @param clientAgents List of customized agents to be passed to User-Agent header.
     */
    public Config(String hostUrl, String apiKey, JsonHandler jsonHandler, OkHttpClient okHttpClient, String[] clientAgents) {
        this.hostUrl = hostUrl;
        this.apiKey = apiKey;
        this.headers = configHeaders(clientAgents);
        this.jsonHandler = jsonHandler;
        this.okHttpClient = okHttpClient;
        this.httpClient = new HttpClient(this);
    }

    /**
     * Method for returning the concatenated Bearer header with apiKey
     *
     * @return Bearer API key String
     */
    public String getBearerApiKey() {
        return "Bearer " + apiKey;
    }

    private Map<String, String> configHeaders(String[] clientAgents) {
        List<String> list = new ArrayList<String>(Arrays.asList(clientAgents));
        list.add(0, Version.getQualifiedVersion());

        Map<String, String> data = new HashMap<>();
        data.put("User-Agent", String.join(";", list));

        return data;
    }

    private static OkHttpClient defaultOkHttpClient() {
        return new OkHttpClient();
    }

    private static JsonHandler defaultJsonHandler() {
        return new GsonJsonHandler();
    }
}
