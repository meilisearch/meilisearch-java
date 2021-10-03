package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonObject;
import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.UpdateStatus;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;
import com.meilisearch.sdk.utils.Movie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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
}
