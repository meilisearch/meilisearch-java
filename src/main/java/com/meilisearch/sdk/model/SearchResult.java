package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;

@ToString
public class SearchResult implements Serializable {

	@Getter
	ArrayList<Object> hits;

	@Getter
	int offset;

	@Getter
	int limit;

	@Getter
	int nbHits;

	@Getter
	boolean exhaustiveNbHits;

	@Getter
	int processingTimeMs;

	@Getter
	String query;
}
