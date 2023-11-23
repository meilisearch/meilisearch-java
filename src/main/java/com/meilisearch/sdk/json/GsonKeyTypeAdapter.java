package com.meilisearch.sdk.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.meilisearch.sdk.model.Key;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GsonKeyTypeAdapter extends TypeAdapter<Key> {

    private static final DateFormat DATE_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    public void write(JsonWriter writer, Key key) throws IOException {
        boolean serializeNulls = writer.getSerializeNulls();

        writer.beginObject();
        writer.setSerializeNulls(true);
        writeOptionalString(writer, "name", key.getName());
        writeOptionalString(writer, "description", key.getDescription());
        writeOptionalString(writer, "uid", key.getUid());
        writeOptionalString(writer, "key", key.getKey());
        writeStringArray(writer, "actions", key.getActions());
        writeStringArray(writer, "indexes", key.getIndexes());
        if (key.getExpiresAt() == null) {
            writeNull(writer, "expiresAt");
        } else {
            writeDate(writer, "expiresAt", key.getExpiresAt());
        }
        writeDate(writer, "createdAt", key.getCreatedAt());
        writeDate(writer, "updatedAt", key.getUpdatedAt());
        writer.endObject();

        writer.setSerializeNulls(serializeNulls);
    }

    @Override
    public Key read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            return null;
        }

        Key key = new Key();
        reader.beginObject();
        while (reader.peek() != JsonToken.END_OBJECT) {
            String name = reader.nextName();
            switch (name) {
                case "name":
                    key.setName(readString(reader));
                    break;
                case "description":
                    key.setDescription(readString(reader));
                    break;
                case "uid":
                    key.setUid(readString(reader));
                    break;
                case "key":
                    key.setKey(readString(reader));
                    break;
                case "actions":
                    key.setActions(readStringArray(reader));
                    break;
                case "indexes":
                    key.setIndexes(readStringArray(reader));
                    break;
                case "expiresAt":
                    key.setExpiresAt(readDate(reader));
                    break;
                case "createdAt":
                    key.setCreatedAt(readDate(reader));
                    break;
                case "updatedAt":
                    key.setUpdatedAt(readDate(reader));
                    break;
                default:
                    discardNext(reader);
                    break;
            }
        }

        reader.endObject();
        return key;
    }

    private String readString(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        return reader.nextString();
    }

    private String[] readStringArray(JsonReader reader) throws IOException {
        List<String> values = new ArrayList<>();
        reader.beginArray();
        while (reader.peek() != JsonToken.END_ARRAY) {
            values.add(reader.nextString());
        }
        reader.endArray();
        return values.toArray(new String[0]);
    }

    private Date readDate(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        String value = reader.nextString();
        return Date.from(Instant.parse(value));
    }

    private JsonWriter writeNull(JsonWriter writer, String key) throws IOException {
        return writer.name(key).nullValue();
    }

    private JsonWriter writeOptionalString(JsonWriter writer, String key, String value) throws IOException {
        if (value == null) {
            return writer;
        }
        return writer.name(key).value(value);
    }

    private JsonWriter writeStringArray(JsonWriter writer, String key, String[] value) throws IOException {
        if (value == null) {
            return writer;
        }

        writer.name(key).beginArray();
        for (String item : value) {
            writer.value(item);
        }
        return writer.endArray();
    }

    private JsonWriter writeDate(JsonWriter writer, String key, Date value) throws IOException {
        if (value == null) {
            return writer;
        }
        return writer.name(key).value(DATE_FORMAT.format(value));
    }

    private JsonReader discardNext(JsonReader reader) throws IOException {
        reader.skipValue();
        return reader;
    }
}
