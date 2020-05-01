package meilisearch;

import org.junit.Before;
import org.junit.Test;

public class DocumentsTest {

    Indexes index;

    @Before
    public void initialize() {
        MSClient ms = new MSClient(new Config("http://localhost:7700", ""));
        String testDocument = "[{\n" +
            "      \"id\": 287947,\n" +
            "      \"title\": \"Shazam\",\n" +
            "      \"poster\": \"https://image.tmdb.org/t/p/w1280/xnopI5Xtky18MPhK40cZAGAOVeV.jpg\",\n" +
            "      \"overview\": \"A boy is given the ability to become an adult superhero in times of need with a single magic word.\",\n" +
            "      \"release_date\": \"2019-03-23\"\n" +
            "  }]";

        try {
            // TODO: add uid of index for test
            this.index = ms.getIndex("4ayjgirl");
//            this.index.addDocument(testDocument);
        } catch (Exception e) {

        }
    }

    @Test
    public void get() throws Exception {
        // TODO: input uid, identifier for test
        System.out.println(this.index.getDocument(""));
    }

    @Test
    public void getAll() throws Exception {
        // TODO: input uid for test
        System.out.println(this.index.getDocuments());
    }

    @Test
    public void add() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }
}
