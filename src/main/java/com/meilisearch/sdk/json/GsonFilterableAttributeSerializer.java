package com.meilisearch.sdk.json;

import com.google.gson.*;
import com.meilisearch.sdk.model.FilterableAttribute;
import java.lang.reflect.Type;
import java.util.*;

/** JSON serializer/deserializer for {@link FilterableAttribute} objects. */
public class GsonFilterableAttributeSerializer
        implements JsonSerializer<FilterableAttribute>, JsonDeserializer<FilterableAttribute> {

    @Override
    public JsonElement serialize(
            FilterableAttribute attributes,
            Type type,
            JsonSerializationContext jsonSerializationContext) {
        // when possible, limit size of data sent by using legacy string format
        return (canBeString(attributes))
                ? new JsonPrimitive(attributes.getPatterns()[0])
                : serializeAttribute(attributes);
    }

    private boolean canBeString(FilterableAttribute attribute) {
        if (attribute == null) return false;
        Map<String, Boolean> filters = attribute.getFilter();
        if (filters == null) filters = new HashMap<>();

        boolean equalityAllowed   = !filters.containsKey("equality") || filters.get("equality");
        boolean comparisonAllowed =  filters.getOrDefault("comparison", false);

        return attribute.getPatterns() != null
                && attribute.getPatterns().length == 1
                && (attribute.getFacetSearch() == null || !attribute.getFacetSearch())
                && equalityAllowed
                && !comparisonAllowed;
    }

    private JsonElement serializeAttribute(FilterableAttribute attribute) {
        if (attribute == null) return null;
        List<Exception> exceptions = new ArrayList<>();
        JsonArray patternArray = new JsonArray();
        if (attribute.getPatterns() != null && attribute.getPatterns().length > 0)
            try {
                // Collect values from POJO
                patternArray =
                        Arrays.stream(attribute.getPatterns())
                                .map(JsonPrimitive::new)
                                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
            } catch (Exception e) {
                exceptions.add(e);
            }
        else exceptions.add(new JsonParseException("Patterns to filter for were not specified!"));

        JsonObject filters = new JsonObject();
        if (attribute.getFilter() != null) {
            try {
                filters =
                        attribute.getFilter().entrySet().stream()
                                .collect(
                                        JsonObject::new,
                                        (jsonObject, kv) ->
                                                jsonObject.addProperty(kv.getKey(), kv.getValue()),
                                        this::combineJsonObjects);
            } catch (Exception e) {
                exceptions.add(e);
            }
        } else {
            filters.addProperty("comparison", false);
            filters.addProperty("equality", true);
        }

        if (!exceptions.isEmpty()) {
            throw new JsonParseException(String.join("\n", Arrays.toString(exceptions.toArray())));
        }

        // Create JSON object
        JsonObject jsonObject = new JsonObject();
        JsonObject features = new JsonObject();
        if (attribute.getFacetSearch() != null)
            features.addProperty("facetSearch", attribute.getFacetSearch());
        else features.addProperty("facetSearch", false);
        features.add("filter", filters);
        jsonObject.add("attributePatterns", patternArray);
        jsonObject.add("features", features);
        return jsonObject;
    }

    private void combineJsonObjects(JsonObject a, JsonObject b) {
        for (Map.Entry<String, JsonElement> kv : b.entrySet()) a.add(kv.getKey(), kv.getValue());
    }

    @Override
    public FilterableAttribute deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        try {
            // legacy check
            if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString())
                return new FilterableAttribute(jsonElement.getAsString());

            JsonObject object = jsonElement.getAsJsonObject();
            JsonObject features =
                    object.has("features") ? object.getAsJsonObject("features") : null;
            // default values for instance lacking `features`
            boolean facetSearch = false;
            Map<String, Boolean> filters = new HashMap<>();
            filters.put("equality", true);
            filters.put("comparison", false);

            List<Exception> exceptions = new ArrayList<>();
            // pull values from features.
            if (features != null && features.has("facetSearch")) {
                try {
                    JsonPrimitive facetSearchPrimitive = features.getAsJsonPrimitive("facetSearch");
                    facetSearch =
                            facetSearchPrimitive != null && facetSearchPrimitive.getAsBoolean();
                } catch (ClassCastException | IllegalStateException e) {
                    exceptions.add(e);
                }
            }
            if (features != null && features.has("filter"))
                try {
                    filters =
                            features.getAsJsonObject("filter").entrySet().stream()
                                    .collect(
                                            HashMap::new,
                                            (m, kv) ->
                                                    m.put(
                                                            kv.getKey(),
                                                            kv.getValue().getAsBoolean()),
                                            HashMap::putAll);
                } catch (ClassCastException | IllegalStateException e) {
                    exceptions.add(e);
                }
            String[] patterns = new String[0];
            try {
                patterns =
                        object.has("attributePatterns")
                                ? object.getAsJsonArray("attributePatterns").asList().stream()
                                        .map(JsonElement::getAsString)
                                        .toArray(String[]::new)
                                : new String[0];
            } catch (ClassCastException | IllegalStateException e) {
                exceptions.add(e);
            }

            if (!exceptions.isEmpty())
                throw new JsonParseException(
                        String.join("\n", Arrays.toString(exceptions.toArray())));

            if (filters.entrySet().stream().noneMatch(Map.Entry::getValue))
                exceptions.add(
                        new JsonParseException(
                                "No filtration methods were allowed! Must have at least one type <comparison, equality> allowed.\n"
                                        + Arrays.toString(filters.entrySet().toArray())));
            if (patterns.length == 0)
                exceptions.add(
                        new JsonParseException(
                                "Patterns to filter for were not specified! Invalid Attribute."));

            if (!exceptions.isEmpty())
                throw new JsonParseException(
                        String.join("\n", Arrays.toString(exceptions.toArray())));

            return new FilterableAttribute(patterns, facetSearch, filters);
        } catch (Exception e) {
            throw new JsonParseException("Failed to deserialize FilterableAttribute", e);
        }
    }
}
