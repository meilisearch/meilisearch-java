package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Data structure for updating a Key
 *
 * <p>https://www.meilisearch.com/docs/reference/api/keys
 */
@Getter
public class KeyUpdate {
    @Setter
    @Accessors(chain = true)
    protected String name = null;

    @Setter
    @Accessors(chain = true)
    protected String description = null;

    public KeyUpdate() {}
}
