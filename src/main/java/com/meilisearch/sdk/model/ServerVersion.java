package com.meilisearch.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
public class ServerVersion {
    private final String commitSha;
    private final String commitDate;
    private final String pkgVersion;

    public String toString() {
        return String.format("v%s - %s (sha %s)", pkgVersion, commitDate, commitSha);
    }
}
