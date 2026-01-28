package com.meilisearch.sdk.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Jackson deserializer that accepts string or object entries for task filterable attributes. */
public class TaskDetailsFilterableAttributesDeserializer extends JsonDeserializer<String[]> {
    @Override
    public String[] deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        if (node == null || !node.isArray()) {
            return null;
        }

        List<String> attributes = new ArrayList<>();
        for (JsonNode element : node) {
            if (element == null || element.isNull()) {
                attributes.add(null);
                continue;
            }

            if (element.isTextual()) {
                attributes.add(element.asText());
                continue;
            }

            if (element.isObject()) {
                JsonNode patterns = element.get("attributePatterns");
                if (patterns != null && patterns.isArray() && patterns.size() > 0) {
                    JsonNode first = patterns.get(0);
                    attributes.add(first == null || first.isNull() ? null : first.asText());
                } else {
                    attributes.add(null);
                }
            }
        }

        return attributes.toArray(new String[0]);
    }
}
