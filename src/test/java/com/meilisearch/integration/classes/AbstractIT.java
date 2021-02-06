package com.meilisearch.integration.classes;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.ClientBuilder;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.api.index.Index;
import com.meilisearch.sdk.http.ApacheHttpClient;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.utils.Movie;

import java.io.*;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractIT {
	protected Client client;
	protected final Gson gson = new Gson();

	private final Map<String, TestData<?>> testData = new HashMap<>();

	public static final String MOVIES_INDEX = "movies.json";

	public AbstractIT() {
		try {
			loadResource(MOVIES_INDEX);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setUp() {
		if (client == null) {
			Map<String, Class<?>> modelMapping = Collections.singletonMap("movies", Movie.class);
			Config config = new Config("http://localhost:7700", "masterKey", modelMapping);
			ApacheHttpClient httpClient = new ApacheHttpClient(config);
			GsonJsonHandler handler = new GsonJsonHandler();
			client = ClientBuilder.withConfig(config).withHttpClient(httpClient).withJsonHandler(handler).build();
		}
	}


	public static void cleanup() {
		deleteAllIndexes();
	}

	public void loadResource(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();

		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file is not found!");
		}
		File file = new File(resource.getFile());
		String rawData = new BufferedReader(new InputStreamReader(new FileInputStream(file))).lines().collect(Collectors.joining());
		List<Movie> movies = gson.fromJson(rawData, TypeToken.getParameterized(List.class, Movie.class).getType());
		testData.put(fileName, new TestData<>(rawData, movies));
	}

	static public void deleteAllIndexes() {
		try {
			Map<String, Class<?>> modelMapping = Collections.singletonMap("movies", Movie.class);
			Config config = new Config("http://localhost:7700", "masterKey", modelMapping);
			ApacheHttpClient httpClient = new ApacheHttpClient(config);
			GsonJsonHandler handler = new GsonJsonHandler();
			Client ms = ClientBuilder.withConfig(config).withHttpClient(httpClient).withJsonHandler(handler).build();

			Index[] indexes = ms.index().getAllIndexes();
			for (Index index : indexes) {
				ms.index().deleteIndex(index.getUid());
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
