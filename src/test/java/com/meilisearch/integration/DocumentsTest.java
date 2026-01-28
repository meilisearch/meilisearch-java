package com.meilisearch.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonObject;
import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.exceptions.MeilisearchApiException;
import com.meilisearch.sdk.model.DocumentsQuery;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.TaskInfo;
import com.meilisearch.sdk.utils.Movie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
public class DocumentsTest extends AbstractIT {

    @AfterAll
    static void cleanMeilisearch() {
        cleanup();
    }

    @BeforeEach
    public void initialize() {
        this.setUp();
        this.setUpJacksonClient();
    }

    /** Test Add single document */
    @Test
    public void testAddDocumentsSingle() throws Exception {

        String indexUid = "AddDocumentsSingle";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        String singleDocument = this.gson.toJson(testData.getData().get(0));
        TaskInfo task = index.addDocuments("[" + singleDocument + "]");

        index.waitForTask(task.getTaskUid());
        Results<Movie> result = index.getDocuments(Movie.class);
        Movie[] movies = result.getResults();

        String expectedOverview =
                "The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown.";
        assertThat(movies, is(arrayWithSize(1)));
        assertThat(movies[0].getId(), is(equalTo("419704")));
        assertThat(movies[0].getTitle(), is(equalTo("Ad Astra")));
        assertThat(
                movies[0].getPoster(),
                is(equalTo("https://image.tmdb.org/t/p/original/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg")));
        assertThat(movies[0].getOverview(), is(equalTo(expectedOverview)));
        assertThat(movies[0].getRelease_date(), is(equalTo("2019-09-17")));
        assertThat(movies[0].getLanguage(), is(equalTo("en")));
        assertThat(movies[0].getGenres(), is(notNullValue()));
        assertThat(movies[0].getGenres(), is(arrayWithSize(2)));
        assertThat(movies[0].getGenres()[0], is(equalTo("Science Fiction")));
        assertThat(movies[0].getGenres()[1], is(equalTo("Drama")));
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

        TaskInfo firstTask = index.addDocuments("[" + firstDocument + "]", "language");
        index.waitForTask(firstTask.getTaskUid());

        Results<Movie> result = index.getDocuments(Movie.class);
        Movie[] movies = result.getResults();
        assertThat(movies, is(arrayWithSize(1)));
        assertThat(movies[0].getId(), is(equalTo("419704")));
        assertThat(movies[0].getTitle(), is(equalTo("Ad Astra")));

        TaskInfo secondTask = index.addDocuments("[" + secondDocument + "]", "language");
        index.waitForTask(secondTask.getTaskUid());

        movies = index.getDocuments(Movie.class).getResults();
        assertThat(movies, is(arrayWithSize(1)));
        assertThat(movies[0].getId(), is(equalTo("574982")));
        assertThat(movies[0].getTitle(), is(equalTo("The Blackout")));
    }

    /** Test Add multiple documents */
    @Test
    public void testAddDocumentsMultiple() throws Exception {

        String indexUid = "AddDocumentsMultiple";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        Results<Movie> result = index.getDocuments(Movie.class);
        Movie[] movies = result.getResults();
        for (int i = 0; i < movies.length; i++) {
            Movie movie = index.getDocument(testData.getData().get(i).getId(), Movie.class);
            assertThat(movie.getTitle(), is(equalTo(testData.getData().get(i).getTitle())));
        }
    }

    /** Test Add multiple documents with Jackson Json Handler */
    @Test
    public void testAddDocumentsMultipleWithJacksonJsonHandler() throws Exception {

        String indexUid = "AddDocumentsMultipleWithJacksonJsonHandler";
        Index index = clientJackson.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        Results<Movie> result = index.getDocuments(Movie.class);
        Movie[] movies = result.getResults();
        for (int i = 0; i < movies.length; i++) {
            Movie movie = index.getDocument(testData.getData().get(i).getId(), Movie.class);
            assertThat(movie.getTitle(), is(equalTo(testData.getData().get(i).getTitle())));
        }
    }

