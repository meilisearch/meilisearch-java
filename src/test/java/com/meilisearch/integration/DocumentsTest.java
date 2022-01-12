package com.meilisearch.integration;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.gson.JsonObject;
import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.UpdateStatus;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;
import com.meilisearch.sdk.utils.Movie;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("integration")
public class DocumentsTest extends AbstractIT {
    @BeforeEach
    public void initialize() {
        this.setUp();
    }

    @AfterAll
    static void cleanMeiliSearch() {
        cleanup();
    }

    /** Test Add single document */
    @Test
    public void testAddDocumentsSingle() throws Exception {

        String indexUid = "AddDocumentsSingle";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        String singleDocument = this.gson.toJson(testData.getData().get(0));
        UpdateStatus updateInfo =
                this.gson.fromJson(
                        index.addDocuments("[" + singleDocument + "]"), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());
        Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);

        assertEquals(1, movies.length);
        assertEquals("419704", movies[0].getId());
        assertEquals("Ad Astra", movies[0].getTitle());
        assertEquals(
                "https://image.tmdb.org/t/p/original/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg",
                movies[0].getPoster());
        assertEquals(
                "The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown.",
                movies[0].getOverview());
        assertEquals("2019-09-17", movies[0].getRelease_date());
        assertEquals("en", movies[0].getLanguage());
        assertNotNull(movies[0].getGenres());
        assertEquals(2, movies[0].getGenres().length);
        assertEquals("Science Fiction", movies[0].getGenres()[0]);
        assertEquals("Drama", movies[0].getGenres()[1]);
    }

    /** Test add Documents with primaryKey */
    @Test
    public void testAddDocumentsWithSuppliedPrimaryKey() throws Exception {

        String indexUid = "TestAddWithPrimaryKey";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);

        // "language" is going to be our PK
        Movie firstMovie = testData.getData().get(0);
        Movie secondMovie = testData.getData().get(1);
        secondMovie.setLanguage(firstMovie.getLanguage());

        String firstDocument = this.gson.toJson(firstMovie);
        String secondDocument = this.gson.toJson(secondMovie);

        UpdateStatus firstUpdate =
                this.gson.fromJson(
                        index.addDocuments("[" + firstDocument + "]", "language"),
                        UpdateStatus.class);
        index.waitForPendingUpdate(firstUpdate.getUpdateId());

        Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        assertEquals(1, movies.length);
        assertEquals("419704", movies[0].getId());
        assertEquals("Ad Astra", movies[0].getTitle());

        UpdateStatus secondUpdate =
                this.gson.fromJson(
                        index.addDocuments("[" + secondDocument + "]", "language"),
                        UpdateStatus.class);
        index.waitForPendingUpdate(secondUpdate.getUpdateId());

        movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        assertEquals(1, movies.length);
        assertEquals("574982", movies[0].getId());
        assertEquals("The Blackout", movies[0].getTitle());
    }

    /** Test Add multiple documents */
    @Test
    public void testAddDocumentsMultiple() throws Exception {

        String indexUid = "AddDocumentsMultiple";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                this.gson.fromJson(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());
        Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        for (int i = 0; i < movies.length; i++) {
            Movie movie =
                    this.gson.fromJson(
                            index.getDocument(testData.getData().get(i).getId()), Movie.class);
            assertEquals(movie.getTitle(), testData.getData().get(i).getTitle());
        }
    }

    /** Test Add Documents in Batches With BatchSize */
    @Test
    public void testAddDocumentsInBatches() throws Exception {
        String indexUid = "AddDocumentsInBatches";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        String updateStatusArr = index.addDocumentsInBatches(testData.getRaw());

        UpdateStatus[] updateStatuses = gson.fromJson(updateStatusArr, UpdateStatus[].class);
        for (UpdateStatus updateStatus : updateStatuses) {
            index.waitForPendingUpdate(updateStatus.getUpdateId());
        }

        assertEquals("[{\"updateId\":0}]", updateStatusArr);
    }

    /** Test Add Documents in Batches With BatchSize */
    @Test
    public void testAddDocumentsInBatchesWithBatchSize() throws Exception {
        String indexUid = "AddDocumentsInBatchesWithBatchSize";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        String updateStatusArr = index.addDocumentsInBatches(testData.getRaw(), 15, null);

        UpdateStatus[] updateStatuses = gson.fromJson(updateStatusArr, UpdateStatus[].class);
        for (UpdateStatus updateStatus : updateStatuses) {
            index.waitForPendingUpdate(updateStatus.getUpdateId());
        }

        assertEquals("[{\"updateId\":0},{\"updateId\":1}]", updateStatusArr);
    }

    /** Test Update a document */
    @Test
    public void testUpdateDocument() throws Exception {

        String indexUid = "UpdateDocument";
        Index index = client.createIndex(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                this.gson.fromJson(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());
        Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        Movie toUpdate = movies[0];
        toUpdate.setTitle("The Perks of Being a Wallflower");
        toUpdate.setOverview("The best movie I've ever seen");

        updateInfo =
                this.gson.fromJson(
                        index.updateDocuments("[" + this.gson.toJson(toUpdate) + "]"),
                        UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());
        Movie responseUpdate = this.gson.fromJson(index.getDocument(toUpdate.getId()), Movie.class);

        assertEquals(toUpdate.getTitle(), responseUpdate.getTitle());
        assertEquals(toUpdate.getOverview(), responseUpdate.getOverview());
    }

    /** Test update Documents with primaryKey */
    @Test
    public void testUpdateDocumentsWithSuppliedPrimaryKey() throws Exception {

        String indexUid = "TestUpdateWithPrimaryKey";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);

        // "language" is going to be our PK
        Movie firstMovie = testData.getData().get(0);
        Movie secondMovie = testData.getData().get(1);
        secondMovie.setLanguage(firstMovie.getLanguage());

        String firstDocument = this.gson.toJson(firstMovie);
        JsonObject secondJson = this.gson.toJsonTree(secondMovie).getAsJsonObject();
        secondJson.remove("title");
        String secondDocument = this.gson.toJson(secondJson);

        UpdateStatus firstUpdate =
                this.gson.fromJson(
                        index.updateDocuments("[" + firstDocument + "]", "language"),
                        UpdateStatus.class);
        index.waitForPendingUpdate(firstUpdate.getUpdateId());

        Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        assertEquals(1, movies.length);
        assertEquals("419704", movies[0].getId());
        assertEquals("Ad Astra", movies[0].getTitle());

        UpdateStatus secondUpdate =
                this.gson.fromJson(
                        index.updateDocuments("[" + secondDocument + "]", "language"),
                        UpdateStatus.class);
        index.waitForPendingUpdate(secondUpdate.getUpdateId());

        movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        assertEquals(1, movies.length);
        assertEquals("574982", movies[0].getId()); // Second movie id
        assertEquals("Ad Astra", movies[0].getTitle()); // First movie title
    }

    /** Test Update multiple documents */
    @Test
    public void testUpdateDocuments() throws Exception {
        String indexUid = "UpdateDocuments";
        Index index = client.createIndex(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                this.gson.fromJson(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());
        Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        List<Movie> toUpdate = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            movies[i].setTitle("Star wars episode: " + i);
            movies[i].setOverview("This star wars movie is for the episode: " + i);
            toUpdate.add(movies[i]);
        }

        updateInfo =
                this.gson.fromJson(
                        index.updateDocuments(this.gson.toJson(toUpdate)), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());
        for (int j = 0; j < 5; j++) {
            Movie responseUpdate =
                    this.gson.fromJson(index.getDocument(toUpdate.get(j).getId()), Movie.class);
            assertEquals(toUpdate.get(j).getTitle(), responseUpdate.getTitle());
            assertEquals(toUpdate.get(j).getOverview(), responseUpdate.getOverview());
        }
    }

    /** Test Update Documents in Batches */
    @Test
    public void testUpdateDocumentsInBatchesWithBatchSize() throws Exception {

        String indexUid = "UpdateDocumentsInBatchesWithBatchSize";
        Index index = client.createIndex(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                this.gson.fromJson(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());
        Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        List<Movie> toUpdate = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            movies[i].setTitle("Star wars episode: " + i);
            movies[i].setOverview("This star wars movie is for the episode: " + i);
            toUpdate.add(movies[i]);
        }
        String updateStatusArr =
                index.updateDocumentsInBatches(this.gson.toJson(toUpdate), 2, null);

        UpdateStatus[] updateStatuses = gson.fromJson(updateStatusArr, UpdateStatus[].class);
        for (UpdateStatus updateStatus : updateStatuses) {
            index.waitForPendingUpdate(updateStatus.getUpdateId());
        }
        assertEquals("[{\"updateId\":1},{\"updateId\":2},{\"updateId\":3}]", updateStatusArr);
    }

    /** Test Update Documents in Batches */
    @Test
    public void testUpdateDocumentsInBatches() throws Exception {

        String indexUid = "UpdateDocumentsInBatches";
        Index index = client.createIndex(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                this.gson.fromJson(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());
        Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        List<Movie> toUpdate = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            movies[i].setTitle("Star wars episode: " + i);
            movies[i].setOverview("This star wars movie is for the episode: " + i);
            toUpdate.add(movies[i]);
        }
        String updateStatusArr = index.updateDocumentsInBatches(this.gson.toJson(toUpdate));

        UpdateStatus[] updateStatuses = gson.fromJson(updateStatusArr, UpdateStatus[].class);
        for (UpdateStatus updateStatus : updateStatuses) {
            index.waitForPendingUpdate(updateStatus.getUpdateId());
        }
        assertEquals("[{\"updateId\":1}]", updateStatusArr);
    }

    /** Test GetDocument */
    @Test
    public void testGetDocument() throws Exception {

        String indexUid = "GetDocument";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                this.gson.fromJson(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());
        Movie movie =
                this.gson.fromJson(
                        index.getDocument(testData.getData().get(0).getId()), Movie.class);
        assertEquals(movie.getTitle(), testData.getData().get(0).getTitle());
    }

    /** Test default GetDocuments */
    @Test
    public void testGetDocuments() throws Exception {

        String indexUid = "GetDocuments";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                this.gson.fromJson(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());
        Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        assertEquals(20, movies.length);
        for (int i = 0; i < movies.length; i++) {
            Movie movie =
                    this.gson.fromJson(
                            index.getDocument(testData.getData().get(i).getId()), Movie.class);
            assertEquals(movie.getTitle(), testData.getData().get(i).getTitle());
            String[] expectedGenres = testData.getData().get(i).getGenres();
            String[] foundGenres = movie.getGenres();
            for (int x = 0; x < expectedGenres.length; x++) {
                assertEquals(expectedGenres[x], foundGenres[x]);
            }
        }
    }

    /** Test GetDocuments with limit */
    @Test
    public void testGetDocumentsLimit() throws Exception {

        String indexUid = "GetDocumentsLimit";
        int limit = 24;
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                this.gson.fromJson(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());
        Movie[] movies = this.gson.fromJson(index.getDocuments(limit), Movie[].class);
        assertEquals(limit, movies.length);
        for (int i = 0; i < movies.length; i++) {
            Movie movie =
                    this.gson.fromJson(
                            index.getDocument(testData.getData().get(i).getId()), Movie.class);
            assertEquals(movie.getTitle(), testData.getData().get(i).getTitle());
        }
    }

    /** Test GetDocuments with limit and offset */
    @Test
    public void testGetDocumentsLimitAndOffset() throws Exception {
        String indexUid = "GetDocumentsLimit";
        int limit = 2;
        int offset = 2;
        int secondOffset = 5;
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                this.gson.fromJson(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());
        Movie[] movies = this.gson.fromJson(index.getDocuments(limit, offset), Movie[].class);
        Movie[] secondMovies =
                this.gson.fromJson(index.getDocuments(limit, secondOffset), Movie[].class);

        assertEquals(limit, movies.length);
        assertEquals(limit, secondMovies.length);

        assertNotEquals(movies[0].getTitle(), secondMovies[0].getTitle());
        assertNotEquals(movies[1].getTitle(), secondMovies[1].getTitle());
    }

    /** Test GetDocuments with limit, offset and specified attributesToRetrieve */
    @Test
    public void testGetDocumentsLimitAndOffsetAndSpecifiedAttributesToRetrieve() throws Exception {
        String indexUid = "GetDocumentsLimit";
        int limit = 2;
        int offset = 2;
        List<String> attributesToRetrieve = Arrays.asList("id", "title");
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                this.gson.fromJson(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());
        Movie[] movies =
                this.gson.fromJson(
                        index.getDocuments(limit, offset, attributesToRetrieve), Movie[].class);

        assertEquals(limit, movies.length);

        assertNotNull(movies[0].getId());
        assertNotNull(movies[0].getTitle());

        assertNull(movies[0].getGenres());
        assertNull(movies[0].getLanguage());
        assertNull(movies[0].getOverview());
        assertNull(movies[0].getPoster());
        assertNull(movies[0].getRelease_date());
    }

    /** Test GetDocuments with limit, offset and attributesToRetrieve */
    @ParameterizedTest
    @MethodSource("attributesToRetrieve")
    public void testGetDocumentsLimitAndOffsetAndAttributesToRetrieve(
            List<String> attributesToRetrieve) throws Exception {
        String indexUid = "GetDocumentsLimit";
        int limit = 2;
        int offset = 2;
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                this.gson.fromJson(index.addDocuments(testData.getRaw()), UpdateStatus.class);

        index.waitForPendingUpdate(updateInfo.getUpdateId());
        Movie[] movies =
                this.gson.fromJson(
                        index.getDocuments(limit, offset, attributesToRetrieve), Movie[].class);

        assertEquals(limit, movies.length);

        assertNotNull(movies[0].getId());
        assertNotNull(movies[0].getTitle());
        assertNotNull(movies[0].getGenres());
        assertNotNull(movies[0].getLanguage());
        assertNotNull(movies[0].getOverview());
        assertNotNull(movies[0].getPoster());
        assertNotNull(movies[0].getRelease_date());
    }

    private static Stream<List<Object>> attributesToRetrieve() {
        return Stream.of(emptyList(), null);
    }

    /** Test deleteDocument */
    @Test
    public void testDeleteDocument() throws Exception {

        String indexUid = "DeleteDocument";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                this.gson.fromJson(index.addDocuments(testData.getRaw()), UpdateStatus.class);
        index.waitForPendingUpdate(updateInfo.getUpdateId());

        Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        Movie toDelete = movies[0];
        updateInfo = this.gson.fromJson(index.deleteDocument(toDelete.getId()), UpdateStatus.class);
        index.waitForPendingUpdate(updateInfo.getUpdateId());

        assertThrows(MeiliSearchApiException.class, () -> index.getDocument(toDelete.getId()));
    }

    /** Test deleteDocuments */
    @Test
    public void testDeleteDocuments() throws Exception {

        String indexUid = "DeleteDocuments";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                this.gson.fromJson(index.addDocuments(testData.getRaw()), UpdateStatus.class);
        index.waitForPendingUpdate(updateInfo.getUpdateId());

        Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        assertEquals(20, movies.length);

        List<String> identifiersToDelete = getIdentifiersToDelete(movies);

        updateInfo =
                this.gson.fromJson(index.deleteDocuments(identifiersToDelete), UpdateStatus.class);
        index.waitForPendingUpdate(updateInfo.getUpdateId());

        movies = this.gson.fromJson(index.getDocuments(), Movie[].class);

        boolean containsDeletedMovie =
                Arrays.stream(movies)
                        .anyMatch(movie -> identifiersToDelete.contains(movie.getId()));

        assertFalse(containsDeletedMovie);
    }

    @NotNull
    private List<String> getIdentifiersToDelete(Movie[] movies) {
        return Arrays.asList(
                movies[1].getId(), movies[4].getId(), movies[10].getId(), movies[16].getId());
    }

    /** Test deleteAllDocuments */
    @Test
    public void testDeleteAllDocuments() throws Exception {
        String indexUid = "DeleteAllDocuments";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        UpdateStatus updateInfo =
                this.gson.fromJson(index.addDocuments(testData.getRaw()), UpdateStatus.class);
        index.waitForPendingUpdate(updateInfo.getUpdateId());

        Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        assertEquals(20, movies.length);

        updateInfo = this.gson.fromJson(index.deleteAllDocuments(), UpdateStatus.class);
        index.waitForPendingUpdate(updateInfo.getUpdateId());

        movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        assertEquals(0, movies.length);
    }

    /** Test add ndjson documents */
    @Test
    public void testAddDocumentsNDJSON() throws Exception {
        String indexUid = "testAddDocumentsNDJSON";
        Index index = client.index(indexUid);

        FileReader ndjsonReader = new FileReader(new File("src/test/resources/movies.ndjson"));
        BufferedReader ndjsonReader2 = new BufferedReader(ndjsonReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;

        while ((line = ndjsonReader2.readLine()) != null) {
            stringBuffer.append(line);
            stringBuffer.append("\n");
        }

        ndjsonReader.close();

        String updateStatus = index.addDocumentsNDJSON(stringBuffer.toString(), null);
        UpdateStatus updateInfo = gson.fromJson(updateStatus, UpdateStatus.class);
        index.waitForPendingUpdate(updateInfo.getUpdateId());
        assertEquals("{\"updateId\":0}", updateStatus);
    }

    /** Test add ndjson documents in batches */
    @Test
    public void testAddDocumentsNDJSONinBatches() throws Exception {
        String indexUid = "testAddDocumentsNDJSONinBatches";
        Index index = client.index(indexUid);

        FileReader ndjsonReader = new FileReader(new File("src/test/resources/movies.ndjson"));
        BufferedReader ndjsonReader2 = new BufferedReader(ndjsonReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;

        while ((line = ndjsonReader2.readLine()) != null) {
            stringBuffer.append(line);
            stringBuffer.append("\n");
        }

        ndjsonReader.close();

        String updateStatusArr = index.addDocumentsNDJSONinBatches(stringBuffer.toString());

        UpdateStatus[] updateStatuses = gson.fromJson(updateStatusArr, UpdateStatus[].class);
        for (UpdateStatus updateStatus : updateStatuses) {
            index.waitForPendingUpdate(updateStatus.getUpdateId());
        }
        assertEquals("[{\"updateId\":0}]", updateStatusArr);
    }

    /** Test add CSV documents */
    @Test
    public void testAddDocumentsCSV() throws Exception {

        String indexUid = "testAddDocumentsCSV";
        Index index = client.index(indexUid);

        FileReader csvReader = new FileReader(new File("src/test/resources/movies.csv"));
        BufferedReader csvReader2 = new BufferedReader(csvReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;

        while ((line = csvReader2.readLine()) != null) {
            stringBuffer.append(line);
            stringBuffer.append("\n");
        }

        csvReader.close();

        String updateStatus = index.addDocumentsCSV(stringBuffer.toString(), null);
        UpdateStatus updateInfo = gson.fromJson(updateStatus, UpdateStatus.class);
        index.waitForPendingUpdate(updateInfo.getUpdateId());
        assertEquals("{\"updateId\":0}", updateStatus);
    }

    /** Test add CSV documents */
    @Test
    public void testAddDocumentsCSVinBatches() throws Exception {

        String indexUid = "testAddDocumentsCSVinBatches";
        Index index = client.index(indexUid);

        FileReader csvReader = new FileReader(new File("src/test/resources/movies.csv"));
        BufferedReader csvReader2 = new BufferedReader(csvReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;

        while ((line = csvReader2.readLine()) != null) {
            stringBuffer.append(line);
            stringBuffer.append("\n");
        }

        csvReader.close();

        String updateStatusArr = index.addDocumentsCSVinBatches(stringBuffer.toString());

        UpdateStatus[] updateStatuses = gson.fromJson(updateStatusArr, UpdateStatus[].class);
        for (UpdateStatus updateStatus : updateStatuses) {
            index.waitForPendingUpdate(updateStatus.getUpdateId());
        }
        assertEquals("[{\"updateId\":0}]", updateStatusArr);
    }

    /** Update Documents from NDJSON file */
    @Test
    public void testUpdateDocumentsNDJSON() throws Exception {
        String indexUid = "testUpdateDocumentsNDJSON";
        Index index = client.index(indexUid);

        FileReader ndjsonReader = new FileReader(new File("src/test/resources/movies.ndjson"));
        BufferedReader ndjsonReader2 = new BufferedReader(ndjsonReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;

        while ((line = ndjsonReader2.readLine()) != null) {
            stringBuffer.append(line);
            stringBuffer.append("\n");
        }

        ndjsonReader.close();

        String updateStatus = index.addDocumentsNDJSON(stringBuffer.toString(), null);
        UpdateStatus updateInfo = gson.fromJson(updateStatus, UpdateStatus.class);
        index.waitForPendingUpdate(updateInfo.getUpdateId());

        Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        List<Movie> toUpdate = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            movies[i].setTitle("Star wars episode: " + i);
            movies[i].setOverview("This star wars movie is for the episode: " + i);
            toUpdate.add(movies[i]);
        }
        StringBuffer updateNDJSONData = new StringBuffer();
        for (int i = 0; i < toUpdate.size(); i++) {
            updateNDJSONData.append(gson.toJson(toUpdate.get(i)));
            updateNDJSONData.append("\n");
        }

        updateStatus = index.updateDocumentsNDJSON(updateNDJSONData.toString(), null);
        updateInfo = this.gson.fromJson(updateStatus, UpdateStatus.class);
        index.waitForPendingUpdate(updateInfo.getUpdateId());

        assertEquals("{\"updateId\":1}", updateStatus);

        for (int j = 0; j < 5; j++) {
            Movie responseUpdate =
                    this.gson.fromJson(index.getDocument(toUpdate.get(j).getId()), Movie.class);
            assertEquals(toUpdate.get(j).getTitle(), responseUpdate.getTitle());
            assertEquals(toUpdate.get(j).getOverview(), responseUpdate.getOverview());
        }
    }

    /** Update Documents from CSV file */
    @Test
    public void testUpdateDocumentsCSV() throws Exception {
        String indexUid = "testUpdateDocumentsCSV";
        Index index = client.index(indexUid);

        FileReader csvReader = new FileReader(new File("src/test/resources/movies.csv"));
        BufferedReader csvReader2 = new BufferedReader(csvReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;

        while ((line = csvReader2.readLine()) != null) {
            stringBuffer.append(line);
            stringBuffer.append("\n");
        }

        csvReader.close();

        String updateStatus = index.addDocumentsCSV(stringBuffer.toString(), null);
        UpdateStatus updateInfo = gson.fromJson(updateStatus, UpdateStatus.class);
        index.waitForPendingUpdate(updateInfo.getUpdateId());

        Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        List<Movie> toUpdate = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            movies[i].setTitle("Star wars episode: " + i);
            movies[i].setOverview("This star wars movie is for the episode: " + i);
            toUpdate.add(movies[i]);
        }

        JsonNode jsonTree = new ObjectMapper().readTree(this.gson.toJson(toUpdate));

        CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
        JsonNode firstObject = jsonTree.elements().next();
        firstObject.fieldNames().forEachRemaining(csvSchemaBuilder::addColumn);
        CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();

        CsvMapper csvMapper = new CsvMapper();
        String updatedCSVData =
                csvMapper.writerFor(JsonNode.class).with(csvSchema).writeValueAsString(jsonTree);

        updateStatus = index.updateDocumentsCSV(updatedCSVData, null);
        updateInfo = this.gson.fromJson(updateStatus, UpdateStatus.class);
        index.waitForPendingUpdate(updateInfo.getUpdateId());

        assertEquals("{\"updateId\":1}", updateStatus);

        for (int j = 0; j < 5; j++) {
            Movie responseUpdate =
                    this.gson.fromJson(index.getDocument(toUpdate.get(j).getId()), Movie.class);
            assertEquals(toUpdate.get(j).getTitle(), responseUpdate.getTitle());
            assertEquals(toUpdate.get(j).getOverview(), responseUpdate.getOverview());
        }
    }

    /** Update Documents in Batches from NDJSON files */
    @Test
    public void updateDocumentsNDJSONinBatches() throws Exception {
        String indexUid = "testUpdateDocumentsNDJSONinBatches";
        Index index = client.index(indexUid);

        FileReader ndjsonReader = new FileReader(new File("src/test/resources/movies.ndjson"));
        BufferedReader ndjsonReader2 = new BufferedReader(ndjsonReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;

        while ((line = ndjsonReader2.readLine()) != null) {
            stringBuffer.append(line);
            stringBuffer.append("\n");
        }

        ndjsonReader.close();

        String updateStatus = index.addDocumentsNDJSON(stringBuffer.toString(), null);
        UpdateStatus updateInfo = gson.fromJson(updateStatus, UpdateStatus.class);
        index.waitForPendingUpdate(updateInfo.getUpdateId());

        Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        List<Movie> toUpdate = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            movies[i].setTitle("Star wars episode: " + i);
            movies[i].setOverview("This star wars movie is for the episode: " + i);
            toUpdate.add(movies[i]);
        }
        StringBuffer updateNDJSONData = new StringBuffer();
        for (int i = 0; i < toUpdate.size(); i++) {
            updateNDJSONData.append(gson.toJson(toUpdate.get(i)));
            updateNDJSONData.append("\n");
        }

        String updateStatusArr =
                index.updateDocumentsNDJSONinBatches(updateNDJSONData.toString(), null);

        UpdateStatus[] updateStatuses = gson.fromJson(updateStatusArr, UpdateStatus[].class);
        for (UpdateStatus updateStatus1 : updateStatuses) {
            index.waitForPendingUpdate(updateStatus1.getUpdateId());
        }
        assertEquals("[{\"updateId\":1}]", updateStatusArr);
    }

    /** Update Documents in Batches from CSV files */
    @Test
    public void updateDocumentsCSVinBatches() throws Exception {
        String indexUid = "testUpdateDocumentsCSVinBatches";
        Index index = client.index(indexUid);

        FileReader csvReader = new FileReader(new File("src/test/resources/movies.csv"));
        BufferedReader csvReader2 = new BufferedReader(csvReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;

        while ((line = csvReader2.readLine()) != null) {
            stringBuffer.append(line);
            stringBuffer.append("\n");
        }

        csvReader.close();

        String updateStatus = index.addDocumentsCSV(stringBuffer.toString(), null);
        UpdateStatus updateInfo = gson.fromJson(updateStatus, UpdateStatus.class);
        index.waitForPendingUpdate(updateInfo.getUpdateId());

        Movie[] movies = this.gson.fromJson(index.getDocuments(), Movie[].class);
        List<Movie> toUpdate = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            movies[i].setTitle("Star wars episode: " + i);
            movies[i].setOverview("This star wars movie is for the episode: " + i);
            toUpdate.add(movies[i]);
        }

        JsonNode jsonTree = new ObjectMapper().readTree(this.gson.toJson(toUpdate));

        CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
        JsonNode firstObject = jsonTree.elements().next();
        firstObject.fieldNames().forEachRemaining(csvSchemaBuilder::addColumn);
        CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();

        CsvMapper csvMapper = new CsvMapper();
        String updatedCSVData =
                csvMapper.writerFor(JsonNode.class).with(csvSchema).writeValueAsString(jsonTree);

        String updateStatusArr = index.updateDocumentsCSVinBatches(updatedCSVData, null);

        UpdateStatus[] updateStatuses = gson.fromJson(updateStatusArr, UpdateStatus[].class);
        for (UpdateStatus updateStatus1 : updateStatuses) {
            index.waitForPendingUpdate(updateStatus1.getUpdateId());
        }
        assertEquals("[{\"updateId\":1}]", updateStatusArr);
    }
}
