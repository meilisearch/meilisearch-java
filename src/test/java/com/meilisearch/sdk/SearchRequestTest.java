package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import com.meilisearch.sdk.model.Hybrid;
import com.meilisearch.sdk.model.MatchingStrategy;
import org.junit.jupiter.api.Test;

class SearchRequestTest {

    @Test
    void toStringSimpleQuery() {
        SearchRequest classToTest = new SearchRequest("This is a Test");

        assertThat(classToTest.toString(), is(equalTo("{\"q\":\"This is a Test\"}")));
    }

    @Test
    void toStringSimpleQueryWithBuilder() {
        SearchRequest classToTest = SearchRequest.builder().q("This is a Test").build();

        assertThat(classToTest.toString(), is(equalTo("{\"q\":\"This is a Test\"}")));
    }

    @Test
    void toStringQueryAndOffset() {
        SearchRequest classToTest =
                new SearchRequest("This is a Test").setQuery("This is a Test").setOffset(200);

        assertThat(
                classToTest.toString(), is(equalTo("{\"q\":\"This is a Test\",\"offset\":200}")));
    }

    @Test
    void toStringQueryAndOffsetWithBuilder() {
        SearchRequest classToTest = SearchRequest.builder().q("This is a Test").offset(200).build();

        assertThat(
                classToTest.toString(), is(equalTo("{\"q\":\"This is a Test\",\"offset\":200}")));
    }

    @Test
    void toStringQueryLimitAndOffset() {
        SearchRequest classToTest =
                new SearchRequest("This is a Test").setOffset(200).setLimit(900);

        assertThat(
                classToTest.toString(),
                is(equalTo("{\"q\":\"This is a Test\",\"offset\":200,\"limit\":900}")));
    }

    @Test
    void toStringQueryLimitAndOffsetWithBuilder() {
        SearchRequest classToTest =
                SearchRequest.builder().q("This is a Test").offset(200).limit(900).build();

        assertThat(
                classToTest.toString(),
                is(equalTo("{\"q\":\"This is a Test\",\"offset\":200,\"limit\":900}")));
    }

    @Test
    void toStringQueryLimitOffsetAndAttributesToRetrieve() {
        SearchRequest classToTest =
                new SearchRequest("This is a Test")
                        .setOffset(200)
                        .setLimit(900)
                        .setAttributesToRetrieve(new String[] {"bubble"});
        String expected =
                "{\"q\":\"This is a Test\",\"attributesToRetrieve\":[\"bubble\"],\"offset\":200,\"limit\":900}";

        assertThat(classToTest.toString(), is(equalTo(expected)));
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
        String expected =
                "{\"q\":\"This is a Test\",\"attributesToRetrieve\":[\"bubble\"],\"offset\":200,\"limit\":900}";

        assertThat(classToTest.toString(), is(equalTo(expected)));
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
        String expected =
                "{\"q\":\"This is a Test\",\"offset\":0,\"hitsPerPage\":2,\"limit\":20,\"page\":10}";

        assertThat(classToTest.toString(), is(equalTo(expected)));
    }

    @Test
    void toStringQueryDefinitionAttributesToSearchOn() {
        SearchRequest classToTest =
                SearchRequest.builder()
                        .q("This is a Test")
                        .attributesToSearchOn(new String[] {"attribute1", "attribute2"})
                        .build();
        String expected =
                "{\"q\":\"This is a Test\",\"attributesToSearchOn\":[\"attribute1\",\"attribute2\"]}";

        assertThat(classToTest.toString(), is(equalTo(expected)));
    }

