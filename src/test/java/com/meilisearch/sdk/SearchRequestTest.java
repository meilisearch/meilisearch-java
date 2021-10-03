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
                        new String[] {"test='test'"},
                        true,
                        new String[] {"facets"},
                        new String[] {"sort"});
        assertEquals("This is a Test", classToTest.getQ());
        assertEquals(200, classToTest.getOffset());
        assertEquals(900, classToTest.getLimit());
        assertEquals("bubble", classToTest.getAttributesToRetrieve()[0]);
        assertEquals("highlight", classToTest.getAttributesToHighlight()[0]);
        assertEquals("crop", classToTest.getAttributesToCrop()[0]);
        assertEquals(null, classToTest.getFilterArray());
        assertEquals(1, classToTest.getFilter().length);
        assertEquals("test='test'", classToTest.getFilter()[0]);
        assertEquals("facets", classToTest.getFacetsDistribution()[0]);
        assertEquals("sort", classToTest.getSort()[0]);
        assertEquals(900, classToTest.getCropLength());

        classToTest =
                new SearchRequest(
                        "This is a Test",
                        200,
                        900,
                        new String[] {"bubble"},
                        new String[] {"crop"},
                        900,
                        new String[] {"highlight"},
                        new String[][] {
                            new String[] {"test='test'"}, new String[] {"test1='test1'"}
                        },
                        true,
                        new String[] {"facets"},
                        new String[] {"sort"});
        assertEquals("This is a Test", classToTest.getQ());
        assertEquals(200, classToTest.getOffset());
        assertEquals(900, classToTest.getLimit());
        assertEquals("bubble", classToTest.getAttributesToRetrieve()[0]);
        assertEquals("highlight", classToTest.getAttributesToHighlight()[0]);
        assertEquals("crop", classToTest.getAttributesToCrop()[0]);
        assertEquals(null, classToTest.getFilter());
        assertEquals(2, classToTest.getFilterArray().length);
        assertEquals("test='test'", classToTest.getFilterArray()[0][0]);
        assertEquals("test1='test1'", classToTest.getFilterArray()[1][0]);
        assertEquals("facets", classToTest.getFacetsDistribution()[0]);
        assertEquals("sort", classToTest.getSort()[0]);
        assertEquals(900, classToTest.getCropLength());
        assertEquals(
                "{\"filter\":[[\"test='test'\"],[\"test1='test1'\"]],\"q\":\"This is a Test\",\"attributesToRetrieve\":[\"bubble\"],\"offset\":200,\"facetsDistribution\":[\"facets\"],\"limit\":900,\"cropLength\":900,\"attributesToHighlight\":[\"highlight\"],\"sort\":[\"sort\"],\"matches\":true,\"attributesToCrop\":[\"crop\"]}",
                classToTest.getQuery());
    }
}
