package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.exceptions.APIError;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;
import org.junit.jupiter.api.*;

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
	 * Test MeiliSearchApiException serialization and getters
	 */
	@Test
	public void testErrorSerializeAndGetters() {
		String errorMessage = "You must have an authorization token";
		String errorCode = "missing_authorization_header";
		String errorType = "authentication_error";
		String errorLink = "https://docs.meilisearch.com/errors#missing_authorization_header";
		try {
			throw new MeiliSearchApiException(new APIError(errorMessage,errorCode,errorType,errorLink));
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
	@Disabled
	public void testMeiliSearchApiExceptionBadRequest () throws Exception {
		String indexUid = "MeiliSearchApiExceptionBadRequest";
		Index index = client.createIndex(indexUid);
		assertThrows(
			MeiliSearchApiException.class,
			() -> client.createIndex(indexUid)
		);
		try {
			client.createIndex(indexUid);
		} catch (MeiliSearchApiException e) {
			assertEquals("index_already_exists", e.getErrorCode());
		}
	}
}
