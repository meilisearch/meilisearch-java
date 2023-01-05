package com.meilisearch.sdk.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.meilisearch.sdk.exceptions.JsonDecodingException;
import com.meilisearch.sdk.exceptions.JsonEncodingException;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.Key;

public class GsonJsonHandler implements JsonHandler {
    private Gson gson;

    public GsonJsonHandler() {
        this.gson = new Gson();
    }

    public GsonJsonHandler(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String encode(Object o) throws MeilisearchException {
        if (o != null && o.getClass() == String.class) {
            return (String) o;
        }
        // TODO: review later
        // This is a workaround to encode the Key class properly
        if (o != null && o.getClass() == Key.class) {
            GsonBuilder builder = new GsonBuilder();
            this.gson = builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

            Key key = (Key) o;
            if (key.getExpiresAt() == null) {
                JsonElement jsonElement = gson.toJsonTree(o);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                jsonObject.add("expiresAt", JsonNull.INSTANCE);
                o = jsonObject;
                this.gson =
                        builder.serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
            }
        }
        try {
            return gson.toJson(o);
        } catch (Exception e) {
            throw new JsonEncodingException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T decode(Object o, Class<?> targetClass, Class<?>... parameters)
            throws MeilisearchException {
        if (o == null) {
            throw new JsonDecodingException("Response to deserialize is null");
        }
        if (targetClass == String.class) {
            return (T) o;
        }
        try {
            if (parameters == null || parameters.length == 0) {
                return gson.<T>fromJson((String) o, targetClass);
            } else {
                TypeToken<?> parameterized = TypeToken.getParameterized(targetClass, parameters);
                return gson.<T>fromJson((String) o, parameterized.getType());
            }
        } catch (JsonSyntaxException e) {
            throw new JsonDecodingException(e);
        }
    }
}
