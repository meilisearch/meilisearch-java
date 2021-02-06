package com.meilisearch.sdk.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;

public class GsonJsonHandler implements JsonHandler {
	private final Gson gson;

	public GsonJsonHandler() {
		this.gson = new Gson();
	}

	public GsonJsonHandler(Gson gson) {
		this.gson = gson;
	}

	@Override
	public String encode(Object o) throws Exception {
		if (o != null && o.getClass() == String.class) {
			return (String) o;
		}
		try {
			return gson.toJson(o);
		} catch (Exception e) {
			// todo: use dedicated exception
			throw new MeiliSearchRuntimeException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T decode(Object o, Class<?> targetClass, Class<?>... parameters) throws Exception {
		if (o == null) {
			// todo: use dedicated exception
			throw new MeiliSearchRuntimeException();
		}
		if (targetClass == String.class) {
			return (T) o;
		}
		try {
			if (parameters == null || parameters.length == 0) {
				return (T) gson.fromJson((String) o, targetClass);
			} else {
				TypeToken<?> parameterized = TypeToken.getParameterized(targetClass, parameters);
				return gson.fromJson((String) o, parameterized.getType());
			}
		} catch (Exception e) {
			// todo: use dedicated exception
			throw new MeiliSearchRuntimeException(e);
		}
	}
}