    /** Test Add Documents in Batches With BatchSize */
    @Test
    public void testAddDocumentsInBatches() throws Exception {
        String indexUid = "AddDocumentsInBatches";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo[] taskArr = index.addDocumentsInBatches(testData.getRaw());

        for (TaskInfo task : taskArr) {
            index.waitForTask(task.getTaskUid());

            assertThat(task, is(instanceOf(TaskInfo.class)));
            assertThat(task.getType(), is(equalTo("documentAdditionOrUpdate")));
            assertThat(task.getEnqueuedAt(), is(notNullValue()));
        }
    }

    /** Test Add Documents in Batches With BatchSize */
    @Test
    public void testAddDocumentsInBatchesWithBatchSize() throws Exception {
        String indexUid = "AddDocumentsInBatchesWithBatchSize";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo[] taskArr = index.addDocumentsInBatches(testData.getRaw(), 15, null);

        for (TaskInfo task : taskArr) {
            index.waitForTask(task.getTaskUid());

            assertThat(task, is(instanceOf(TaskInfo.class)));
            assertThat(task.getType(), is(equalTo("documentAdditionOrUpdate")));
            assertThat(task.getEnqueuedAt(), is(notNullValue()));
        }
    }

    /** Test Update a document */
    @Test
    public void testUpdateDocument() throws Exception {

        String indexUid = "UpdateDocument";
        Index index = createEmptyIndex(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        Results<Movie> result = index.getDocuments(Movie.class);
        Movie[] movies = result.getResults();
        Movie toUpdate = movies[0];
        toUpdate.setTitle("The Perks of Being a Wallflower");
        toUpdate.setOverview("The best movie I've ever seen");

        task = index.updateDocuments("[" + this.gson.toJson(toUpdate) + "]");

        index.waitForTask(task.getTaskUid());
        Movie responseUpdate = index.getDocument(toUpdate.getId(), Movie.class);

        assertThat(responseUpdate.getTitle(), is(equalTo(toUpdate.getTitle())));
        assertThat(responseUpdate.getOverview(), is(equalTo(toUpdate.getOverview())));
    }

    /** Test Update Documents with primaryKey */
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

        TaskInfo firstTask = index.updateDocuments("[" + firstDocument + "]", "language");
        index.waitForTask(firstTask.getTaskUid());

        Results<Movie> result = index.getDocuments(Movie.class);
        Movie[] movies = result.getResults();
        assertThat(movies, is(arrayWithSize(1)));
        assertThat(movies[0].getId(), is(equalTo("419704")));
        assertThat(movies[0].getTitle(), is(equalTo("Ad Astra")));

        TaskInfo secondTask = index.updateDocuments("[" + secondDocument + "]", "language");
        index.waitForTask(secondTask.getTaskUid());

        movies = index.getDocuments(Movie.class).getResults();
        assertThat(movies, is(arrayWithSize(1)));
        assertThat(movies[0].getId(), is(equalTo("574982"))); // Second movie id
        assertThat(movies[0].getTitle(), is(equalTo("Ad Astra"))); // First movie title
    }

    /** Test Update multiple documents */
    @Test
    public void testUpdateDocuments() throws Exception {
        String indexUid = "UpdateDocuments";
        Index index = createEmptyIndex(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        Results<Movie> result = index.getDocuments(Movie.class);
        Movie[] movies = result.getResults();
        List<Movie> toUpdate = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            movies[i].setTitle("Star wars episode: " + i);
            movies[i].setOverview("This star wars movie is for the episode: " + i);
            toUpdate.add(movies[i]);
        }

        task = index.updateDocuments(this.gson.toJson(toUpdate));

        index.waitForTask(task.getTaskUid());
        for (int j = 0; j < 5; j++) {
            Movie responseUpdate = index.getDocument(toUpdate.get(j).getId(), Movie.class);
            assertThat(responseUpdate.getTitle(), is(equalTo(toUpdate.get(j).getTitle())));
            assertThat(responseUpdate.getOverview(), is(equalTo(toUpdate.get(j).getOverview())));
        }
    }

