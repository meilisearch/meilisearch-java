package com.meilisearch.sdk;

import lombok.*;
import org.json.JSONObject;

@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class ExportIndexFilter {
    private String filter;
    @Builder.Default private boolean overrideSettings = false;

    /**
     * Method that returns the JSON String of the ExportRequest
     *
     * @return JSON String of the ExportRequest query
     */
    @Override
    public String toString() {
        JSONObject jsonObject =
                new JSONObject()
                        .putOpt("filter", this.filter)
                        .putOpt("overrideSettings", this.overrideSettings);
        return jsonObject.toString();
    }
}
