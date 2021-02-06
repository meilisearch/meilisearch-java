package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.sdk.api.index.Index;
import com.meilisearch.sdk.exceptions.APIError;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;
import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
	 * Test MeiliSearchApiException serialization and getters
	 */
	@Test
	public void testErrorSerializeAndGetters() {
		String errorMessage = "You must have an authorization token";
		String errorCode = "missing_authorization_header";
		String errorType = "authentication_error";
		String errorLink = "https://docs.meilisearch.com/errors#missing_authorization_header";
		try {
			throw new MeiliSearchApiException(new APIError(errorMessage, errorCode, errorType, errorLink));
		} catch (MeiliSearchApiException e) {
			assertEquals(errorMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(errorType, e.getErrorType());
			assertEquals(errorLink, e.getErrorLink());
		}
	}

	/**
	 * Test MeiliSearchApiException is thrown on MeiliSearch bad request
	 */
	@Test
	public void testMeiliSearchApiExceptionBadRequest() throws Exception {
		String indexUid = "MeiliSearchApiExceptionBadRequest";
		Index index = client.index().createIndex(indexUid);
		assertThrows(
			MeiliSearchRuntimeException.class,
			() -> client.index().createIndex(indexUid)
		);
		try {
			client.index().createIndex(indexUid);
		} catch (MeiliSearchRuntimeException e) {
			if (e.getCause().getClass() == MeiliSearchApiException.class) {
				assertEquals("index_already_exists", ((MeiliSearchApiException) e.getCause()).getErrorCode());
			}
		}
	}
}
