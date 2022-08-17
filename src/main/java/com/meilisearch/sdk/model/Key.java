package com.meilisearch.sdk.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * Data structure of MeiliSearch response for a Key
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/keys.html
 */
@Getter
public class Key {
    @Setter protected String description = null;
    protected String key = "";
    @Setter protected String[] actions = null;
    @Setter protected String[] indexes = null;
    @Setter protected Date expiresAt = null;
    protected Date createdAt = null;
    protected Date updatedAt = null;

    public Key() {}
}
