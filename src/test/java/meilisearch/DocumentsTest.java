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
            this.index = ms.getIndex("");
        } catch (Exception e) {

        }
    }

    @Test
    public void getDocument() throws Exception {
        // TODO: input uid, identifier for test
        System.out.println(this.index.getDocument(""));
    }

    @Test
    public void getDocuments() throws Exception {
        // TODO: input uid for test
        System.out.println(this.index.getDocuments());
    }
}
