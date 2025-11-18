package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Swap Indexes Params data structure
 */
@Getter
@Setter
@Accessors(chain = true)
public class SwapIndexesParams {
    protected String[] indexes;
    protected Boolean rename;

    public SwapIndexesParams() {
    }
}
