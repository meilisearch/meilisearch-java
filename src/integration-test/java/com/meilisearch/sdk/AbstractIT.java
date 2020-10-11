package com.meilisearch.sdk;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meilisearch.sdk.utils.Movie;

import java.io.*;
import java.net.URL;
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
		if (client == null)
			client = new Client(new Config("http://localhost:7700", "masterKey"));
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
			Client ms = new Client(new Config("http://localhost:7700", "masterKey"));
			Index[] indexes = ms.getIndexList();
			for (Index index : indexes) {
				ms.deleteIndex(index.uid);
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
