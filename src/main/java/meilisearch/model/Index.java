package meilisearch.model;

import java.io.Serializable;

public class Index extends DefaultModel implements Serializable {
    String name;
    String uid;

    public String getName() {
        return this.name;
    }

    public String getUid() {
        return this.uid;
    }
}
