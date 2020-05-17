package meilisearch;

import org.junit.Before;
import org.junit.Test;

public class DocumentsTest {

    Indexes index;

    @Before
    public void initialize() {
        MSClient ms = new MSClient(new Config("http://localhost:7700", ""));

        try {
            // TODO: add uid of index for test
            this.index = ms.getIndex("movies");
        } catch (Exception e) {

        }
    }

    @Test
    public void get() throws Exception {
        // TODO: input identifier for test
        System.out.println(this.index.getDocument("9999"));
    }

    @Test
    public void getAll() throws Exception {
        System.out.println(this.index.getDocuments());
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
        System.out.println(this.index.addDocument(""));
    }

    @Test
    public void delete() throws Exception {
        // TODO: input identifier for test
        System.out.println(this.index.deleteDocument(""));
    }

    @Test
    public void search() throws Exception {
        System.out.println(this.index.search("Batman"));
    }
}
