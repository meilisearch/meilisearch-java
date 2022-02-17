package com.meilisearch.sdk.api.documents;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.GenericServiceTemplate;
import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;
import com.meilisearch.sdk.http.AbstractHttpClient;
import com.meilisearch.sdk.http.factory.BasicRequestFactory;
import com.meilisearch.sdk.http.factory.RequestFactory;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.http.response.BasicHttpResponse;
import com.meilisearch.sdk.json.JacksonJsonHandler;
import com.meilisearch.sdk.json.JsonHandler;
import com.meilisearch.sdk.utils.Movie;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class DocumentHandlerTest {

    private final AbstractHttpClient client = mock(AbstractHttpClient.class);
    private final JsonHandler jsonHandler = new JacksonJsonHandler(new ObjectMapper());
    private final RequestFactory requestFactory = new BasicRequestFactory(jsonHandler);
    private final GenericServiceTemplate serviceTemplate =
            new GenericServiceTemplate(client, jsonHandler, requestFactory);
    private final DocumentHandler<Movie> classToTest =
            new DocumentHandler<Movie>(serviceTemplate, requestFactory, "movies", Movie.class);

    @Test
    void getDocument() throws Exception {
        when(client.get(any(HttpRequest.class)))
                .thenAnswer(
                        invocation ->
                                new BasicHttpResponse(
                                        null,
                                        200,
                                        "{\"id\":25684,\"title\":\"American Ninja 5\",\"poster\":\"https://image.tmdb.org/t/p/w1280/iuAQVI4mvjI83wnirpD8GVNRVuY.jpg\",\"overview\":\"When a scientists daughter is kidnapped, American Ninja, attempts to find her, but this time he teams up with a youngster he has trained in the ways of the ninja.\",\"release_date\":\"1993-01-01\"}"));
        Movie movie = classToTest.getDocument("25684");
        assertNotNull(movie);
        assertEquals("25684", movie.getId());
        assertEquals("American Ninja 5", movie.getTitle());
        assertEquals(
                "https://image.tmdb.org/t/p/w1280/iuAQVI4mvjI83wnirpD8GVNRVuY.jpg",
                movie.getPoster());
        assertEquals(
                "When a scientists daughter is kidnapped, American Ninja, attempts to find her, but this time he teams up with a youngster he has trained in the ways of the ninja.",
                movie.getOverview());
        assertEquals("1993-01-01", movie.getRelease_date());
    }

    @Test
    void getDocuments() throws Exception {
        when(client.get(any(HttpRequest.class)))
                .thenAnswer(
                        invocation ->
                                new BasicHttpResponse(
                                        null,
                                        200,
                                        "[{\"id\":25684,\"release_date\":\"1993-01-01\",\"poster\":\"https://image.tmdb.org/t/p/w1280/iuAQVI4mvjI83wnirpD8GVNRVuY.jpg\",\"title\":\"American Ninja 5\",\"overview\":\"When a scientists daughter is kidnapped, American Ninja, attempts to find her, but this time he teams up with a youngster he has trained in the ways of the ninja.\"},{\"id\":468219,\"title\":\"Dead in a Week (Or Your Money Back)\",\"release_date\":\"2018-09-12\",\"poster\":\"https://image.tmdb.org/t/p/w1280/f4ANVEuEaGy2oP5M0Y2P1dwxUNn.jpg\",\"overview\":\"William has failed to kill himself so many times that he outsources his suicide to aging assassin Leslie. But with the contract signed and death assured within a week (or his money back), William suddenly discovers reasons to live... However Leslie is under pressure from his boss to make sure the contract is completed.\"}]"));
        List<Movie> movies = classToTest.getDocuments();
        assertNotNull(movies);
        assertEquals(2, movies.size());
        Movie movie = movies.get(0);
        assertEquals("25684", movie.getId());
        assertEquals("American Ninja 5", movie.getTitle());
        assertEquals(
                "https://image.tmdb.org/t/p/w1280/iuAQVI4mvjI83wnirpD8GVNRVuY.jpg",
                movie.getPoster());
        assertEquals(
                "When a scientists daughter is kidnapped, American Ninja, attempts to find her, but this time he teams up with a youngster he has trained in the ways of the ninja.",
                movie.getOverview());
        assertEquals("1993-01-01", movie.getRelease_date());
    }

    @Test
    void addAndReplaceDocument() throws Exception {
        when(client.post(any(HttpRequest.class)))
                .thenAnswer(invocation -> new BasicHttpResponse(null, 200, "{\"uid\":1}"));
        Movie movie =
                new Movie(
                        "287947",
                        "Shazam",
                        "https://image.tmdb.org/t/p/w1280/xnopI5Xtky18MPhK40cZAGAOVeV.jpg",
                        "A boy is given the ability to become an adult superhero in times of need with a single magic word.",
                        "2019-03-23",
                        "English",
                        "Action",
                        "Comedy",
                        "Fantasy");
        Task task = classToTest.replaceDocuments(Collections.singletonList(movie));
        assertNotNull(task);
        assertEquals(1, task.getUid());
    }

    @Test
    void addAndUpdateDocument() throws Exception {
        when(client.put(any(HttpRequest.class)))
                .thenAnswer(invocation -> new BasicHttpResponse(null, 200, "{\"uid\":1}"));
        Task task =
                classToTest.updateDocuments(
                        "[{\"id\":287947,\"title\":\"Shazam\",\"poster\":\"https://image.tmdb.org/t/p/w1280/xnopI5Xtky18MPhK40cZAGAOVeV.jpg\",\"overview\":\"A boy is given the ability to become an adult superhero in times of need with a single magic word.\",\"release_date\":\"2019-03-23\"}]");
        assertNotNull(task);
        assertEquals(1, task.getUid());
    }

    @Test
    void deleteDocument() throws Exception {
        when(client.delete(any(HttpRequest.class)))
                .thenAnswer(invocation -> new BasicHttpResponse(null, 200, "{\"uid\": 1}"));
        assertEquals(1, classToTest.deleteDocument("123").getUid());
        when(client.delete(any(HttpRequest.class)))
                .thenAnswer(invocation -> new BasicHttpResponse(null, 200, null));
        assertThrows(MeiliSearchRuntimeException.class, () -> classToTest.deleteDocument("123"));
        when(client.delete(any(HttpRequest.class)))
                .thenAnswer(invocation -> new BasicHttpResponse(null, 200, ""));
        assertThrows(MeiliSearchRuntimeException.class, () -> classToTest.deleteDocument("123"));
    }

    @Test
    void deleteDocuments() throws Exception {
        when(client.delete(any(HttpRequest.class)))
                .thenAnswer(invocation -> new BasicHttpResponse(null, 200, "{\"uid\": 1}"));
        assertEquals(1, classToTest.deleteDocuments().getUid());
    }

    @Test
    void search() throws Exception {
        when(client.post(any(HttpRequest.class)))
                .thenAnswer(
                        invocation ->
                                new BasicHttpResponse(
                                        null,
                                        200,
                                        "{\"hits\":[{\"id\":\"2770\",\"title\":\"American Pie 2\",\"poster\":\"https://image.tmdb.org/t/p/w1280/q4LNgUnRfltxzp3gf1MAGiK5LhV.jpg\",\"overview\":\"The whole gang are back and as close as ever. They decide to get even closer by spending the summer together at a beach house. They decide to hold the biggest...\",\"release_date\":997405200},{\"id\":\"190859\",\"title\":\"American Sniper\",\"poster\":\"https://image.tmdb.org/t/p/w1280/svPHnYE7N5NAGO49dBmRhq0vDQ3.jpg\",\"overview\":\"U.S. Navy SEAL Chris Kyle takes his sole mission—protect his comrades—to heart and becomes one of the most lethal snipers in American history. His pinpoint accuracy not only saves countless lives but also makes him a prime...\",\"release_date\":1418256000}],\"offset\":0,\"limit\":20,\"nbHits\":1,\"exhaustiveNbHits\":false,\"processingTimeMs\":2,\"query\":\"test120232\"}"));
        SearchResponse<Movie> movieSearchResponse = classToTest.search("American Pie");
        assertNotNull(movieSearchResponse);
        assertNotNull(movieSearchResponse.getHits());
        assertEquals(2, movieSearchResponse.getHits().size());
        assertEquals("American Pie 2", movieSearchResponse.getHits().get(0).getTitle());
        assertEquals("2770", movieSearchResponse.getHits().get(0).getId());
        assertEquals(
                "https://image.tmdb.org/t/p/w1280/q4LNgUnRfltxzp3gf1MAGiK5LhV.jpg",
                movieSearchResponse.getHits().get(0).getPoster());
        assertEquals("997405200", movieSearchResponse.getHits().get(0).getRelease_date());
    }

    @Test
    void testSearch() throws Exception {
        when(client.post(any(HttpRequest.class)))
                .thenAnswer(
                        invocation ->
                                new BasicHttpResponse(
                                        null,
                                        200,
                                        "{\"hits\":[{\"id\":\"2770\",\"title\":\"American Pie 2\",\"poster\":\"https://image.tmdb.org/t/p/w1280/q4LNgUnRfltxzp3gf1MAGiK5LhV.jpg\",\"overview\":\"The whole gang are back and as close as ever. They decide to get even closer by spending the summer together at a beach house. They decide to hold the biggest...\",\"release_date\":997405200},{\"id\":\"190859\",\"title\":\"American Sniper\",\"poster\":\"https://image.tmdb.org/t/p/w1280/svPHnYE7N5NAGO49dBmRhq0vDQ3.jpg\",\"overview\":\"U.S. Navy SEAL Chris Kyle takes his sole mission—protect his comrades—to heart and becomes one of the most lethal snipers in American history. His pinpoint accuracy not only saves countless lives but also makes him a prime...\",\"release_date\":1418256000}],\"offset\":0,\"limit\":20,\"nbHits\":1,\"exhaustiveNbHits\":false,\"processingTimeMs\":2,\"query\":\"test120232\"}"));
        SearchResponse<Movie> movieSearchResponse =
                classToTest.search(new SearchRequest("american pie"));
        assertNotNull(movieSearchResponse);
        assertNotNull(movieSearchResponse.getHits());
        assertEquals(2, movieSearchResponse.getHits().size());
        assertEquals("American Pie 2", movieSearchResponse.getHits().get(0).getTitle());
        assertEquals("2770", movieSearchResponse.getHits().get(0).getId());
        assertEquals(
                "https://image.tmdb.org/t/p/w1280/q4LNgUnRfltxzp3gf1MAGiK5LhV.jpg",
                movieSearchResponse.getHits().get(0).getPoster());
        assertEquals("997405200", movieSearchResponse.getHits().get(0).getRelease_date());
    }

    @Test
    void getTask() throws Exception {
        when(client.get(any(HttpRequest.class)))
                .thenAnswer(
                        invocation ->
                                new BasicHttpResponse(
                                        null,
                                        200,
                                        "{\"status\":\"succeeded\",\"uid\": 1,\"indexUid\":\"AddDocuments\",\"type\":\"documentAddition\",\"duration\":\"PT0.021985708S\",\"enqueuedAt\":\"2022-01-11T13:54:18.408270424Z\",\"startedAt\":\"2022-01-11T13:54:18.411897715Z\",\"finishedAt\":\"2022-01-11T13:54:18.430256132Z\"}"));
        Task task = classToTest.getTask(1);
        assertNotNull(task);
        assertEquals("succeeded", task.getStatus());
        assertEquals(1, task.getUid());
        assertEquals("documentAddition", task.getType());
        assertEquals("PT0.021985708S", task.getDuration());
        assertEquals("2022-01-11T13:54:18.408270424Z", task.getEnqueuedAt());
        assertEquals("2022-01-11T13:54:18.411897715Z", task.getStartedAt());
        assertEquals("2022-01-11T13:54:18.430256132Z", task.getFinishedAt());
    }

    // Must be reviewed when resolving the issue #315
    // @Test
    // void getTasks() throws Exception {
    //     when(client.get(any(HttpRequest.class)))
    //             .thenAnswer(
    //                     invocation ->
    //                             new BasicHttpResponse(
    //                                     null,
    //                                     200,
    //                                     "\"results\":[{\"status\":\"succeeded\",\"uid\":
    // 1,\"indexUid\":\"AddDocuments\",\"type\":\"documentAddition\",\"duration\":\"PT0.021985708S\",\"enqueuedAt\":\"2022-01-11T13:54:18.408270424Z\",\"startedAt\":\"2022-01-11T13:54:18.411897715Z\",\"finishedAt\":\"2022-01-11T13:54:18.430256132Z\"}]"));
    //     //
    // "[{\"status\":\"processed\",\"uid\":1,\"type\":{\"name\":\"DocumentsAddition\",\"number\":4},\"duration\":0.076980613,\"enqueuedAt\":\"2019-12-07T21:16:09.623944Z\",\"processedAt\":\"2019-12-07T21:16:09.703509Z\"}]"));
    //     List<Task> tasks = classToTest.getTasks();
    //     assertNotNull(tasks);
    //     assertEquals(1, tasks.size());
    //     Task task = tasks.get(0);
    //     assertEquals("succeeded", task.getStatus());
    //     assertEquals(1, task.getUid());
    //     assertEquals("DocumentsAddition", task.getType());
    //     assertEquals(0.076980613, task.getDuration());
    //     assertEquals("2019-12-07T21:16:09.623944Z", task.getEnqueuedAt());
    //     assertEquals("2022-01-11T13:54:18.411897715Z", task.getStartedAt());
    //     assertEquals("2022-01-11T13:54:18.430256132Z", task.getFinishedAt());
    // }
}
