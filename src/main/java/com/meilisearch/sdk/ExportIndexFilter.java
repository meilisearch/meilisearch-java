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
     * Method that returns the JSON String of the ExportIndexFilter
     *
     * @return JSON String of the ExportIndexFilter query
     */
    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        if (this.filter != null) {
            jsonObject.put("filter", this.filter);
        }
        if (this.overrideSettings) {
            jsonObject.put("overrideSettings", true);
        }

        return jsonObject.toString();
    }
}
