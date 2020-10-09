package com.meilisearch.sdk;

import java.util.List;

public class TestData<T> {
	private final String raw;
	private final List<T> data;

	public TestData(String raw, List<T> data) {
		this.raw = raw;
		this.data = data;
	}

	public String getRaw() {
		return raw;
	}

	public List<T> getData() {
		return data;
	}
}
