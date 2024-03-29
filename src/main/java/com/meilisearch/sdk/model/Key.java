package com.meilisearch.sdk.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Data structure of Meilisearch response for a Key
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/keys">API specification</a>
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class Key {
    @Accessors(chain = true)
    protected String name = null;

    @Accessors(chain = true)
    protected String description = null;

    protected String uid = null;

    protected String key = null;

    @Accessors(chain = true)
    protected String[] actions = null;

    @Accessors(chain = true)
    protected String[] indexes = null;

    @Accessors(chain = true)
    @JsonInclude(Include.ALWAYS)
    protected Date expiresAt = null;

    protected Date createdAt = null;
    protected Date updatedAt = null;

    public Key() {}
}
