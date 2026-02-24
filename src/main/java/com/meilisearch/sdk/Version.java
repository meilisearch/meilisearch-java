package com.meilisearch.sdk;

public class Version {
    static final String VERSION = "0.20.0";

    public static String getQualifiedVersion() {
        return "Meilisearch Java (v" + VERSION + ")";
    }
}
