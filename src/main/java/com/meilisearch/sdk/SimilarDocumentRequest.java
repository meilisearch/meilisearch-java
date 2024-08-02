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
     * with the default values:
     * id null, embedder "default", attributesToRetrieve ["*"], offset 0, limit 20,
     * filter null, showRankingScore false, showRankingScoreDetails false, rankingScoreThreshold null,
     * retrieveVectors false
     *
     */
    public SimilarDocumentRequest() {}

    @Override
    public String toString() {
        JSONObject jsonObject =
            new JSONObject()
                .put("id", this.id)
                .put("embedder", this.embedder)
                .put("attributesToRetrieve", this.attributesToRetrieve)
                .put("offset", this.offset)
                .put("limit", this.limit)
                .put("filter", this.filter)
                .put("showRankingScore", this.showRankingScore)
                .put("showRankingScoreDetails", this.showRankingScoreDetails)
                .put("rankingScoreThreshold", this.rankingScoreThreshold)
                .put("retrieveVectors", this.retrieveVectors);

            return jsonObject.toString();
    }
}
