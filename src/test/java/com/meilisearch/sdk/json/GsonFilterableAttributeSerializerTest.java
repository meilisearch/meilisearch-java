package com.meilisearch.sdk.json;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonParseException;
import com.meilisearch.sdk.model.FilterableAttribute;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GsonFilterableAttributeSerializerTest {

    GsonFilterableAttributeSerializer serializer;
    GsonJsonHandler handler;

    @BeforeEach
    public void preTestSetup() {
        handler = new GsonJsonHandler();
    }

    @AfterEach
    public void postTestCleanup() {
        // Cleanup after each test, if needed
    }

    @Test
    public void testLegacySerialization() {
        String input = "attribute1";
        String expectedOutput = "\"attribute1\"";
        String result = handler.encode(new FilterableAttribute(input));
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testLegacyDeserialization() {
        String input = "\"attribute1\"";
        FilterableAttribute expectedOutput = new FilterableAttribute("attribute1");
        assertDeserializedOutputsEquals(
                handler.decode(input, FilterableAttribute.class), expectedOutput);
    }

    @Test
    public void testNewDeserializationOutputArray() {
        String input = "{\"attributePatterns\":[\"attribute1\"]}";
        FilterableAttribute expectedOutput = new FilterableAttribute("attribute1");
        assertDeserializedOutputsEquals(
                handler.decode(input, FilterableAttribute.class), expectedOutput);
    }

    @Test
    public void testNewSerializationOutputArray() {
        FilterableAttribute input = new FilterableAttribute(new String[] {"attribute1"});
        String expectedOutput = "\"attribute1\"";
        assertEquals(expectedOutput, handler.encode(input));
    }

    @Test
    public void testMixedDeserializationOutputWithNoFeatures() {
        String input = "[{attributePatterns:[\"attribute1\", \"attribute2\"]},\"attribute3\"]";
        FilterableAttribute expectedOutputOne =
                new FilterableAttribute(new String[] {"attribute1", "attribute2"});
        FilterableAttribute expectedOutputTwo = new FilterableAttribute("attribute3");
        FilterableAttribute[] expectedOutput =
                new FilterableAttribute[] {expectedOutputOne, expectedOutputTwo};
        FilterableAttribute[] result = handler.decode(input, FilterableAttribute[].class);
        assertEquals(expectedOutput.length, result.length);
        for (int i = 0; i < expectedOutput.length; i++) {
            assertDeserializedOutputsEquals(expectedOutput[i], result[i]);
        }
    }

    @Test
    public void testDeserializationWithFeatures() {
        String input =
                "{attributePatterns:[\"attribute1\", \"attribute2\"],features: {facetSearch:true, filter:{equality:true, comparison:true}}}";
        Map<String, Boolean> filters = new HashMap<>();
        filters.put("equality", true);
        filters.put("comparison", true);
        FilterableAttribute expectedOutput =
                new FilterableAttribute(new String[] {"attribute1", "attribute2"}, true, filters);
        FilterableAttribute result = handler.decode(input, FilterableAttribute.class);
        assertDeserializedOutputsEquals(expectedOutput, result);
    }

    @Test
    public void testMixedSerializationWithFeatures() {
        HashMap<String, Boolean> filters = new HashMap<>();
        filters.put("equality", false);
        filters.put("comparison", true);
        FilterableAttribute input =
                new FilterableAttribute(new String[] {"attribute1", "attribute2"}, true, filters);
        FilterableAttribute input2 = new FilterableAttribute("attribute3");
        String expectedOutput =
                "[{\"attributePatterns\":[\"attribute1\",\"attribute2\"],\"features\":{\"facetSearch\":true,\"filter\":{\"comparison\":true,\"equality\":false}}},\"attribute3\"]";
        String array = handler.encode(new FilterableAttribute[] {input, input2});
        assertEquals(expectedOutput, array);
    }

    @Test
    public void testDeserializationWithNullElement() {
        assertNull(handler.decode("null", FilterableAttribute.class));
    }

    @Test
    public void testDeserializationWithInvalidFilter() {
        String input =
                "{attributePatterns:[\"attribute1\"],features: {facetSearch:true, filter:{equality:false, comparison:false}}}";
        Exception exception =
                assertThrows(
                        JsonParseException.class,
                        () -> handler.decode(input, FilterableAttribute.class));
        assertTrue(exception.getMessage().contains("Failed to deserialize"));
        assertTrue(
                exception.getCause().getMessage().contains("No filtration methods were allowed"));
    }

    @Test
    public void testDeserializationWithMissingPatterns() {
        String input = "{features: {facetSearch:true, filter:{equality:true, comparison:true}}}";
        Exception exception =
                assertThrows(
                        JsonParseException.class,
                        () -> handler.decode(input, FilterableAttribute.class));
        assertTrue(exception.getMessage().contains("Failed to deserialize"));
        assertTrue(
                exception
                        .getCause()
                        .getMessage()
                        .contains("Patterns to filter for were not specified"));
    }

    @Test
    public void testSerializationWithNullAttribute() {
        FilterableAttribute input = null;
        String result = handler.encode(input);
        assertEquals("null", result);
    }

    @Test
    public void testSerializationWithEmptyPatterns() {
        FilterableAttribute input = new FilterableAttribute(new String[0], false, new HashMap<>());
        Exception exception = assertThrows(Exception.class, () -> handler.encode(input));
        assertTrue(
                exception
                        .getCause()
                        .getMessage()
                        .contains("Patterns to filter for were not specified"));
    }

    private static void assertDeserializedOutputsEquals(
            FilterableAttribute a, FilterableAttribute b) {
        if (a == null) assertNull(b);
        else {
            assertEquals(a.getPatterns().length, b.getPatterns().length);
            for (int i = 0; i < a.getPatterns().length; i++) {
                assertEquals(a.getPatterns()[i], b.getPatterns()[i]);
            }
            assertEquals(a.getFacetSearch(), b.getFacetSearch());
            for (String key : a.getFilter().keySet()) {
                assertEquals(a.getFilter().get(key), b.getFilter().get(key));
            }
        }
    }
}
