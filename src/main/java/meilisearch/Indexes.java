package meilisearch;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Indexes implements Serializable {
    String name;
    String uid;
    String createdAt;
    String updatedAt;
    Config config;
    Documents documents;

    void setConfig(Config config) {
        this.config = config;
        this.documents = new Documents(config);
    }

    public String getName() {
        return this.name;
    }

    public String getUid() {
        return this.uid;
    }

    public ZonedDateTime getCreatedAt() {
        return ZonedDateTime.parse(this.createdAt);
    }

    public ZonedDateTime getUpdatedAt() {
        return ZonedDateTime.parse(this.updatedAt);
    }

    public String getDocument(String identifier) throws Exception {
        return this.documents.getDocument(this.uid, identifier);
    }

    public String getDocuments() throws Exception {
        return this.documents.getDocuments(this.uid);
    }

    public String getDocuments(int limits) throws Exception {
        return this.documents.getDocuments(this.uid, limits);
    }

    public String addDocument(String document) throws Exception {
        return this.documents.addDocument(this.uid, document);
    }

    public String deleteDocument(String identifier) throws Exception {
        return this.documents.deleteDocument(this.uid, identifier);
    }

    public String deleteDocuments() throws Exception {
        return this.documents.deleteDocuments(this.uid);
    }

    public String search(String q) throws Exception {
        return this.documents.search(this.uid, q);
    }

    @Override
    public String toString() {
        // TODO: update format
        return "Indexes:" + name + " / uid: " + uid;
    }
}
