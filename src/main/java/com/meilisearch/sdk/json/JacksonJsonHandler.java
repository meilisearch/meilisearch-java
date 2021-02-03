package com.meilisearch.sdk.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JacksonJsonHandler implements JsonHandler {

	private final ObjectMapper mapper;

	/**
	 * this constructor uses a default ObjectMapper with enabled 'FAIL_ON_UNKNOWN_PROPERTIES' feature.
	 */
	public JacksonJsonHandler() {
		this.mapper = new ObjectMapper();
		this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * @param mapper ObjectMapper
	 */
	public JacksonJsonHandler(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String encode(Object o) throws Exception {
		if (o != null && o.getClass() == String.class) {
			return (String) o;
		}
		try {
			return mapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			// todo: use dedicated exception
			throw new RuntimeException("Error while serializing: ", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T decode(Object o, Class<?> targetClass, Class<?>... parameters) throws Exception {
		if (o == null) {
			// todo: use dedicated exception
			throw new RuntimeException("String to deserialize is null");
		}
		if (targetClass == String.class) {
			return (T) o;
		}
		try {
			if (parameters == null || parameters.length == 0) {
				return (T) mapper.readValue((String) o, targetClass);
			} else {
				return mapper.readValue((String) o, mapper.getTypeFactory().constructParametricType(targetClass, parameters));
			}
		} catch (IOException e) {
			// todo: use dedicated exception
			throw new RuntimeException("Error while serializing: ", e);
		}
	}
}
