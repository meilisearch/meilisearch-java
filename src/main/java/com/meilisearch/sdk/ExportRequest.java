package com.meilisearch.sdk;

import java.util.Map;
import lombok.*;
import org.json.JSONObject;

@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class ExportRequest {
    private String url;
    private String apiKey;
    private String payloadSize;
    private Map<String, ExportIndexFilter> indexes;

    /**
     * Method that returns the JSON String of the ExportRequest
     *
     * @return JSON String of the ExportRequest query
     */
    @Override
    public String toString() {
        JSONObject jsonObject =
                new JSONObject()
                        .put("url", this.url)
                        .putOpt("apiKey", this.apiKey)
                        .putOpt("payloadSize", this.payloadSize)
                        .putOpt("indexes", this.indexes);
        return jsonObject.toString();
    }
}
