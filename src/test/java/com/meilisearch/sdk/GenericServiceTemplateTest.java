package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;
import com.meilisearch.sdk.http.AbstractHttpClient;
import com.meilisearch.sdk.http.factory.BasicRequestFactory;
import com.meilisearch.sdk.http.request.BasicHttpRequest;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.response.BasicHttpResponse;
import com.meilisearch.sdk.json.JsonHandler;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenericServiceTemplateTest {

	private final AbstractHttpClient client = mock(AbstractHttpClient.class);
	private final Config config = new Config("http://localhost:7700","masterKey");
	private final JsonHandler handler = mock(JsonHandler.class);
	private final GenericServiceTemplate classToTest = new GenericServiceTemplate(client, handler, new BasicRequestFactory(handler, config));

	@Test
	void getClient() {
		assertEquals(client, classToTest.getClient());
	}

	@Test
	void getProcessor() {
		assertEquals(handler, classToTest.getProcessor());
	}

	@Test
	void executeGet() throws Exception {
		when(client.get(any(BasicHttpRequest.class))).thenAnswer(invocationOnMock -> {
			BasicHttpRequest request = invocationOnMock.getArgument(0);
			return new MockHttpResponse(request.getPath(), request.getHeaders(), request.getContent());
		});
		BasicHttpRequest request = new BasicHttpRequest(HttpMethod.GET, "/path", Collections.emptyMap(), null);
		MockHttpResponse response = classToTest.execute(request, null);
		assertEquals(request.getPath(), response.getRequestPath());
		assertEquals(request.getHeaders(), response.getHeaders());
	}

	@Test
	void executePost() throws Exception {
		when(client.post(any())).thenAnswer(invocationOnMock -> {
			BasicHttpRequest request = invocationOnMock.getArgument(0);
			return new MockHttpResponse(request.getPath(), request.getHeaders(), request.getContent());
		}).thenAnswer(invocationOnMock -> {
			BasicHttpRequest request = invocationOnMock.getArgument(0);
			return new MockHttpResponse(request.getPath(), request.getHeaders(), request.getContent());
		});
		BasicHttpRequest fullRequest = new BasicHttpRequest(HttpMethod.POST, "/path", Collections.emptyMap(), "content");
		MockHttpResponse response = classToTest.execute(fullRequest, null);
		assertEquals(fullRequest.getPath(), response.getRequestPath());
		assertEquals(fullRequest.getContent(), response.getContent());
		assertEquals(fullRequest.getHeaders(), response.getHeaders());

		MockHttpRequest contentlessRequest = new MockHttpRequest(HttpMethod.POST, "/path", Collections.emptyMap(), null);
		response = classToTest.execute(contentlessRequest, null);
		assertEquals(contentlessRequest.getPath(), response.getRequestPath());
		assertNull(response.getContent());
		assertEquals(contentlessRequest.getHeaders(), response.getHeaders());
	}

	@Test
	void executePut() throws Exception {
		when(client.put(any())).thenAnswer(invocationOnMock -> {
			BasicHttpRequest request = invocationOnMock.getArgument(0);
			return new MockHttpResponse(request.getPath(), request.getHeaders(), request.getContent());
		}).thenAnswer(invocationOnMock -> {
			BasicHttpRequest request = invocationOnMock.getArgument(0);
			return new MockHttpResponse(request.getPath(), request.getHeaders(), request.getContent());
		});
		BasicHttpRequest fullRequest = new BasicHttpRequest(HttpMethod.PUT, "/path", Collections.emptyMap(), "content");
		MockHttpResponse response = classToTest.execute(fullRequest, null);
		assertEquals(fullRequest.getPath(), response.getRequestPath());
		assertEquals(fullRequest.getContent(), response.getContent());
		assertEquals(fullRequest.getHeaders(), response.getHeaders());

		MockHttpRequest contentlessRequest = new MockHttpRequest(HttpMethod.PUT, "/path", Collections.emptyMap(), null);
		response = classToTest.execute(contentlessRequest, null);
		assertEquals(contentlessRequest.getPath(), response.getRequestPath());
		assertNull(response.getContent());
		assertEquals(contentlessRequest.getHeaders(), response.getHeaders());
	}

	@Test
	void executeDelete() throws Exception {
		when(client.delete(any())).thenAnswer(invocationOnMock -> {
			BasicHttpRequest request = invocationOnMock.getArgument(0);
			return new MockHttpResponse(request.getPath(), request.getHeaders(), request.getContent());
		}).thenAnswer(invocationOnMock -> {
			BasicHttpRequest request = invocationOnMock.getArgument(0);
			return new MockHttpResponse(request.getPath(), request.getHeaders(), request.getContent());
		});
		BasicHttpRequest fullRequest = new BasicHttpRequest(HttpMethod.DELETE, "/path", Collections.emptyMap(), "content");
		MockHttpResponse response = classToTest.execute(fullRequest, null);
		assertEquals(fullRequest.getPath(), response.getRequestPath());
		assertNotNull(response.getContent());
		assertEquals(fullRequest.getHeaders(), response.getHeaders());

		MockHttpRequest contentlessRequest = new MockHttpRequest(HttpMethod.DELETE, "/path", Collections.emptyMap(), null);
		response = classToTest.execute(contentlessRequest, null);
		assertEquals(contentlessRequest.getPath(), response.getRequestPath());
		assertNull(response.getContent());
		assertEquals(contentlessRequest.getHeaders(), response.getHeaders());
	}

	@Test
	void defaultBranch() {
		assertThrows(MeiliSearchRuntimeException.class, () -> classToTest.execute(new MockHttpRequest(HttpMethod.HEAD, null, null, null), null));
	}

	public static class MockHttpRequest extends BasicHttpRequest {
		private HttpMethod method;
		private String path;

		private Map<String, String> headers;
		private String content;

		public MockHttpRequest(HttpMethod method, String path, Map<String, String> headers, String content) {
			this.method = method;
			this.path = path;
			this.headers = headers;
			this.content = content;
		}

		@Override
		public HttpMethod getMethod() {
			return method;
		}

		@Override
		public void setMethod(HttpMethod method) {
			this.method = method;
		}

		@Override
		public String getPath() {
			return path;
		}

		@Override
		public void setPath(String path) {
			this.path = path;
		}

		@Override
		public Map<String, String> getHeaders() {
			return headers;
		}

		@Override
		public void setHeaders(Map<String, String> headers) {
			this.headers = headers;
		}

		@Override
		public boolean hasContent() {
			return this.content == null || "".equals(this.content);
		}

		@Override
		public String getContent() {
			return this.content;
		}

		@Override
		public byte[] getContentAsBytes() {
			return new byte[0];
		}
	}

	public static class MockHttpResponse extends BasicHttpResponse {

		private final String requestPath;
		private final Map<String, String> requestHeaders;
		private final String requestBody;

		public MockHttpResponse(String requestPath, Map<String, String> requestHeaders, String requestBody) {
			super(requestHeaders, 200, requestBody);
			this.requestPath = requestPath;
			this.requestHeaders = requestHeaders;
			this.requestBody = requestBody;
		}

		@Override
		public Map<String, String> getHeaders() {
			return requestHeaders;
		}

		@Override
		public int getStatusCode() {
			return 0;
		}

		@Override
		public boolean hasContent() {
			return requestBody == null || "".equals(requestBody);
		}

		@Override
		public String getContent() {
			return requestBody;
		}

		@Override
		public byte[] getContentAsBytes() {
			return new byte[0];
		}

		public String getRequestPath() {
			return requestPath;
		}
	}
}