    @Test
    void toStringEveryParameters() {
        SearchRequest classToTest =
                new SearchRequest("This is a Test")
                        .setOffset(200)
                        .setLimit(900)
                        .setAttributesToRetrieve(new String[] {"bubble"})
                        .setAttributesToHighlight(new String[] {"highlight"})
                        .setAttributesToSearchOn(new String[] {"searchOn"})
                        .setAttributesToCrop(new String[] {"crop"})
                        .setCropLength(900)
                        .setFilter(new String[] {"test='test'"})
                        .setFacets(new String[] {"facets"})
                        .setSort(new String[] {"sort"})
                        .setPage(10)
                        .setHitsPerPage(2)
                        .setLocales(new String[] {"eng"})
                        .setDistinct("distinct");

        assertThat(classToTest.getQ(), is(equalTo("This is a Test")));
        assertThat(classToTest.getOffset(), is(equalTo(200)));
        assertThat(classToTest.getLimit(), is(equalTo(900)));
        assertThat(classToTest.getAttributesToRetrieve()[0], is(equalTo("bubble")));
        assertThat(classToTest.getAttributesToHighlight()[0], is(equalTo("highlight")));
        assertThat(classToTest.getAttributesToSearchOn()[0], is(equalTo("searchOn")));
        assertThat(classToTest.getAttributesToCrop()[0], is(equalTo("crop")));
        assertThat(classToTest.getCropMarker(), is(nullValue()));
        assertThat(classToTest.getHighlightPreTag(), is(nullValue()));
        assertThat(classToTest.getHighlightPostTag(), is(nullValue()));
        assertThat(classToTest.getFilterArray(), is(nullValue()));
        assertThat(classToTest.getFilter().length, is(equalTo(1)));
        assertThat(classToTest.getFilter()[0], is(equalTo("test='test'")));
        assertThat(classToTest.getFacets()[0], is(equalTo("facets")));
        assertThat(classToTest.getSort()[0], is(equalTo("sort")));
        assertThat(classToTest.getCropLength(), is(equalTo(900)));
        assertThat(classToTest.getPage(), is(equalTo(10)));
        assertThat(classToTest.getHitsPerPage(), is(equalTo(2)));
        assertThat(classToTest.getLocales()[0], is(equalTo("eng")));
        assertThat(classToTest.getDistinct(), is(equalTo("distinct")));
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
                        .attributesToSearchOn(new String[] {"searchOn"})
                        .attributesToCrop(new String[] {"crop"})
                        .cropLength(900)
                        .filter(new String[] {"test='test'"})
                        .facets(new String[] {"facets"})
                        .sort(new String[] {"sort"})
                        .page(10)
                        .hitsPerPage(2)
                        .locales(new String[] {"eng"})
                        .distinct("distinct")
                        .build();

        assertThat(classToTest.getQ(), is(equalTo("This is a Test")));
        assertThat(classToTest.getOffset(), is(equalTo(200)));
        assertThat(classToTest.getLimit(), is(equalTo(900)));
        assertThat(classToTest.getAttributesToRetrieve()[0], is(equalTo("bubble")));
        assertThat(classToTest.getAttributesToHighlight()[0], is(equalTo("highlight")));
        assertThat(classToTest.getAttributesToSearchOn()[0], is(equalTo("searchOn")));
        assertThat(classToTest.getAttributesToCrop()[0], is(equalTo("crop")));
        assertThat(classToTest.getCropMarker(), is(nullValue()));
        assertThat(classToTest.getHighlightPreTag(), is(nullValue()));
        assertThat(classToTest.getHighlightPostTag(), is(nullValue()));
        assertThat(classToTest.getFilterArray(), is(nullValue()));
        assertThat(classToTest.getFilter().length, is(equalTo(1)));
        assertThat(classToTest.getFilter()[0], is(equalTo("test='test'")));
        assertThat(classToTest.getFacets()[0], is(equalTo("facets")));
        assertThat(classToTest.getSort()[0], is(equalTo("sort")));
        assertThat(classToTest.getCropLength(), is(equalTo(900)));
        assertThat(classToTest.getPage(), is(equalTo(10)));
        assertThat(classToTest.getHitsPerPage(), is(equalTo(2)));
        assertThat(classToTest.getLocales()[0], is(equalTo("eng")));
        assertThat(classToTest.getDistinct(), is(equalTo("distinct")));
    }