    /** Test Update Documents in Batches */
    @Test
    public void testUpdateDocumentsInBatchesWithBatchSize() throws Exception {

        String indexUid = "UpdateDocumentsInBatchesWithBatchSize";
        Index index = createEmptyIndex(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo taskIndex = index.addDocuments(testData.getRaw());

        index.waitForTask(taskIndex.getTaskUid());
        Results<Movie> result = index.getDocuments(Movie.class);
        Movie[] movies = result.getResults();
        List<Movie> toUpdate = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            movies[i].setTitle("Star wars episode: " + i);
            movies[i].setOverview("This star wars movie is for the episode: " + i);
            toUpdate.add(movies[i]);
        }
        TaskInfo[] taskArr = index.updateDocumentsInBatches(this.gson.toJson(toUpdate), 2, null);

        for (TaskInfo task : taskArr) {
            index.waitForTask(task.getTaskUid());
            assertThat(task.getType(), is(equalTo("documentAdditionOrUpdate")));
            assertThat(task.getEnqueuedAt(), is(notNullValue()));
        }
    }

    /** Test Update Documents in Batches */
    @Test
    public void testUpdateDocumentsInBatches() throws Exception {

        String indexUid = "UpdateDocumentsInBatches";
        Index index = createEmptyIndex(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo taskIndex = index.addDocuments(testData.getRaw());

        index.waitForTask(taskIndex.getTaskUid());
        Results<Movie> result = index.getDocuments(Movie.class);
        Movie[] movies = result.getResults();
        List<Movie> toUpdate = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            movies[i].setTitle("Star wars episode: " + i);
            movies[i].setOverview("This star wars movie is for the episode: " + i);
            toUpdate.add(movies[i]);
        }
        TaskInfo[] taskArr = index.updateDocumentsInBatches(this.gson.toJson(toUpdate));

        for (TaskInfo task : taskArr) {
            index.waitForTask(task.getTaskUid());
            assertThat(task.getType(), is(equalTo("documentAdditionOrUpdate")));
            assertThat(task.getEnqueuedAt(), is(notNullValue()));
        }
    }

    /** Test GetDocument */
    @Test
    public void testGetDocument() throws Exception {

        String indexUid = "GetDocument";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        Movie movie = index.getDocument(testData.getData().get(0).getId(), Movie.class);
        assertThat(movie.getTitle(), is(equalTo(testData.getData().get(0).getTitle())));
    }

    /** Test default GetRawDocuments */
    @Test
    public void testGetRawDocument() throws Exception {

        String indexUid = "GetRawDocument";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        Movie movie =
                this.gson.fromJson(
                        index.getRawDocument(testData.getData().get(0).getId()), Movie.class);
        assertThat(movie.getTitle(), is(equalTo(testData.getData().get(0).getTitle())));
    }

    /** Test default GetDocuments */
    @Test
    public void testGetDocuments() throws Exception {

        String indexUid = "GetDocuments";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        Results<Movie> result = index.getDocuments(Movie.class);
        Movie[] movies = result.getResults();

        assertThat(movies, is(arrayWithSize(20)));
        for (int i = 0; i < movies.length; i++) {
            assertThat(movies[i].getTitle(), is(equalTo(testData.getData().get(i).getTitle())));
            String[] expectedGenres = testData.getData().get(i).getGenres();
            String[] foundGenres = movies[i].getGenres();
            for (int x = 0; x < expectedGenres.length; x++) {
                assertThat(expectedGenres[x], is(equalTo(foundGenres[x])));
            }
        }
        for (int i = 0; i < movies.length; i++) {
            Movie movie = index.getDocument(testData.getData().get(i).getId(), Movie.class);
            assertThat(movie.getTitle(), is(equalTo(testData.getData().get(i).getTitle())));
            String[] expectedGenres = testData.getData().get(i).getGenres();
            String[] foundGenres = movie.getGenres();
            for (int x = 0; x < expectedGenres.length; x++) {
                assertThat(expectedGenres[x], is(equalTo(foundGenres[x])));
            }
        }
    }

    /** Test GetDocuments with limit */
    @Test
    public void testGetDocumentsLimit() throws Exception {

        String indexUid = "GetDocumentsLimit";
        int limit = 24;
        DocumentsQuery query = new DocumentsQuery().setLimit(limit);
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        Results<Movie> result = index.getDocuments(query, Movie.class);
        Movie[] movies = result.getResults();
        assertThat(movies, is(arrayWithSize(limit)));
        for (int i = 0; i < movies.length; i++) {
            Movie movie = index.getDocument(testData.getData().get(i).getId(), Movie.class);
            assertThat(movie.getTitle(), is(equalTo(testData.getData().get(i).getTitle())));
        }
    }

