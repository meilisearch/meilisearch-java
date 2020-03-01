package meilisearch.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Index extends DefaultModel implements Serializable {
    String name;
    String uid;

    public String getName() {
        return this.name;
    }

    public String getUid() {
        return this.uid;
    }

    public ZonedDateTime getCreatedAt() {
        return ZonedDateTime.parse(this.createdAt);
    }

    public ZonedDateTime getUpdatedAt() {
        return ZonedDateTime.parse(this.createdAt);
    }

}
