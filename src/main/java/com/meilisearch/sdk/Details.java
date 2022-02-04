package com.meilisearch.sdk;

public class Details {

	public Details() {}

	protected int receivedDocuments;
	protected int indexedDocuments;

	public int getReceivedDocuments() { return receivedDocuments; }

	public void setReceivedDocuments(int receivedDocuments) { this.receivedDocuments = receivedDocuments; }

	public int getIndexedDocuments() { return indexedDocuments; }

	public void setIndexedDocuments(int indexedDocuments) { this.indexedDocuments = indexedDocuments; }
}
