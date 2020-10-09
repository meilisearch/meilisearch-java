package com.meilisearch.sdk;

public class Movie {
	private long id;
	private String title;
	private String poster;
	private String overview;
	private String release_date;
	private String language;
	private String[] genres;

	public Movie() {
	}

	public Movie(long id, String title) {
		this.id = id;
		this.title = title;
	}

	public long getId() {
		return id;
	}

	public Movie setId(long id) {
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
}
