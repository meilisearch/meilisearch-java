package com.meilisearch.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.meilisearch.sdk.model.MatchingStrategy;
import org.junit.jupiter.api.Test;

class SearchRequestTest {

    @Test
    void toStringSimpleQuery() {
        SearchRequest classToTest = SearchRequest.builder().q("This is a Test").build();

        assertEquals("{\"q\":\"This is a Test\"}", classToTest.toString());
    }

    @Test
    void toStringSimpleQueryWithBuilder() {
        SearchRequest classToTest = SearchRequest.builder().q("This is a Test").build();

        assertEquals("{\"q\":\"This is a Test\"}", classToTest.toString());
    }

    @Test
    void toStringQueryAndOffset() {
        SearchRequest classToTest = SearchRequest.builder().q("This is a Test").offset(200).build();

        assertEquals("{\"q\":\"This is a Test\",\"offset\":200}", classToTest.toString());
    }

    @Test
    void toStringQueryAndOffsetWithBuilder() {
        SearchRequest classToTest = SearchRequest.builder().q("This is a Test").offset(200).build();

        assertEquals("{\"q\":\"This is a Test\",\"offset\":200}", classToTest.toString());
    }

    @Test
    void toStringQueryLimitAndOffset() {
        SearchRequest classToTest = SearchRequest.builder().q("This is a Test").offset(200).limit(900).build();

        assertEquals(
                "{\"q\":\"This is a Test\",\"offset\":200,\"limit\":900}", classToTest.toString());
    }

    @Test
    void toStringQueryLimitAndOffsetWithBuilder() {
        SearchRequest classToTest =
                SearchRequest.builder().q("This is a Test").offset(200).limit(900).build();

        assertEquals(
                "{\"q\":\"This is a Test\",\"offset\":200,\"limit\":900}", classToTest.toString());
    }

    @Test
    void toStringQueryLimitOffsetAndAttributesToRetrieve() {
        SearchRequest classToTest = SearchRequest.builder().q("This is a Test").offset(200).limit(900).attributesToRetrieve(new String[] {"bubble"}).build();

        assertEquals(
                "{\"q\":\"This is a Test\",\"attributesToRetrieve\":[\"bubble\"],\"offset\":200,\"limit\":900}",
                classToTest.toString());
    }

    @Test
    void toStringQueryLimitOffsetAndAttributesToRetrieveWithBuilder() {
        SearchRequest classToTest =
                SearchRequest.builder()
                        .q("This is a Test")
                        .attributesToRetrieve(new String[] {"bubble"})
                        .limit(900)
                        .offset(200)
                        .build();

        assertEquals(
                "{\"q\":\"This is a Test\",\"attributesToRetrieve\":[\"bubble\"],\"offset\":200,\"limit\":900}",
                classToTest.toString());
    }

    @Test
    void toStringQueryLimitOffsetAndPageAndHitPerPage() {
        SearchRequest classToTest =
                SearchRequest.builder()
                        .q("This is a Test")
                        .limit(20)
                        .offset(0)
                        .page(10)
                        .hitsPerPage(2)
                        .build();

        assertEquals(
                "{\"q\":\"This is a Test\",\"offset\":0,\"hitsPerPage\":2,\"limit\":20,\"page\":10}",
                classToTest.toString());
    }

    @Test
    void toStringEveryParameters() {
        SearchRequest classToTest =
            SearchRequest
                .builder()
                .q("This is a Test")
                .offset(200)
                .limit(900)
                .attributesToRetrieve(new String[] {"bubble"})
                .attributesToHighlight(new String[] {"highlight"})
                .attributesToCrop(new String[] {"crop"})
                .cropLength(900)
                .filter(new String[] {"test='test'"})
                .facets(new String[] {"facets"})
                .sort(new String[] {"sort"})
                .page(10)
                .hitsPerPage(2)
                .build();

        assertEquals("This is a Test", classToTest.getQ());
        assertEquals(200, classToTest.getOffset());
        assertEquals(900, classToTest.getLimit());
        assertEquals("bubble", classToTest.getAttributesToRetrieve()[0]);
        assertEquals("highlight", classToTest.getAttributesToHighlight()[0]);
        assertEquals("crop", classToTest.getAttributesToCrop()[0]);
        assertEquals(null, classToTest.getCropMarker());
        assertEquals(null, classToTest.getHighlightPreTag());
        assertEquals(null, classToTest.getHighlightPostTag());
        assertEquals(null, classToTest.getFilterArray());
        assertEquals(1, classToTest.getFilter().length);
        assertEquals("test='test'", classToTest.getFilter()[0]);
        assertEquals("facets", classToTest.getFacets()[0]);
        assertEquals("sort", classToTest.getSort()[0]);
        assertEquals(900, classToTest.getCropLength());
        assertEquals(10, classToTest.getPage());
        assertEquals(2, classToTest.getHitsPerPage());
    }

