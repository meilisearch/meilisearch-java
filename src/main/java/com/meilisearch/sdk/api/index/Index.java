package com.meilisearch.sdk.api.index;

import com.meilisearch.sdk.api.MeiliApiError;

import java.io.Serializable;

/**
 * Meilisearch index
 */
public class Index extends MeiliApiError implements Serializable {
	private String uid;
	private String primaryKey;
	private String createdAt;
	private String updatedAt;

	public Index() {
		this(null, null, null, null);
	}

	public Index(String uid) {
		this(uid, null, null, null);
	}

	public Index(String uid, String primaryKey) {
		this(uid, primaryKey, null, null);
	}

	public Index(String uid, String primaryKey, String createdAt, String updatedAt) {
		this.uid = uid;
		this.primaryKey = primaryKey;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public String getUid() {
		return uid;
	}

	public Index setUid(String uid) {
		this.uid = uid;
		return this;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public Index setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
		return this;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public Index setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public Index setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
		return this;
	}
}
