package com.meilisearch.sdk.utils;

import java.util.*;

public class Movie {

	private String id;
	private String title;
	private String poster;
	private String overview;
	private String release_date;
	private String language;
	private String[] genres;
	private Movie _formatted;
	private HashMap<String, List<Match>> _matchesInfo;

	public class Match {
		public int start;
		public int length;
	}

	public Movie() {
	}

	public Movie(String id, String title) {
		this.id = id;
		this.title = title;
	}

	public Movie(String id, String title, String poster, String overview, String release_date, String language, String... genres) {
		this.id = id;
		this.title = title;
		this.poster = poster;
		this.overview = overview;
		this.release_date = release_date;
		this.language = language;
		this.genres = genres;
	}

	public String getId() {
		return id;
	}

	public Movie setId(String id) {
		this.id = id;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Movie setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getPoster() {
		return poster;
	}

	public Movie setPoster(String poster) {
		this.poster = poster;
		return this;
	}

	public String getOverview() {
		return overview;
	}

	public Movie setOverview(String overview) {
		this.overview = overview;
		return this;
	}

	public String getRelease_date() {
		return release_date;
	}

	public Movie setRelease_date(String release_date) {
		this.release_date = release_date;
		return this;
	}

	public String getLanguage() {
		return language;
	}

	public Movie setLanguage(String language) {
		this.language = language;
		return this;
	}

	public String[] getGenres() {
		return genres;
	}

	public Movie setGenres(String[] genres) {
		this.genres = genres;
		return this;
	}

	public Movie getFormatted() {
		return _formatted;
	}

	public Movie setFormatted(Movie _formatted) {
		this._formatted = _formatted;
		return this;
	}

	public HashMap<String, List<Match>> getMatchesInfo() {
		return _matchesInfo;
	}

	public Movie setMatchesInfo(HashMap<String, List<Match>> _matchesInfo) {
		this._matchesInfo = _matchesInfo;
		return this;
	}
}