    @Test
    void toStringEveryParametersWithBuilder() {
        SearchRequest classToTest =
                SearchRequest.builder()
                        .q("This is a Test")
                        .limit(900)
                        .offset(200)
                        .attributesToRetrieve(new String[] {"bubble"})
                        .attributesToHighlight(new String[] {"highlight"})
                        .attributesToCrop(new String[] {"crop"})
                        .cropLength(900)
                        .filter(new String[] {"test='test'"})
                        .facets(new String[] {"facets"})
                        .sort(new String[] {"sort"})
                        .page(10)
                        .hitsPerPage(2)
                        .build();

        assertEquals("This is a Test", classToTest.getQ());
        assertEquals(200, classToTest.getOffset());
        assertEquals(900, classToTest.getLimit());
        assertEquals("bubble", classToTest.getAttributesToRetrieve()[0]);
        assertEquals("highlight", classToTest.getAttributesToHighlight()[0]);
        assertEquals("crop", classToTest.getAttributesToCrop()[0]);
        assertEquals(null, classToTest.getCropMarker());
        assertEquals(null, classToTest.getHighlightPreTag());
        assertEquals(null, classToTest.getHighlightPostTag());
        assertEquals(null, classToTest.getFilterArray());
        assertEquals(1, classToTest.getFilter().length);
        assertEquals("test='test'", classToTest.getFilter()[0]);
        assertEquals("facets", classToTest.getFacets()[0]);
        assertEquals("sort", classToTest.getSort()[0]);
        assertEquals(900, classToTest.getCropLength());
        assertEquals(10, classToTest.getPage());
        assertEquals(2, classToTest.getHitsPerPage());
    }

    @Test
    void toStringEveryParametersWithArray() {
        SearchRequest classToTest = SearchRequest.builder()
            .q("This is a Test")
            .limit(900)
            .offset(200)
            .attributesToRetrieve(new String[] {"bubble"})
            .attributesToHighlight(new String[] {"highlight"})
            .attributesToCrop(new String[] {"crop"})
            .cropLength(900)
            .cropMarker("123")
            .highlightPreTag("abc")
            .highlightPostTag("zyx")
            .matchingStrategy(MatchingStrategy.ALL)
            .filterArray((
                new String[][] {
                    new String[] {"test='test'"}, new String[] {"test1='test1'"}
                }))
            .showMatchesPosition(true)
            .facets(new String[] {"facets"})
            .sort(new String[] {"sort"})
            .page(0)
            .hitsPerPage(0)
            .build();

        assertEquals("This is a Test", classToTest.getQ());
        assertEquals(200, classToTest.getOffset());
        assertEquals(900, classToTest.getLimit());
        assertEquals("123", classToTest.getCropMarker());
        assertEquals("abc", classToTest.getHighlightPreTag());
        assertEquals(MatchingStrategy.ALL, classToTest.getMatchingStrategy());
        assertEquals("zyx", classToTest.getHighlightPostTag());
        assertEquals("bubble", classToTest.getAttributesToRetrieve()[0]);
        assertEquals("highlight", classToTest.getAttributesToHighlight()[0]);
        assertEquals("crop", classToTest.getAttributesToCrop()[0]);
        assertEquals(null, classToTest.getFilter());
        assertEquals(2, classToTest.getFilterArray().length);
        assertEquals("test='test'", classToTest.getFilterArray()[0][0]);
        assertEquals("test1='test1'", classToTest.getFilterArray()[1][0]);
        assertEquals("facets", classToTest.getFacets()[0]);
        assertEquals("sort", classToTest.getSort()[0]);
        assertEquals(900, classToTest.getCropLength());
        assertEquals(0, classToTest.getPage());
        assertEquals(0, classToTest.getHitsPerPage());
        assertEquals(
                "{\"attributesToRetrieve\":[\"bubble\"],\"offset\":200,\"cropMarker\":\"123\",\"hitsPerPage\":0,\"sort\":[\"sort\"],\"highlightPreTag\":\"abc\",\"facets\":[\"facets\"],\"filter\":[[\"test='test'\"],[\"test1='test1'\"]],\"q\":\"This is a Test\",\"matchingStrategy\":\"all\",\"showMatchesPosition\":true,\"limit\":900,\"cropLength\":900,\"highlightPostTag\":\"zyx\",\"attributesToHighlight\":[\"highlight\"],\"page\":0,\"attributesToCrop\":[\"crop\"]}",
                classToTest.toString());
    }

