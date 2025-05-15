package com.meilisearch.sdk;

import lombok.*;
import lombok.experimental.Accessors;
import org.json.JSONObject;

@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@Accessors(chain = true)
public class SimilarDocumentRequest {
    private String id;
    private String embedder;
    private String[] attributesToRetrieve;
    private Integer offset;
    private Integer limit;
    private String filter;
    private Boolean showRankingScore;
    private Boolean showRankingScoreDetails;
    private Double rankingScoreThreshold;
    private Boolean retrieveVectors;

    /**
     * Constructor for SimilarDocumentsRequest for building search request for similar documents
     */
    public SimilarDocumentRequest() {}

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("embedder", this.embedder);
        jsonObject.putOpt("attributesToRetrieve", this.attributesToRetrieve);
        jsonObject.putOpt("offset", this.offset);
        jsonObject.putOpt("limit", this.limit);
        jsonObject.putOpt("filter", this.filter);
        jsonObject.putOpt("showRankingScore", this.showRankingScore);
        jsonObject.putOpt("showRankingScoreDetails", this.showRankingScoreDetails);
        jsonObject.putOpt("rankingScoreThreshold", this.rankingScoreThreshold);
        jsonObject.putOpt("retrieveVectors", this.retrieveVectors);

        return jsonObject.toString();
    }
}