    /** Test GetDocuments with limit and offset */
    @Test
    public void testGetDocumentsLimitAndOffset() throws Exception {
        String indexUid = "GetDocumentsLimitAndOffset";
        int limit = 2;
        int offset = 2;
        int secondOffset = 5;
        DocumentsQuery query = new DocumentsQuery().setLimit(limit).setOffset(offset);
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        Results<Movie> result = index.getDocuments(query, Movie.class);
        Movie[] movies = result.getResults();
        Results<Movie> secondResults =
                index.getDocuments(query.setOffset(secondOffset), Movie.class);
        Movie[] secondMovies = secondResults.getResults();

        assertThat(movies, is(arrayWithSize(limit)));
        assertThat(secondMovies, is(arrayWithSize(limit)));
        assertThat(secondMovies[0].getTitle(), is(not(equalTo(movies[0].getTitle()))));
        assertThat(secondMovies[1].getTitle(), is(not(equalTo(movies[1].getTitle()))));
    }

    /** Test GetDocuments with limit, offset and specified fields */
    @Test
    public void testGetDocumentsLimitAndOffsetAndSpecifiedFields() throws Exception {
        String indexUid = "GetDocumentsLimitAndOffsetAndSpecifiedFields";
        int limit = 2;
        int offset = 2;
        List<String> fields = Arrays.asList("id", "title");
        DocumentsQuery query =
                new DocumentsQuery()
                        .setLimit(limit)
                        .setOffset(offset)
                        .setFields(fields.toArray(new String[0]));
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        Results<Movie> result = index.getDocuments(query, Movie.class);
        Movie[] movies = result.getResults();

        assertThat(movies, is(arrayWithSize(limit)));
        assertThat(movies[0].getId(), is(notNullValue()));
        assertThat(movies[0].getTitle(), is(notNullValue()));
        assertThat(movies[0].getGenres(), is(nullValue()));
        assertThat(movies[0].getLanguage(), is(nullValue()));
        assertThat(movies[0].getOverview(), is(nullValue()));
        assertThat(movies[0].getPoster(), is(nullValue()));
        assertThat(movies[0].getRelease_date(), is(nullValue()));
    }

    /** Test GetDocuments with limit, offset, specified fields and specified filter */
    @Test
    void testGetDocumentsLimitAndOffsetAndSpecifiedFieldsAndSpecifiedFilter() throws Exception {
        String indexUid = "GetDocumentsLimitAndOffsetAndSpecifiedFieldsAndSpecifiedFilter";
        int limit = 2;
        int offset = 0;
        List<String> fields = Arrays.asList("id", "title", "genres");
        List<String> filters = Arrays.asList("(genres = Horror OR genres = Action)");

        DocumentsQuery query =
                new DocumentsQuery()
                        .setLimit(limit)
                        .setOffset(offset)
                        .setFields(fields.toArray(new String[0]))
                        .setFilter(filters.toArray(new String[0]));
        Index index = client.index(indexUid);

        String[] filterAttributes = {"genres"};
        index.waitForTask(index.updateFilterableAttributesSettings(filterAttributes).getTaskUid());

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        Results<Movie> result = index.getDocuments(query, Movie.class);
        Movie[] movies = result.getResults();

        assertThat(movies, is(arrayWithSize(limit)));
        assertThat(movies[0].getId(), is(notNullValue()));
        assertThat(movies[0].getTitle(), is(notNullValue()));
        assertThat(movies[0].getGenres(), is(notNullValue()));
        assertThat(
                movies[0].getGenres(),
                hasItemInArray(Matchers.anyOf(equalTo("Horror"), equalTo("Action"))));
        assertThat(movies[0].getLanguage(), is(nullValue()));
        assertThat(movies[0].getOverview(), is(nullValue()));
        assertThat(movies[0].getPoster(), is(nullValue()));
        assertThat(movies[0].getRelease_date(), is(nullValue()));
    }

