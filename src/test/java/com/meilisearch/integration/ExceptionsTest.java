package com.meilisearch.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.meilisearch.sdk.exceptions.MeiliSearchException;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
public class ExceptionsTest extends AbstractIT {
	

	@BeforeEach
	public void initialize() {
		this.setUp();
	}

	@AfterAll
	static void cleanMeiliSearch() {
		cleanup();
	}

	/**
	 * Test MeiliSearchApiError serialization and getters
	 */
	@Test
	public void testErrorSerializeAndGetters() throws Exception {
		String errorMessage = "You must have an authorization token";
		String errorCode = "missing_authorization_header";
		String errorType = "authentication_error";
		String errorLink = "https://docs.meilisearch.com/errors#missing_authorization_header";
		try {
			throw new MeiliSearchApiException(
				"{"
				+ "\"message\":\"" + errorMessage + "\","
				+ "\"errorCode\":\"" + errorCode + "\","
				+ "\"errorType\":\"" + errorType + "\","
				+ "\"errorLink\":\"" + errorLink + "\""
				+ "}");
		} catch (MeiliSearchApiException e) {
			assertEquals(errorMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(errorType, e.getErrorType());
			assertEquals(errorLink, e.getErrorLink());
		}
	}
}
