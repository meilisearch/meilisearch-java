package meilisearch;

import org.junit.*;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocumentsTest {
	final static String indexUid = "__TEST_MOVIES";
	static MeilisearchIndex meilisearchIndex;
	static final MeilisearchClient ms = new MeilisearchClient(new MeilisearchConfig("http://localhost:7700", ""));

	private static final String testDocument = "{" +
		"\"id\":9999," +
		"\"title\":\"Shazam\"," +
		"\"poster\":\"https://image.tmdb.org/t/p/w1280/xnopI5Xtky18MPhK40cZAGAOVeV.jpg\"," +
		"\"overview\":\"A boy is given the ability to become an adult superhero in times of need with a single magic word.\"," +
		"\"release_date\":\"2019-03-23\"" +
		"}";

	@BeforeClass
	public static void initialize() throws Exception {
		DocumentsTest.ms.createIndex(DocumentsTest.indexUid);
		DocumentsTest.meilisearchIndex = DocumentsTest.ms.getIndex(DocumentsTest.indexUid);
		assertNotNull(meilisearchIndex);
	}

	@AfterClass
	public static void cleanup() throws Exception {
		DocumentsTest.ms.deleteIndex(DocumentsTest.indexUid);
	}

	@Test(expected = FileNotFoundException.class)
	public void test1_shouldGetNoneAtFirst() throws Exception {
		// Expect no documents in the index
		assertEquals("[]", DocumentsTest.meilisearchIndex.getDocuments());

		// Expect 404 NOT FOUND HTTP error
		DocumentsTest.meilisearchIndex.getDocument("9999");
	}

	@Test
	public void test2_shouldAllowInsertion() throws Exception {
		final String docArr = "[" + testDocument + "]";
		DocumentsTest.meilisearchIndex.addDocument(docArr);
	}

	@Test(timeout = 100L)
	public void test3_shouldGetNewlyInsertedDocument() throws Exception {
		final String docs = DocumentsTest.meilisearchIndex.getDocuments();
		assertEquals("[" + DocumentsTest.testDocument + "]", docs);
		assertEquals(testDocument, DocumentsTest.meilisearchIndex.getDocument("9999"));
	}

	@Test
	public void test4_validSearchShouldReturnTestDocument() throws Exception {
		assertTrue(DocumentsTest.meilisearchIndex.search("superhero").contains(testDocument));
	}

	@Test
	public void test5_invalidSearchShouldNotReturnDocuments() throws Exception {
		assertTrue(DocumentsTest.meilisearchIndex.search("Batman").contains("\"hits\":[]"));
	}

	@Test
	public void test6_shouldAllowDocumentDeletion() throws Exception {
		DocumentsTest.meilisearchIndex.deleteDocument("");

		// Expect no documents in the index
		assertEquals("[]", DocumentsTest.meilisearchIndex.getDocuments());
	}
}
