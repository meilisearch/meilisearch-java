package com.meilisearch.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SearchRequestTest {

    @Test
    void getQuery() {
        SearchRequest classToTest = new SearchRequest("This is a Test");
        assertEquals(
                "{\"q\":\"This is a Test\",\"attributesToRetrieve\":[\"*\"],\"offset\":0,\"limit\":20,\"cropLength\":200,\"matches\":false}",
                classToTest.getQuery());
        classToTest = new SearchRequest("This is a Test", 200);
        assertEquals(
                "{\"q\":\"This is a Test\",\"attributesToRetrieve\":[\"*\"],\"offset\":200,\"limit\":20,\"cropLength\":200,\"matches\":false}",
                classToTest.getQuery());
        classToTest = new SearchRequest("This is a Test", 200, 900);
        assertEquals(
                "{\"q\":\"This is a Test\",\"attributesToRetrieve\":[\"*\"],\"offset\":200,\"limit\":900,\"cropLength\":200,\"matches\":false}",
                classToTest.getQuery());
        classToTest = new SearchRequest("This is a Test", 200, 900, new String[] {"bubble"});
        assertEquals(
                "{\"q\":\"This is a Test\",\"attributesToRetrieve\":[\"bubble\"],\"offset\":200,\"limit\":900,\"cropLength\":200,\"matches\":false}",
                classToTest.getQuery());

        classToTest =
                new SearchRequest(
                        "This is a Test",
                        200,
                        900,
                        new String[] {"bubble"},
                        new String[] {"crop"},
                        900,
                        new String[] {"highlight"},
                        "test='test'",
                        true,
                        new String[] {"facets"},
                        new String[] {"sort"});
        assertEquals("This is a Test", classToTest.getQ());
        assertEquals(200, classToTest.getOffset());
        assertEquals(900, classToTest.getLimit());
        assertEquals("bubble", classToTest.getAttributesToRetrieve()[0]);
        assertEquals("highlight", classToTest.getAttributesToHighlight()[0]);
        assertEquals("crop", classToTest.getAttributesToCrop()[0]);
        assertEquals("test='test'", classToTest.getFilter());
        assertEquals("facets", classToTest.getFacetsDistribution()[0]);
        assertEquals("sort", classToTest.getSort()[0]);
        assertEquals(900, classToTest.getCropLength());
    }
}
