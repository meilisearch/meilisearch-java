package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Result of `search` API
 */
@ToString
public class SearchResult implements Serializable {

	@Getter
	ArrayList<HashMap<String, Object>> hits;

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
