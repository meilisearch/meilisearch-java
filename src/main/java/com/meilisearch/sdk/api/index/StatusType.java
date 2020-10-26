package com.meilisearch.sdk.api.index;

public class StatusType {
	private String name = "";
	private int number = 0;

	public String getName() {
		return name;
	}

	public StatusType setName(String name) {
		this.name = name;
		return this;
	}

	public int getNumber() {
		return number;
	}

	public StatusType setNumber(int number) {
		this.number = number;
		return this;
	}
}