    @Test
    void toStringEveryParametersWithArrayWithBuilder() {
        SearchRequest classToTest =
                SearchRequest.builder()
                        .q("This is a Test")
                        .limit(900)
                        .offset(200)
                        .attributesToRetrieve(new String[] {"bubble"})
                        .attributesToHighlight(new String[] {"highlight"})
                        .attributesToCrop(new String[] {"crop"})
                        .cropLength(900)
                        .cropMarker("123")
                        .highlightPreTag("abc")
                        .highlightPostTag("zyx")
                        .matchingStrategy(MatchingStrategy.ALL)
                        .filterArray(
                                new String[][] {
                                    new String[] {"test='test'"}, new String[] {"test1='test1'"}
                                })
                        .showMatchesPosition(true)
                        .facets(new String[] {"facets"})
                        .sort(new String[] {"sort"})
                        .page(0)
                        .hitsPerPage(0)
                        .build();

        assertEquals("This is a Test", classToTest.getQ());
        assertEquals(200, classToTest.getOffset());
        assertEquals(900, classToTest.getLimit());
        assertEquals("123", classToTest.getCropMarker());
        assertEquals("abc", classToTest.getHighlightPreTag());
        assertEquals(MatchingStrategy.ALL, classToTest.getMatchingStrategy());
        assertEquals("zyx", classToTest.getHighlightPostTag());
        assertEquals("bubble", classToTest.getAttributesToRetrieve()[0]);
        assertEquals("highlight", classToTest.getAttributesToHighlight()[0]);
        assertEquals("crop", classToTest.getAttributesToCrop()[0]);
        assertEquals(null, classToTest.getFilter());
        assertEquals(2, classToTest.getFilterArray().length);
        assertEquals("test='test'", classToTest.getFilterArray()[0][0]);
        assertEquals("test1='test1'", classToTest.getFilterArray()[1][0]);
        assertEquals("facets", classToTest.getFacets()[0]);
        assertEquals("sort", classToTest.getSort()[0]);
        assertEquals(900, classToTest.getCropLength());
        assertEquals(0, classToTest.getPage());
        assertEquals(0, classToTest.getHitsPerPage());
        assertEquals(
                "{\"attributesToRetrieve\":[\"bubble\"],\"offset\":200,\"cropMarker\":\"123\",\"hitsPerPage\":0,\"sort\":[\"sort\"],\"highlightPreTag\":\"abc\",\"facets\":[\"facets\"],\"filter\":[[\"test='test'\"],[\"test1='test1'\"]],\"q\":\"This is a Test\",\"matchingStrategy\":\"all\",\"showMatchesPosition\":true,\"limit\":900,\"cropLength\":900,\"highlightPostTag\":\"zyx\",\"attributesToHighlight\":[\"highlight\"],\"page\":0,\"attributesToCrop\":[\"crop\"]}",
                classToTest.toString());
    }

    @Test
    void toStringEveryParametersWithArrayMatchingStrategyNull() {
        SearchRequest classToTest =
                SearchRequest.builder()
                        .q("This is a Test")
                        .limit(900)
                        .offset(200)
                        .attributesToRetrieve(new String[] {"bubble"})
                        .attributesToHighlight(new String[] {"highlight"})
                        .attributesToCrop(new String[] {"crop"})
                        .cropLength(900)
                        .cropMarker("123")
                        .highlightPreTag("abc")
                        .highlightPostTag("zyx")
                        .matchingStrategy(null)
                        .filterArray(
                                new String[][] {
                                    new String[] {"test='test'"}, new String[] {"test1='test1'"}
                                })
                        .showMatchesPosition(true)
                        .facets(new String[] {"facets"})
                        .sort(new String[] {"sort"})
                        .page(0)
                        .hitsPerPage(0)
                        .build();

        assertEquals("This is a Test", classToTest.getQ());
        assertEquals(200, classToTest.getOffset());
        assertEquals(900, classToTest.getLimit());
        assertEquals("123", classToTest.getCropMarker());
        assertEquals("abc", classToTest.getHighlightPreTag());
        assertEquals(null, classToTest.getMatchingStrategy());
        assertEquals("zyx", classToTest.getHighlightPostTag());
        assertEquals("bubble", classToTest.getAttributesToRetrieve()[0]);
        assertEquals("highlight", classToTest.getAttributesToHighlight()[0]);
        assertEquals("crop", classToTest.getAttributesToCrop()[0]);
        assertEquals(null, classToTest.getFilter());
        assertEquals(2, classToTest.getFilterArray().length);
        assertEquals("test='test'", classToTest.getFilterArray()[0][0]);
        assertEquals("test1='test1'", classToTest.getFilterArray()[1][0]);
        assertEquals("facets", classToTest.getFacets()[0]);
        assertEquals("sort", classToTest.getSort()[0]);
        assertEquals(900, classToTest.getCropLength());
        assertEquals(
                "{\"attributesToRetrieve\":[\"bubble\"],\"offset\":200,\"cropMarker\":\"123\",\"hitsPerPage\":0,\"sort\":[\"sort\"],\"highlightPreTag\":\"abc\",\"facets\":[\"facets\"],\"filter\":[[\"test='test'\"],[\"test1='test1'\"]],\"q\":\"This is a Test\",\"showMatchesPosition\":true,\"limit\":900,\"cropLength\":900,\"highlightPostTag\":\"zyx\",\"attributesToHighlight\":[\"highlight\"],\"page\":0,\"attributesToCrop\":[\"crop\"]}",
                classToTest.toString());
    }
}