    @Test
    void toStringEveryParametersWithArray() {
        SearchRequest classToTest =
                new SearchRequest("This is a Test")
                        .setOffset(200)
                        .setLimit(900)
                        .setAttributesToRetrieve(new String[] {"bubble"})
                        .setAttributesToHighlight(new String[] {"highlight"})
                        .setAttributesToSearchOn(new String[] {"searchOn"})
                        .setAttributesToCrop(new String[] {"crop"})
                        .setCropLength(900)
                        .setCropMarker("123")
                        .setHighlightPreTag("abc")
                        .setHighlightPostTag("zyx")
                        .setMatchingStrategy(MatchingStrategy.ALL)
                        .setFilterArray(
                                new String[][] {
                                    new String[] {"test='test'"}, new String[] {"test1='test1'"}
                                })
                        .setShowMatchesPosition(true)
                        .setFacets(new String[] {"facets"})
                        .setSort(new String[] {"sort"})
                        .setPage(0)
                        .setHitsPerPage(0)
                        .setDistinct("distinct")
                        .setLocales(new String[] {"eng"});
        String expectedToString =
                "{\"attributesToRetrieve\":[\"bubble\"],\"offset\":200,\"cropMarker\":\"123\",\"hitsPerPage\":0,\"attributesToSearchOn\":[\"searchOn\"],\"distinct\":\"distinct\",\"sort\":[\"sort\"],\"highlightPreTag\":\"abc\",\"facets\":[\"facets\"],\"filter\":[[\"test='test'\"],[\"test1='test1'\"]],\"q\":\"This is a Test\",\"locales\":[\"eng\"],\"matchingStrategy\":\"all\",\"showMatchesPosition\":true,\"limit\":900,\"cropLength\":900,\"highlightPostTag\":\"zyx\",\"attributesToHighlight\":[\"highlight\"],\"page\":0,\"attributesToCrop\":[\"crop\"]}";

        assertThat(classToTest.getQ(), is(equalTo("This is a Test")));
        assertThat(classToTest.getOffset(), is(equalTo(200)));
        assertThat(classToTest.getLimit(), is(equalTo(900)));
        assertThat(classToTest.getCropMarker(), is(equalTo("123")));
        assertThat(classToTest.getHighlightPreTag(), is(equalTo("abc")));
        assertThat(classToTest.getMatchingStrategy(), is(equalTo(MatchingStrategy.ALL)));
        assertThat(classToTest.getHighlightPostTag(), is(equalTo("zyx")));
        assertThat(classToTest.getAttributesToRetrieve()[0], is(equalTo("bubble")));
        assertThat(classToTest.getAttributesToHighlight()[0], is(equalTo("highlight")));
        assertThat(classToTest.getAttributesToSearchOn()[0], is(equalTo("searchOn")));
        assertThat(classToTest.getAttributesToCrop()[0], is(equalTo("crop")));
        assertThat(classToTest.getFilter(), is(nullValue()));
        assertThat(classToTest.getFilterArray().length, is(equalTo(2)));
        assertThat(classToTest.getFilterArray()[0][0], is(equalTo("test='test'")));
        assertThat(classToTest.getFilterArray()[1][0], is(equalTo("test1='test1'")));
        assertThat(classToTest.getFacets()[0], is(equalTo("facets")));
        assertThat(classToTest.getSort()[0], is(equalTo("sort")));
        assertThat(classToTest.getCropLength(), is(equalTo(900)));
        assertThat(classToTest.getPage(), is(equalTo(0)));
        assertThat(classToTest.getHitsPerPage(), is(equalTo(0)));
        assertThat(classToTest.getLocales()[0], is(equalTo("eng")));
        assertThat(classToTest.getDistinct(), is(equalTo("distinct")));
        assertThat(classToTest.toString(), is(equalTo(expectedToString)));
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
                        .attributesToSearchOn(new String[] {"searchOn"})
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
                        .locales(new String[] {"eng"})
                        .distinct("distinct")
                        .build();
        String expectedToString =
                "{\"attributesToRetrieve\":[\"bubble\"],\"offset\":200,\"cropMarker\":\"123\",\"hitsPerPage\":0,\"attributesToSearchOn\":[\"searchOn\"],\"distinct\":\"distinct\",\"sort\":[\"sort\"],\"highlightPreTag\":\"abc\",\"facets\":[\"facets\"],\"filter\":[[\"test='test'\"],[\"test1='test1'\"]],\"q\":\"This is a Test\",\"locales\":[\"eng\"],\"matchingStrategy\":\"all\",\"showMatchesPosition\":true,\"limit\":900,\"cropLength\":900,\"highlightPostTag\":\"zyx\",\"attributesToHighlight\":[\"highlight\"],\"page\":0,\"attributesToCrop\":[\"crop\"]}";

        assertThat(classToTest.getQ(), is(equalTo("This is a Test")));
        assertThat(classToTest.getOffset(), is(equalTo(200)));
        assertThat(classToTest.getLimit(), is(equalTo(900)));
        assertThat(classToTest.getCropMarker(), is(equalTo("123")));
        assertThat(classToTest.getHighlightPreTag(), is(equalTo("abc")));
        assertThat(classToTest.getMatchingStrategy(), is(equalTo(MatchingStrategy.ALL)));
        assertThat(classToTest.getHighlightPostTag(), is(equalTo("zyx")));
        assertThat(classToTest.getAttributesToRetrieve()[0], is(equalTo("bubble")));
        assertThat(classToTest.getAttributesToHighlight()[0], is(equalTo("highlight")));
        assertThat(classToTest.getAttributesToSearchOn()[0], is(equalTo("searchOn")));
        assertThat(classToTest.getAttributesToCrop()[0], is(equalTo("crop")));
        assertThat(classToTest.getFilter(), is(equalTo(null)));
        assertThat(classToTest.getFilterArray().length, is(equalTo(2)));
        assertThat(classToTest.getFilterArray()[0][0], is(equalTo("test='test'")));
        assertThat(classToTest.getFilterArray()[1][0], is(equalTo("test1='test1'")));
        assertThat(classToTest.getFacets()[0], is(equalTo("facets")));
        assertThat(classToTest.getSort()[0], is(equalTo("sort")));
        assertThat(classToTest.getCropLength(), is(equalTo(900)));
        assertThat(classToTest.getPage(), is(equalTo(0)));
        assertThat(classToTest.getHitsPerPage(), is(equalTo(0)));
        assertThat(classToTest.getLocales()[0], is(equalTo("eng")));
        assertThat(classToTest.getDistinct(), is(equalTo("distinct")));
        assertThat(classToTest.toString(), is(equalTo(expectedToString)));
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
                        .attributesToSearchOn(new String[] {"searchOn"})
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
                        .distinct("distinct")
                        .build();
        String expectedToString =
                "{\"attributesToRetrieve\":[\"bubble\"],\"offset\":200,\"cropMarker\":\"123\",\"hitsPerPage\":0,\"attributesToSearchOn\":[\"searchOn\"],\"distinct\":\"distinct\",\"sort\":[\"sort\"],\"highlightPreTag\":\"abc\",\"facets\":[\"facets\"],\"filter\":[[\"test='test'\"],[\"test1='test1'\"]],\"q\":\"This is a Test\",\"showMatchesPosition\":true,\"limit\":900,\"cropLength\":900,\"highlightPostTag\":\"zyx\",\"attributesToHighlight\":[\"highlight\"],\"page\":0,\"attributesToCrop\":[\"crop\"]}";

        assertThat(classToTest.getQ(), is(equalTo("This is a Test")));
        assertThat(classToTest.getOffset(), is(equalTo(200)));
        assertThat(classToTest.getLimit(), is(equalTo(900)));
        assertThat(classToTest.getCropMarker(), is(equalTo("123")));
        assertThat(classToTest.getHighlightPreTag(), is(equalTo("abc")));
        assertThat(classToTest.getMatchingStrategy(), is(equalTo(null)));
        assertThat(classToTest.getHighlightPostTag(), is(equalTo("zyx")));
        assertThat(classToTest.getAttributesToRetrieve()[0], is(equalTo("bubble")));
        assertThat(classToTest.getAttributesToHighlight()[0], is(equalTo("highlight")));
        assertThat(classToTest.getAttributesToSearchOn()[0], is(equalTo("searchOn")));
        assertThat(classToTest.getAttributesToCrop()[0], is(equalTo("crop")));
        assertThat(classToTest.getFilter(), is(equalTo(null)));
        assertThat(classToTest.getFilterArray().length, is(equalTo(2)));
        assertThat(classToTest.getFilterArray()[0][0], is(equalTo("test='test'")));
        assertThat(classToTest.getFilterArray()[1][0], is(equalTo("test1='test1'")));
        assertThat(classToTest.getFacets()[0], is(equalTo("facets")));
        assertThat(classToTest.getSort()[0], is(equalTo("sort")));
        assertThat(classToTest.getCropLength(), is(equalTo(900)));
        assertThat(classToTest.getDistinct(), is(equalTo("distinct")));
        assertThat(classToTest.toString(), is(equalTo(expectedToString)));
    }