    /** Test default GetRawDocuments */
    @Test
    public void testGetRawDocuments() throws Exception {
        String indexUid = "GetRawDocuments";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        String results = index.getRawDocuments();

        assertThat(results.contains("results"), is(equalTo(true)));
        assertThat(results.contains(testData.getData().get(0).getId()), is(equalTo(true)));
        assertThat(results.contains(testData.getData().get(0).getTitle()), is(equalTo(true)));
        assertThat(results.contains(testData.getData().get(0).getGenres()[0]), is(equalTo(true)));
        assertThat(results.contains(testData.getData().get(0).getLanguage()), is(equalTo(true)));
        assertThat(results.contains(testData.getData().get(0).getOverview()), is(equalTo(true)));
        assertThat(results.contains(testData.getData().get(0).getPoster()), is(equalTo(true)));
        assertThat(
                results.contains(testData.getData().get(0).getRelease_date()), is(equalTo(true)));
    }

    /** Test GetRawDocuments with limit */
    @Test
    public void testGetRawDocumentsLimit() throws Exception {

        String indexUid = "GetRawDocumentsLimit";
        int limit = 24;
        DocumentsQuery query = new DocumentsQuery().setLimit(limit);
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        String results = index.getRawDocuments(query);

        assertThat(results.contains("results"), is(equalTo(true)));
        assertThat(results.contains("\"limit\":24"), is(equalTo(true)));
    }

    /** Test GetRawDocuments with limit and offset */
    @Test
    public void testGetRawDocumentsLimitAndOffset() throws Exception {
        String indexUid = "GetRawDocumentsLimitAndOffset";
        int limit = 2;
        int offset = 2;
        DocumentsQuery query = new DocumentsQuery().setLimit(limit).setOffset(offset);
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        String results = index.getRawDocuments(query);

        assertThat(results.contains("results"), is(equalTo(true)));
        assertThat(results.contains("\"limit\":2"), is(equalTo(true)));
        assertThat(results.contains("\"offset\":2"), is(equalTo(true)));
    }

    /** Test GetRawDocuments with limit, offset and specified fields */
    @Test
    public void testGetRawDocumentsLimitAndOffsetAndSpecifiedFields() throws Exception {
        String indexUid = "GetRawDocumentsLimitAndOffsetAndSpecifiedFields";
        int limit = 2;
        int offset = 2;
        List<String> fields = Arrays.asList("id", "title");
        DocumentsQuery query =
                new DocumentsQuery()
                        .setLimit(limit)
                        .setOffset(offset)
                        .setFields(fields.toArray(new String[0]));
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        index.waitForTask(task.getTaskUid());
        String results = index.getRawDocuments(query);

        assertThat(results.contains("results"), is(equalTo(true)));
        assertThat(results.contains("\"limit\":2"), is(equalTo(true)));
        assertThat(results.contains("\"offset\":2"), is(equalTo(true)));
        assertThat(results.contains("id"), is(equalTo(true)));
        assertThat(results.contains("title"), is(equalTo(true)));
        assertThat(results.contains("genres"), is(equalTo(false)));
        assertThat(results.contains("langage"), is(equalTo(false)));
        assertThat(results.contains("poster"), is(equalTo(false)));
        assertThat(results.contains("release_date"), is(equalTo(false)));
    }

    /** Test deleteDocument */
    @Test
    public void testDeleteDocument() throws Exception {

        String indexUid = "DeleteDocument";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());
        index.waitForTask(task.getTaskUid());

        Results<Movie> result = index.getDocuments(Movie.class);
        Movie[] movies = result.getResults();
        Movie toDelete = movies[0];
        task = index.deleteDocument(toDelete.getId());
        index.waitForTask(task.getTaskUid());

