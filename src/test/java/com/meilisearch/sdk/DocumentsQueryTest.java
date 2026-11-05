package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.meilisearch.sdk.model.DocumentsQuery;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class DocumentsQueryTest {

    @Test
    void toQueryIncludesIds() {
        DocumentsQuery query = new DocumentsQuery().setIds(new String[] {"1", "abc"});

        assertThat(query.toQuery(), is(equalTo("?ids=1,abc")));
    }

    @Test
    void toStringIncludesIds() {
        DocumentsQuery query = new DocumentsQuery().setIds(new String[] {"1", "abc"});

        JSONObject json = new JSONObject(query.toString());

        assertThat(json.getJSONArray("ids").length(), is(equalTo(2)));
        assertThat(json.getJSONArray("ids").getString(0), is(equalTo("1")));
        assertThat(json.getJSONArray("ids").getString(1), is(equalTo("abc")));
    }

    @Test
    void toStringOmitsIdsWhenNotSet() {
        JSONObject json = new JSONObject(new DocumentsQuery().toString());

        assertThat(json.has("ids"), is(false));
    }
}
