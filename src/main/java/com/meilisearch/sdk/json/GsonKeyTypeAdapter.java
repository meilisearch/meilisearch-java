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
import java.util.TimeZone;

public class GsonKeyTypeAdapter extends TypeAdapter<Key> {

    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_UID = "uid";
    private static final String KEY_KEY = "key";
    private static final String KEY_ACTIONS = "actions";
    private static final String KEY_INDEXES = "indexes";
    private static final String KEY_EXPIRES_AT = "expiresAt";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_UPDATED_AT = "updatedAt";
    private static final DateFormat DATE_FORMAT = createUtcDateFormat();

    private static DateFormat createUtcDateFormat() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

    @Override
    public void write(JsonWriter writer, Key key) throws IOException {
        boolean serializeNulls = writer.getSerializeNulls();
        writer.setSerializeNulls(true);

        try {
            writeStartObject(writer);
            writeString(writer, KEY_NAME, key.getName());
            writeString(writer, KEY_DESCRIPTION, key.getDescription());
            writeString(writer, KEY_UID, key.getUid());
            writeString(writer, KEY_KEY, key.getKey());
            writeStringArray(writer, KEY_ACTIONS, key.getActions());
            writeStringArray(writer, KEY_INDEXES, key.getIndexes());
            if (key.getExpiresAt() != null) {
                writeDate(writer, KEY_EXPIRES_AT, key.getExpiresAt());
            } else {
                writeNull(writer, KEY_EXPIRES_AT);
            }
            writeDate(writer, KEY_CREATED_AT, key.getCreatedAt());
            writeDate(writer, KEY_UPDATED_AT, key.getUpdatedAt());
            writeEndObject(writer);
        } finally {
            writer.setSerializeNulls(serializeNulls);
        }
    }

    @Override
    public Key read(JsonReader reader) throws IOException {
        return nullOrElse(
                reader,
                r -> {
                    Key key = new Key();
                    readStartObject(r);
                    while (r.peek() != JsonToken.END_OBJECT) {
                        switch (r.nextName()) {
                            case KEY_NAME:
                                key.setName(readString(r));
                                break;
                            case KEY_DESCRIPTION:
                                key.setDescription(readString(r));
                                break;
                            case KEY_UID:
                                key.setUid(readString(r));
                                break;
                            case KEY_KEY:
                                key.setKey(readString(r));
                                break;
                            case KEY_ACTIONS:
                                key.setActions(readStringArray(r));
                                break;
                            case KEY_INDEXES:
                                key.setIndexes(readStringArray(r));
                                break;
                            case KEY_EXPIRES_AT:
                                key.setExpiresAt(readDate(r));
                                break;
                            case KEY_CREATED_AT:
                                key.setCreatedAt(readDate(r));
                                break;
                            case KEY_UPDATED_AT:
                                key.setUpdatedAt(readDate(r));
                                break;
                            default:
                                readAndDiscard(r);
                                break;
                        }
                    }
                    readEndObject(r);
                    return key;
                });
    }

    private void writeStartObject(JsonWriter writer) throws IOException {
        writer.beginObject();
    }

    private void writeEndObject(JsonWriter writer) throws IOException {
        writer.endObject();
    }

    private void writeNull(JsonWriter writer, String key) throws IOException {
        writer.name(key).nullValue();
    }

    private void writeString(JsonWriter writer, String key, String value) throws IOException {
        if (value == null) {
            return;
        }
        writer.name(key).value(value);
    }

    private void writeStringArray(JsonWriter writer, String key, String[] value)
            throws IOException {
        if (value == null) {
            return;
        }

        writer.name(key).beginArray();
        for (String item : value) {
            writer.value(item);
        }
        writer.endArray();
    }

    private void writeDate(JsonWriter writer, String key, Date value) throws IOException {
        if (value == null) {
            return;
        }
        writer.name(key).value(DATE_FORMAT.format(value));
    }

    private void readStartObject(JsonReader reader) throws IOException {
        reader.beginObject();
    }

    private void readEndObject(JsonReader reader) throws IOException {
        reader.endObject();
    }

    private String readString(JsonReader reader) throws IOException {
        return nullOrElse(reader, JsonReader::nextString);
    }

    private String[] readStringArray(JsonReader reader) throws IOException {
        return nullOrElse(
                reader,
                r -> {
                    List<String> values = new ArrayList<>();
                    r.beginArray();
                    while (r.peek() != JsonToken.END_ARRAY) {
                        values.add(r.nextString());
                    }
                    r.endArray();
                    return values.toArray(new String[0]);
                });
    }

    private Date readDate(JsonReader reader) throws IOException {
        return nullOrElse(reader, r -> parseDate(r.nextString()));
    }

    private void readAndDiscard(JsonReader reader) throws IOException {
        reader.skipValue();
    }

    private Date parseDate(String value) {
        return Date.from(Instant.parse(value));
    }

    private <T> T nullOrElse(JsonReader reader, ReadValueFunction<T> fn) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        return fn.apply(reader);
    }

    private interface ReadValueFunction<T> {

        T apply(JsonReader reader) throws IOException;
    }
}
