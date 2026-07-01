package com.meilisearch.sdk.model;

import com.meilisearch.sdk.http.URLBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Data structure of the query parameters for the stats routes.
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/stats">API specification</a>
 */
@Setter
@Getter
@Accessors(chain = true)
public class StatsQuery {
    private Boolean showInternalDatabaseSizes;
    private String sizeFormat;

    public StatsQuery() {}

    public String toQuery() {
        URLBuilder urlb =
                new URLBuilder()
                        .addParameter(
                                "showInternalDatabaseSizes",
                                this.getShowInternalDatabaseSizes())
                        .addParameter("sizeFormat", this.getSizeFormat());
        return urlb.getURL();
    }
}
