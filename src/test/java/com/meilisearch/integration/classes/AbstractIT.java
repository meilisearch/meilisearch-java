package com.meilisearch.integration.classes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.json.JacksonJsonHandler;
import com.meilisearch.sdk.model.Key;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.TaskInfo;
import com.meilisearch.sdk.utils.Movie;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractIT {
    protected Client client;
    protected Client clientJackson;
    protected final Gson gson = new Gson();

    private final Map<String, TestData<?>> testData = new HashMap<>();

    public static final String MOVIES_INDEX = "movies.json";
    public static final String NESTED_MOVIES = "nested_movies.json";

    public AbstractIT() {
        try {
            loadResource(MOVIES_INDEX);
            loadResource(NESTED_MOVIES);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUp() {
        if (client == null) client = new Client(new Config(getMeilisearchHost(), "masterKey"));
    }

    public void setUpJacksonClient() {
        Config config = new Config(getMeilisearchHost(), "masterKey", new JacksonJsonHandler());
        if (clientJackson == null) clientJackson = new Client(config);
    }

    public static void cleanup() {
        deleteAllIndexes();
    }

    public static String getMeilisearchHost() {
        String meilisearchHost = System.getenv("MEILISEARCH_HOST");
        if (meilisearchHost != null) {
            return meilisearchHost;
        }
        return "http://localhost:7700";
    }

    public Index createEmptyIndex(String indexUid) throws Exception {
        TaskInfo task = client.createIndex(indexUid);
        client.waitForTask(task.getTaskUid());
        return client.getIndex(indexUid);
    }

    public Index createEmptyIndex(String indexUid, String primaryKey) throws Exception {
        TaskInfo task = client.createIndex(indexUid, primaryKey);
        client.waitForTask(task.getTaskUid());
        return client.getIndex(indexUid);
    }

    public void loadResource(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        }
        File file = new File(resource.getFile());
        String rawData =
                new BufferedReader(new InputStreamReader(new FileInputStream(file)))
                        .lines()
                        .collect(Collectors.joining());
        List<Movie> movies =
                gson.fromJson(
                        rawData, TypeToken.getParameterized(List.class, Movie.class).getType());
        testData.put(fileName, new TestData<>(rawData, movies));
    }

    public Key getPrivateKey() throws Exception {
        Results<Key> result = client.getKeys();
        Key[] keys = result.getResults();
        for (Key key : keys) {
            if ((key.getName() == null) || (key.getName().contains("Default Admin API"))) {
                return key;
            }
        }
        return null;
    }

    public static void deleteAllIndexes() {
        try {
            Client ms = new Client(new Config(getMeilisearchHost(), "masterKey"));
            Results<Index> indexes = ms.getIndexes();
            for (Index index : indexes.getResults()) {
                TaskInfo task = ms.deleteIndex(index.getUid());
                ms.waitForTask(task.getTaskUid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllKeys() {
        try {
            Client ms = new Client(new Config(getMeilisearchHost(), "masterKey"));
            Results<Key> result = ms.getKeys();
            Key[] keys = result.getResults();
            for (Key key : keys) {
                if ((key.getDescription() == null) || (key.getDescription().contains("test"))) {
                    ms.deleteKey(key.getKey());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> TestData<T> getTestData(String index, Class<T> tClass) {
        return (TestData<T>) testData.get(index);
    }
}
