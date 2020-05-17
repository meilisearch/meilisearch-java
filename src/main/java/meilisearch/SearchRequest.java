package meilisearch;

class SearchRequest {
    String q;
    int offset;
    int limit;
    String attributesToRetrieve;
    String attributesToCrop;
    int cropLength;
    String attributesToHighlight;
    String filters;
    boolean matches;

    SearchRequest (String q) {
        this(q, 0);
    }

    SearchRequest (String q, int offset) {
        this(q, offset, 20);
    }

    SearchRequest (String q, int offset, int limit) {
        this(q, offset, limit, "*");
    }

    SearchRequest (String q, int offset, int limit, String attributesToRetrieve) {
        this(q, offset, limit, attributesToRetrieve, null, 200, null, null, false);
    }

    SearchRequest (String q,
                   int offset,
                   int limit,
                   String attributesToRetrieve,
                   String attributesToCrop,
                   int cropLength,
                   String attributesToHighlight,
                   String filters,
                   boolean matches) {
        this.q = q.replaceAll("\\s+?","%20");;
        this.offset = offset;
        this.limit = limit;
        this.attributesToRetrieve = attributesToRetrieve;
        this.attributesToCrop = attributesToCrop;
        this.cropLength = cropLength;
        this.attributesToHighlight = attributesToHighlight;
        this.filters = filters;
        this.matches = matches;
    }

    String getQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("?q=" + this.q);
        sb.append("&offset=" + this.offset);
        sb.append("&limit=" + this.limit);
        sb.append("&attributesToRetrieve=" + this.attributesToRetrieve);
        if (this.attributesToCrop != null) {
            sb.append("&attributesToCrop=" + this.attributesToCrop);
        }
        sb.append("&cropLength=" + this.cropLength);
        if (this.attributesToHighlight != null) {
            sb.append("&attributesToHighlight=" + this.attributesToHighlight);
        }
        if (this.filters != null) {
            sb.append("&filters=" + this.filters);
        }
        sb.append("&matches=" + this.matches);

        return sb.toString();
    }
}
