package com.meilisearch.sdk.api.index;

import com.meilisearch.sdk.Index;

import java.util.HashMap;
import java.util.Map;

/**
 * Data Structure for the Settings in an {@link Index}
 * <p>
 * Refer https://docs.meilisearch.com/references/settings.html
 */
public class Settings {
	private HashMap<String, String[]> synonyms = new HashMap<>();
	private String[] stopWords;
	private String[] rankingRules;
	private String[] attributesForFaceting;
	private String distinctAttribute;
	private String[] searchableAttributes;
	private String[] displayedAttributes;

	/**
	 * Empty SettingsRequest constructor
	 */
	public Settings() {
	}

	public Map<String, String[]> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(Map<String, String[]> synonyms) {
		this.synonyms = new HashMap<>(synonyms);
	}

	public String[] getStopWords() {
		return stopWords;
	}

	public void setStopWords(String[] stopWords) {
		this.stopWords = stopWords;
	}

	public String[] getRankingRules() {
		return rankingRules;
	}

	public void setRankingRules(String[] rankingRules) {
		this.rankingRules = rankingRules;
	}

	public String[] getAttributesForFaceting() {
		return attributesForFaceting;
	}

	public void setAttributesForFaceting(String[] attributesForFaceting) {
		this.attributesForFaceting = attributesForFaceting;
	}

	public String getDistinctAttribute() {
		return distinctAttribute;
	}

	public void setDistinctAttribute(String distinctAttribute) {
		this.distinctAttribute = distinctAttribute;
	}

	public String[] getSearchableAttributes() {
		return searchableAttributes;
	}

	public void setSearchableAttributes(String[] searchableAttributes) {
		this.searchableAttributes = searchableAttributes;
	}

	public String[] getDisplayedAttributes() {
		return displayedAttributes;
	}

	public void setDisplayedAttributes(String[] displayedAttributes) {
		this.displayedAttributes = displayedAttributes;
	}
}