    @Test
    void toStringWithHybrid() {
        Hybrid hybrid = Hybrid.builder().semanticRatio(0.5).embedder("default").build();

        SearchRequest classToTest = new SearchRequest("This is a Test").setHybrid(hybrid);

        String expected =
                "{\"q\":\"This is a Test\",\"hybrid\":{\"semanticRatio\":0.5,\"embedder\":\"default\"}}";
        assertThat(classToTest.toString(), is(equalTo(expected)));

        // Verify getters
        assertThat(classToTest.getHybrid().getSemanticRatio(), is(equalTo(0.5)));
        assertThat(classToTest.getHybrid().getEmbedder(), is(equalTo("default")));
    }

    @Test
    void toStringWithHybridUsingBuilder() {
        SearchRequest classToTest =
                SearchRequest.builder()
                        .q("This is a Test")
                        .hybrid(Hybrid.builder().semanticRatio(0.5).embedder("default").build())
                        .build();

        String expected =
                "{\"q\":\"This is a Test\",\"hybrid\":{\"semanticRatio\":0.5,\"embedder\":\"default\"}}";
        assertThat(classToTest.toString(), is(equalTo(expected)));

        // Verify getters
        assertThat(classToTest.getHybrid().getSemanticRatio(), is(equalTo(0.5)));
        assertThat(classToTest.getHybrid().getEmbedder(), is(equalTo("default")));
    }

    @Test
    void toStringWithHybridAndOtherParameters() {
        SearchRequest classToTest =
                SearchRequest.builder()
                        .q("This is a Test")
                        .offset(200)
                        .limit(900)
                        .hybrid(
                                Hybrid.builder()
                                        .semanticRatio(0.7)
                                        .embedder("custom-embedder")
                                        .build())
                        .build();

        String expected =
                "{\"q\":\"This is a Test\",\"hybrid\":{\"semanticRatio\":0.7,\"embedder\":\"custom-embedder\"},\"offset\":200,\"limit\":900}";
        assertThat(classToTest.toString(), is(equalTo(expected)));
    }

    @Test
    void toStringWithHybridOnlyEmbedder() {
        SearchRequest classToTest =
                new SearchRequest("This is a Test")
                        .setHybrid(Hybrid.builder().embedder("default").build());

        String expected = "{\"q\":\"This is a Test\",\"hybrid\":{\"embedder\":\"default\"}}";
        assertThat(classToTest.toString(), is(equalTo(expected)));
    }
}
