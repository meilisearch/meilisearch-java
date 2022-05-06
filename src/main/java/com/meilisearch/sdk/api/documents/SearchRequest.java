package com.meilisearch.sdk.api.documents;

import java.util.Collections;
import java.util.List;
import lombok.Getter;

public class SearchRequest {
   @Getter private final String q;
   @Getter private final int offset;
   @Getter private final int limit;
   @Getter private final String[] filter;
   @Getter private final String[][] filterArray;
   @Getter private final List<String> attributesToRetrieve;
   @Getter private final List<String> attributesToCrop;
   @Getter private final int cropLength;
   @Getter private final List<String> attributesToHighlight;
   @Getter private final boolean matches;
   @Getter private final List<String> sort;

    public SearchRequest(String q) {
        this(q, 0);
    }

    public SearchRequest(String q, int offset) {
        this(q, offset, 20);
    }

    public SearchRequest(String q, int offset, int limit) {
        this(q, offset, limit, Collections.singletonList("*"));
    }

    public SearchRequest(String q, int offset, int limit, List<String> attributesToRetrieve) {
        this(q, offset, limit, attributesToRetrieve, null, 200, null, null, null, false, null);
    }

    public SearchRequest(
            String q,
            int offset,
            int limit,
            List<String> attributesToRetrieve,
            List<String> attributesToCrop,
            int cropLength,
            List<String> attributesToHighlight,
            String[] filter,
            boolean matches,
            List<String> sort) {
        this(q, offset, limit, attributesToRetrieve, null, 200, null, filter, null, false, null);
    }

    public SearchRequest(
            String q,
            int offset,
            int limit,
            List<String> attributesToRetrieve,
            List<String> attributesToCrop,
            int cropLength,
            List<String> attributesToHighlight,
            String[][] filterArray,
            boolean matches,
            List<String> sort) {
        this(
                q,
                offset,
                limit,
                attributesToRetrieve,
                null,
                200,
                null,
                null,
                filterArray,
                false,
                null);
    }

    public SearchRequest(
            String q,
            int offset,
            int limit,
            List<String> attributesToRetrieve,
            List<String> attributesToCrop,
            int cropLength,
            List<String> attributesToHighlight,
            String[] filter,
            String[][] filterArray,
            boolean matches,
            List<String> sort) {
        this.q = q;
        this.offset = offset;
        this.limit = limit;
        this.attributesToRetrieve = attributesToRetrieve;
        this.attributesToCrop = attributesToCrop;
        this.cropLength = cropLength;
        this.attributesToHighlight = attributesToHighlight;
        this.filter = filter;
        this.filterArray = filterArray;
        this.matches = matches;
        this.sort = sort;
    }
}