        assertThrows(
                MeilisearchApiException.class,
                () -> index.getDocument(toDelete.getId(), Movie.class));
    }

    /** Test deleteDocuments */
    @Test
    public void testDeleteDocuments() throws Exception {

        String indexUid = "DeleteDocuments";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());
        index.waitForTask(task.getTaskUid());

        Results<Movie> result = index.getDocuments(Movie.class);
        Movie[] movies = result.getResults();
        assertThat(movies, is(arrayWithSize(20)));

        List<String> identifiersToDelete = getIdentifiersToDelete(movies);

        task = index.deleteDocuments(identifiersToDelete);
        index.waitForTask(task.getTaskUid());

        movies = index.getDocuments(Movie.class).getResults();

        boolean containsDeletedMovie =
                Arrays.stream(movies)
                        .anyMatch(movie -> identifiersToDelete.contains(movie.getId()));

        assertThat(containsDeletedMovie, is(equalTo(false)));
    }

    @NotNull
    private List<String> getIdentifiersToDelete(Movie[] movies) {
        return Arrays.asList(
                movies[1].getId(), movies[4].getId(), movies[10].getId(), movies[16].getId());
    }

    /** Test deleteDocumentsByFilter */
    @Test
    public void testDeleteDocumentsByFilter() throws Exception {

        String indexUid = "DeleteDocumentsByFilter";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());
        index.waitForTask(task.getTaskUid());

        Results<Movie> result = index.getDocuments(Movie.class);
        Movie[] movies = result.getResults();
        assertThat(movies, is(arrayWithSize(20)));

        index.waitForTask(index.updateFilterableAttributesSettings(new String[] {"genres"}).getTaskUid());

        String deleteFilter = "genres = action OR genres = adventure";

        task = index.deleteDocumentsByFilter(deleteFilter);
        index.waitForTask(task.getTaskUid());

        movies = index.getDocuments(Movie.class).getResults();

        boolean noActionOrAdventureMovie =
                Arrays.stream(movies)
                        .noneMatch(
                                movie ->
                                        Arrays.stream(movie.getGenres())
                                                .anyMatch(
                                                        genre ->
                                                                genre.equals("action")
                                                                        || genre.equals(
                                                                                "adventure")));

        assertThat(noActionOrAdventureMovie, is(equalTo(true)));
    }

    /** Test deleteAllDocuments */
    @Test
    public void testDeleteAllDocuments() throws Exception {
        String indexUid = "DeleteAllDocuments";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo taskIndex = index.addDocuments(testData.getRaw());
        index.waitForTask(taskIndex.getTaskUid());

        Results<Movie> result = index.getDocuments(Movie.class);
        Movie[] movies = result.getResults();
        assertThat(movies, is(arrayWithSize(20)));

        TaskInfo task = index.deleteAllDocuments();
        index.waitForTask(task.getTaskUid());

        assertThat(task, is(instanceOf(TaskInfo.class)));
        assertThat(task.getType(), is(equalTo("documentDeletion")));
        assertThat(task.getEnqueuedAt(), is(notNullValue()));

        movies = index.getDocuments(Movie.class).getResults();
        assertThat(movies, is(arrayWithSize(0)));
    }

    /** Test addDocuments with customMetadata */
    @Test
    public void testAddDocumentsWithCustomMetadata() throws Exception {
        String indexUid = "AddDocumentsWithCustomMetadata";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        String singleDocument = this.gson.toJson(testData.getData().get(0));
        TaskInfo task =
                index.addDocuments("[" + singleDocument + "]", null, null, "add-movies-metadata");

        index.waitForTask(task.getTaskUid());
        assertThat(task, is(instanceOf(TaskInfo.class)));
        assertThat(task.getType(), is(equalTo("documentAdditionOrUpdate")));

        // Verify the task was created with custom metadata
        com.meilisearch.sdk.model.Task taskDetails = index.getTask(task.getTaskUid());
        assertThat(taskDetails.getCustomMetadata(), is(equalTo("add-movies-metadata")));
    }

    /** Test updateDocuments with customMetadata */
    @Test
    public void testUpdateDocumentsWithCustomMetadata() throws Exception {
        String indexUid = "UpdateDocumentsWithCustomMetadata";
        Index index = createEmptyIndex(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo addTask = index.addDocuments(testData.getRaw());
        index.waitForTask(addTask.getTaskUid());

        String updateDocument =
                "[{\"id\":\"419704\",\"title\":\"Ad Astra Updated\",\"genre\":[\"Drama\"]}]";
        TaskInfo task = index.updateDocuments(updateDocument, null, null, "update-movies-metadata");

        index.waitForTask(task.getTaskUid());
        assertThat(task, is(instanceOf(TaskInfo.class)));
        assertThat(task.getType(), is(equalTo("documentAdditionOrUpdate")));

        // Verify the task was created with custom metadata
        com.meilisearch.sdk.model.Task taskDetails = index.getTask(task.getTaskUid());
        assertThat(taskDetails.getCustomMetadata(), is(equalTo("update-movies-metadata")));
    }

    /** Test deleteDocument with customMetadata */
    @Test
    public void testDeleteDocumentWithCustomMetadata() throws Exception {
        String indexUid = "DeleteDocumentWithCustomMetadata";
        Index index = createEmptyIndex(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo addTask = index.addDocuments(testData.getRaw());
        index.waitForTask(addTask.getTaskUid());

        TaskInfo task = index.deleteDocument("419704", "delete-single-movie-metadata");
        index.waitForTask(task.getTaskUid());

        assertThat(task, is(instanceOf(TaskInfo.class)));
        assertThat(task.getType(), is(equalTo("documentDeletion")));

        // Verify the task was created with custom metadata
        com.meilisearch.sdk.model.Task taskDetails = index.getTask(task.getTaskUid());
        assertThat(taskDetails.getCustomMetadata(), is(equalTo("delete-single-movie-metadata")));
    }

    /** Test deleteDocuments (batch) with customMetadata */
    @Test
    public void testDeleteDocumentsBatchWithCustomMetadata() throws Exception {
        String indexUid = "DeleteDocumentsBatchWithCustomMetadata";
        Index index = createEmptyIndex(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo addTask = index.addDocuments(testData.getRaw());
        index.waitForTask(addTask.getTaskUid());

        List<String> identifiers = Arrays.asList("419704", "574982");
        TaskInfo task = index.deleteDocuments(identifiers, "delete-batch-metadata");
        index.waitForTask(task.getTaskUid());

        assertThat(task, is(instanceOf(TaskInfo.class)));
        assertThat(task.getType(), is(equalTo("documentDeletion")));

        // Verify the task was created with custom metadata
        com.meilisearch.sdk.model.Task taskDetails = index.getTask(task.getTaskUid());
        assertThat(taskDetails.getCustomMetadata(), is(equalTo("delete-batch-metadata")));
    }

    /** Test deleteDocumentsByFilter with customMetadata */
    @Test
    public void testDeleteDocumentsByFilterWithCustomMetadata() throws Exception {
        String indexUid = "DeleteDocumentsByFilterWithCustomMetadata";
        Index index = createEmptyIndex(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo addTask = index.addDocuments(testData.getRaw());
        index.waitForTask(addTask.getTaskUid());

        index.waitForTask(index.updateFilterableAttributesSettings(new String[] {"id"}).getTaskUid());

        TaskInfo task = index.deleteDocumentsByFilter("id = 419704", "delete-filter-metadata");
        index.waitForTask(task.getTaskUid());

        assertThat(task, is(instanceOf(TaskInfo.class)));
        assertThat(task.getType(), is(equalTo("documentDeletion")));

        // Verify the task was created with custom metadata
        com.meilisearch.sdk.model.Task taskDetails = index.getTask(task.getTaskUid());
        assertThat(taskDetails.getCustomMetadata(), is(equalTo("delete-filter-metadata")));
    }

    /** Test deleteAllDocuments with customMetadata */
    @Test
    public void testDeleteAllDocumentsWithCustomMetadata() throws Exception {
        String indexUid = "DeleteAllDocumentsWithCustomMetadata";
        Index index = client.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo addTask = index.addDocuments(testData.getRaw());
        index.waitForTask(addTask.getTaskUid());

        TaskInfo task = index.deleteAllDocuments("delete-all-metadata");
        index.waitForTask(task.getTaskUid());

        assertThat(task, is(instanceOf(TaskInfo.class)));
        assertThat(task.getType(), is(equalTo("documentDeletion")));

        // Verify the task was created with custom metadata
        com.meilisearch.sdk.model.Task taskDetails = index.getTask(task.getTaskUid());
        assertThat(taskDetails.getCustomMetadata(), is(equalTo("delete-all-metadata")));

        Results<Movie> result = index.getDocuments(Movie.class);
        assertThat(result.getResults(), is(arrayWithSize(0)));
    }
}
