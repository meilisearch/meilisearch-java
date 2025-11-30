package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.meilisearch.sdk.model.FilterableAttribute;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class FilterableAttributeTest {

    @Test
    public void testSinglePatternConstructor() {
        FilterableAttribute attribute = new FilterableAttribute("attribute1");
        assertArrayEquals(new String[] {"attribute1"}, attribute.getPatterns());
        assertFalse(attribute.getFacetSearch());
        assertTrue(attribute.getFilter().get("equality"));
        assertFalse(attribute.getFilter().get("comparison"));
    }

    @Test
    public void testSinglePatternConstructorWithGeo() {
        FilterableAttribute attribute = new FilterableAttribute("_geo");
        assertArrayEquals(new String[] {"_geo"}, attribute.getPatterns());
        assertTrue(attribute.getFacetSearch());
        assertTrue(attribute.getFilter().get("equality"));
        assertTrue(attribute.getFilter().get("comparison"));
    }

    @Test
    public void testArrayPatternConstructor() {
        FilterableAttribute attribute =
                new FilterableAttribute(new String[] {"attribute1", "attribute2"});
        assertArrayEquals(new String[] {"attribute1", "attribute2"}, attribute.getPatterns());
        assertFalse(attribute.getFacetSearch());
        assertTrue(attribute.getFilter().get("equality"));
        assertFalse(attribute.getFilter().get("comparison"));
    }

    @Test
    public void testArrayPatternConstructorWithGeo() {
        FilterableAttribute attribute =
                new FilterableAttribute(new String[] {"attribute1", "_geo"});
        assertArrayEquals(new String[] {"attribute1", "_geo"}, attribute.getPatterns());
        assertTrue(attribute.getFacetSearch());
        assertTrue(attribute.getFilter().get("equality"));
        assertTrue(attribute.getFilter().get("comparison"));
    }

    @Test
    public void testFullConstructorValidInput() {
        Map<String, Boolean> filters = new HashMap<>();
        filters.put("equality", true);
        filters.put("comparison", true);
        FilterableAttribute attribute =
                new FilterableAttribute(new String[] {"attribute1"}, true, filters);
        assertArrayEquals(new String[] {"attribute1"}, attribute.getPatterns());
        assertTrue(attribute.getFacetSearch());
        assertEquals(filters, attribute.getFilter());
    }

    @Test
    public void testFullConstructorWithGeoValidation() {
        Map<String, Boolean> filters = new HashMap<>();
        filters.put("equality", true);
        filters.put("comparison", true);
        FilterableAttribute attribute =
                new FilterableAttribute(new String[] {"_geo"}, true, filters);
        assertArrayEquals(new String[] {"_geo"}, attribute.getPatterns());
        assertTrue(attribute.getFacetSearch());
        assertEquals(filters, attribute.getFilter());
    }

    @Test
    public void testFullConstructorWithInvalidGeoValidation() {
        Map<String, Boolean> filters = new HashMap<>();
        filters.put("equality", false);
        filters.put("comparison", false);
        Exception exception =
                assertThrows(
                        RuntimeException.class,
                        () -> new FilterableAttribute(new String[] {"_geo"}, false, filters));
        assertTrue(exception.getMessage().contains("Invalid filter for geo pattern"));
    }

    @Test
    public void testFullConstructorWithNullPatterns() {
        Map<String, Boolean> filters = new HashMap<>();
        filters.put("equality", true);
        filters.put("comparison", true);
        Exception exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new FilterableAttribute(null, true, filters));
        assertEquals("Patterns cannot be null", exception.getMessage());
    }
}
