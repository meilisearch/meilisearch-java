package com.meilisearch.sdk;

public class Version {
    static final String VERSION = "v0.20.1";

    public static String getQualifiedVersion() {
        return "Meilisearch Java (v" + VERSION + ")";
    }
}
