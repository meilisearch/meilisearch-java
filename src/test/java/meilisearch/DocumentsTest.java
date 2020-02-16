package meilisearch;

import org.junit.Before;
import org.junit.Test;

public class DocumentsTest {

    MSClient ms;

    @Before
    public void initialize() {
        ms = new MSClient(new Config("http://localhost:7700", ""));
    }

    @Test
    public void getDocument() throws Exception {
        // TODO: input uid, identifier for test
        System.out.println(ms.getDocument("", ""));
    }

    @Test
    public void getDocuments() throws Exception {
        // TODO: input uid for test
        System.out.println(ms.getDocuments(""));
    }
}
