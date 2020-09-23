package meilisearch;

import org.junit.Before;
import org.junit.Test;

public class DocumentsTest {

	MeiliSearchIndex meilisearchIndex;

	@Before
	public void initialize() {
		Client ms = new Client(new Config("http://localhost:7700", ""));

		try {
			// TODO: add uid of index for test
			this.meilisearchIndex = ms.getIndex("movies");
		} catch (Exception e) {

		}
	}

	@Test
	public void get() throws Exception {
		// TODO: input identifier for test
		System.out.println(this.meilisearchIndex.getDocument("9999"));
	}

	@Test
	public void getAll() throws Exception {
		System.out.println(this.meilisearchIndex.getDocuments());
	}

	@Test
	public void add() throws Exception {
		String testDoc = "[{\n" +
			"      \"id\": 9999,\n" +
			"      \"title\": \"Shazam\",\n" +
			"      \"poster\": \"https://image.tmdb.org/t/p/w1280/xnopI5Xtky18MPhK40cZAGAOVeV.jpg\",\n" +
			"      \"overview\": \"A boy is given the ability to become an adult superhero in times of need with a single magic word.\",\n" +
			"      \"release_date\": \"2019-03-23\"\n" +
			"  }]";
		// TODO: setup test document for 'add'
		System.out.println(this.meilisearchIndex.addDocument(""));
	}

	@Test
	public void delete() throws Exception {
		// TODO: input identifier for test
		System.out.println(this.meilisearchIndex.deleteDocument(""));
	}

	@Test
	public void search() throws Exception {
		System.out.println(this.meilisearchIndex.search("Batman"));
	}

	@Test
	public void updates() throws Exception {
		System.out.println(this.meilisearchIndex.getUpdates()[0].toString());
	}
}
