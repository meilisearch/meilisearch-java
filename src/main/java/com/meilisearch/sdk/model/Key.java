package com.meilisearch.sdk.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Data structure of Meilisearch response for a Key
 *
 * <p>https://www.meilisearch.com/docs/reference/api/keys
 */
@Getter
public class Key {
    @Setter
    @Accessors(chain = true)
    protected String name = null;

    @Setter
    @Accessors(chain = true)
    protected String description = null;

    protected String uid = null;

    protected String key = null;

    @Setter
    @Accessors(chain = true)
    protected String[] actions = null;

    @Setter
    @Accessors(chain = true)
    protected String[] indexes = null;

    @Setter
    @Accessors(chain = true)
    protected Date expiresAt = null;

    protected Date createdAt = null;
    protected Date updatedAt = null;

    public Key() {}
}
