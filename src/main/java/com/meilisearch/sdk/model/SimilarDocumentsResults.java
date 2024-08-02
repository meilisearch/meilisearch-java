package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Meilisearch similar documents results data structure
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/similar#response-200-ok">API
 *     specification</a>
 */
@Getter
@ToString
public class SimilarDocumentsResults {
    ArrayList<HashMap<String, Object>> hits;
    String id;
    int processingTimeMs;
    int offset;
    int limit;
    int estimatedTotalHits;
}

