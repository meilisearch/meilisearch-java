package com.meilisearch.sdk;

public class Version {
    static final String VERSION = "0.10.0";

    public static String getQualifiedVersion() {
        return "Meilisearch Java (v" + VERSION + ")";
    }
}
