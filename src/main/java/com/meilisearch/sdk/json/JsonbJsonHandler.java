package com.meilisearch.sdk.json;


import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;

public class JsonbJsonHandler implements JsonHandler {

	private final Jsonb mapper;

	/**
	 * this constructor uses a default ObjectMapper with enabled 'FAIL_ON_UNKNOWN_PROPERTIES' feature.
	 */
	public JsonbJsonHandler() {
		JsonbConfig config = new JsonbConfig().withNullValues(false);
		this.mapper = JsonbBuilder.create(config);
	}

	/**
	 * @param mapper ObjectMapper
	 */
	public JsonbJsonHandler(Jsonb mapper) {
		this.mapper = mapper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String encode(Object o) throws Exception {
		if (o.getClass() == String.class) {
			return (String) o;
		}
		try {
			return mapper.toJson(o);
		} catch (JsonbException e) {
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
		return (T) mapper.fromJson((String) o, targetClass);
	}
}
