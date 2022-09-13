package com.meilisearch.sdk.json;

import com.meilisearch.sdk.exceptions.MeilisearchException;

public interface JsonHandler {
    /**
     * @param o the Object to serialize
     * @return the serialized Object {@code o}
     * @throws MeilisearchException wrapped exceptions of the used json library
     */
    String encode(Object o) throws MeilisearchException;

    /**
     * @param o Object to deserialize, most of the time this is a string
     * @param targetClass return type
     * @param parameters in case the return type is a generic class, this is a list of types to use
     *     with that generic.
     * @param <T> Abstract type to deserialize
     * @return the deserialized object
     * @throws MeilisearchException wrapped exceptions of the used json library
     */
    <T> T decode(Object o, Class<?> targetClass, Class<?>... parameters)
            throws MeilisearchException;
}
