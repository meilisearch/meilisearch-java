package com.meilisearch.sdk.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.meilisearch.sdk.model.TaskDetails;
import java.io.IOException;

/**
 * Normalizes the filterableAttributes field in TaskDetails when Gson deserializes mixed string/object
 * arrays returned by Meilisearch.
 */
public class GsonTaskDetailsTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!TaskDetails.class.isAssignableFrom(type.getRawType())) {
            return null;
        }

        final TypeAdapter<TaskDetails> delegate =
                gson.getDelegateAdapter(this, TypeToken.get(TaskDetails.class));
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

        return (TypeAdapter<T>)
                new TypeAdapter<TaskDetails>() {
                    @Override
                    public void write(JsonWriter out, TaskDetails value) throws IOException {
                        delegate.write(out, value);
                    }

                    @Override
                    public TaskDetails read(JsonReader in) throws IOException {
                        JsonElement tree = elementAdapter.read(in);
                        if (tree != null && tree.isJsonObject()) {
                            JsonObject obj = tree.getAsJsonObject();
                            if (obj.has("filterableAttributes")
                                    && obj.get("filterableAttributes").isJsonArray()) {
                                JsonArray source = obj.getAsJsonArray("filterableAttributes");
                                JsonArray normalized = new JsonArray();
                                for (JsonElement element : source) {
                                    if (element == null || element.isJsonNull()) {
                                        normalized.add(JsonNull.INSTANCE);
                                        continue;
                                    }
                                    if (element.isJsonPrimitive()
                                            && element.getAsJsonPrimitive().isString()) {
                                        normalized.add(element);
                                        continue;
                                    }
                                    if (element.isJsonObject()) {
                                        JsonObject o = element.getAsJsonObject();
                                        JsonElement patterns = o.get("attributePatterns");
                                        if (patterns != null
                                                && patterns.isJsonArray()
                                                && patterns.getAsJsonArray().size() > 0) {
                                            JsonElement first = patterns.getAsJsonArray().get(0);
                                            normalized.add(
                                                    first == null || first.isJsonNull()
                                                            ? JsonNull.INSTANCE
                                                            : first);
                                        } else {
                                            normalized.add(JsonNull.INSTANCE);
                                        }
                                        continue;
                                    }
                                    normalized.add(JsonNull.INSTANCE);
                                }
                                obj.add("filterableAttributes", normalized);
                            }
                        }
                        return delegate.fromJsonTree(tree);
                    }
                };
    }
}
