<p align="center">
  <img src="https://raw.githubusercontent.com/meilisearch/integration-guides/main/assets/logos/logo.svg" alt="MeiliSearch Java" width="200" height="200" />
</p>

<h1 align="center">MeiliSearch Java</h1>

<h4 align="center">
  <a href="https://github.com/meilisearch/MeiliSearch">MeiliSearch</a> |
  <a href="https://docs.meilisearch.com">Documentation</a> |
  <a href="https://slack.meilisearch.com">Slack</a> |
  <a href="https://roadmap.meilisearch.com/tabs/1-under-consideration">Roadmap</a> |
  <a href="https://www.meilisearch.com">Website</a> |
  <a href="https://docs.meilisearch.com/faq">FAQ</a>
</h4>

<p align="center">
  <a href="https://maven-badges.herokuapp.com/maven-central/com.meilisearch.sdk/meilisearch-java"><img src="https://maven-badges.herokuapp.com/maven-central/com.meilisearch.sdk/meilisearch-java/badge.svg" alt="Version"></a>
  <a href="https://github.com/meilisearch/meilisearch-java/actions"><img src="https://github.com/meilisearch/meilisearch-java/workflows/Tests/badge.svg" alt="Tests"></a>
  <a href="https://github.com/meilisearch/meilisearch-java/blob/main/LICENSE"><img src="https://img.shields.io/badge/license-MIT-informational" alt="License"></a>
  <a href="https://app.bors.tech/repositories/29365"><img src="https://bors.tech/images/badge_small.svg" alt="Bors enabled"></a>
</p>

<p align="center">⚡ The MeiliSearch API client written for Java ☕️</p>

**MeiliSearch Java** is the MeiliSearch API client for Java developers.

**MeiliSearch** is an open-source search engine. [Discover what MeiliSearch is!](https://github.com/meilisearch/MeiliSearch)

## Table of Contents <!-- omit in toc -->

- [📖 Documentation](#-documentation)
- [🔧 Installation](#-installation)
- [🚀 Getting Started](#-getting-started)
- [🤖 Compatibility with MeiliSearch](#-compatibility-with-meilisearch)
- [💡 Learn More](#-learn-more)
- [⚙️ Development Workflow and Contributing](#️-development-workflow-and-contributing)

## 📖 Documentation

See our [Documentation](https://docs.meilisearch.com/learn/tutorials/getting_started.html) or our [API References](https://docs.meilisearch.com/reference/api/).


## 🔧 Installation

`meilisearch-java` is available from JCentral official repository. To be able to import this package, declare it as a dependency in your project:

### Maven <!-- omit in toc -->

Add the following code to the `<dependencies>` section of your project:

```xml
<dependency>
  <groupId>com.meilisearch.sdk</groupId>
  <artifactId>meilisearch-java</artifactId>
  <version>0.4.1</version>
  <type>pom</type>
</dependency>
```

### Gradle <!-- omit in toc -->

Add the following line to the `dependencies` section of your `build.gradle`:

```groovy
implementation 'com.meilisearch.sdk:meilisearch-java:0.4.1'
```

## 🚀 Getting Started

#### Add documents <!-- omit in toc -->

```java
package com.meilisearch.sdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

class TestMeiliSearch {
  public static void main(String[] args) throws Exception {

    JSONArray array = new JSONArray();
    ArrayList items = new ArrayList() {{
      add(new JSONObject().put("movie_id", "1").put("title", "Carol").put("genre",new JSONArray("[\"Romance\",\"Drama\"]")));
      add(new JSONObject().put("movie_id", "2").put("title", "Wonder Woman").put("genre",new JSONArray("[\"Action\",\"Adventure\"]")));
      add(new JSONObject().put("movie_id", "3").put("title", "Life of Pi").put("genre",new JSONArray("[\"Adventure\",\"Drama\"]")));
      add(new JSONObject().put("movie_id", "4").put("title", "Mad Max: Fury Road").put("genre",new JSONArray("[\"Adventure\",\"Science Fiction\"]")));
      add(new JSONObject().put("movie_id", "5").put("title", "Moana").put("genre",new JSONArray("[\"Fantasy\",\"Action\"]")));
      add(new JSONObject().put("movie_id", "6").put("title", "Philadelphia").put("genre",new JSONArray("[\"Drama\"]")));
    }};

    array.put(items);
    String documents = array.getJSONArray(0).toString();
    Client client = new Client(new Config("http://localhost:7700", "masterKey"));

    // An index is where the documents are stored.
    Index index = client.index("movies");

    // If the index 'movies' does not exist, MeiliSearch creates it when you first add the documents.
    index.addDocuments(documents); // => { "updateId": 0 }
  }
}
```

With the `updateId`, you can check the status (`enqueued`, `processing`, `processed` or `failed`) of your documents addition using the [update endpoint](https://docs.meilisearch.com/reference/api/updates.html#get-an-update-status).

#### Basic Search <!-- omit in toc -->

A basic search can be performed by calling `index.search()` method, with a simple string query.

```java
import com.meilisearch.sdk.model.SearchResult;

// MeiliSearch is typo-tolerant:
SearchResult results = index.search("mad max");
System.out.println(results);
```

- Output:

```
SearchResult(hits=[{movie_id=4.0, title=Mad Max: Fury Road, genre:[Adventure, Science Fiction]}], offset=0, limit=20, nbHits=1, exhaustiveNbHits=false, facetsDistribution=null, exhaustiveFacetsCount=false, processingTimeMs=3, query=mad max)
```

#### Custom Search <!-- omit in toc -->

If you want a custom search, the easiest way is to create a `SearchRequest` object, and set the parameters that you need.<br>
All the supported options are described in the [search parameters](https://docs.meilisearch.com/reference/features/search_parameters.html) section of the documentation.

```java
import com.meilisearch.sdk.SearchRequest;

// ...

SearchResult results = index.search(
  new SearchRequest("in")
  .setMatches(true)
  .setAttributesToHighlight(new String[]{"title"})
);
System.out.println(results.getHits());
```

- Output:

```json
[{
  "movie_id":1,
  "title":"Carol",
  "genre":["Romance","Drama"],
  "_formatted":{
    "movie_id":1,
    "title":"Carol",
    "genre":["Romance","Drama"]
  },
  "_matchesInfo":{
    "title":[{
      "start":6,
      "length":2
    }]
  }
}]
```
#### Basic JSON <!-- omit in toc -->
The default JSON can be created by calling the default constructor of <b>JsonbJsonHandler</b> class which will create a config of type JsonbConfig and using this config, it will initialize the mapper variable by calling the create method of <b>JsonbBuilder</b> class.

#### Custom JSON <!-- omit in toc -->
<b>Creating Custom GsonJsonHandler</b><br>
To create a custom JSON handler, create an object of GsonJsonHandler and send the GSON object in the parameterized constructor.<br>
```
Gson gson = new GsonBuilder()
             .disableHtmlEscaping()
             .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
             .setPrettyPrinting()
             .serializeNulls()
             .create();
private GsonJsonHandler jsonGson = new GsonJsonHandler(gson);
jsonGson.encode("dummy_data");
```
<b>Creating Custom JacksonJsonHandler</b><br>
Another method is to create an object of JacksonJsonHandler and set the required parameters. The supported option is an object of <b>ObjectMapper</b> is passed as a parameter to the <b>JacksonJsonHandler’s parameterized constructor</b>. This is used to initialize the mapper variable.

The mapper variable is responsible for the encoding and decoding of the JSON. <br>
<b>Using the custom JSON: </b>

```
Config config = new Config("http://localhost:7700", "masterKey");
HttpAsyncClient client = HttpAsyncClients.createDefault();
ApacheHttpClient client = new ApacheHttpClient(config, client);
private final JsonHandler jsonHandler = new JacksonJsonHandler(new ObjectMapper());
private final RequestFactory requestFactory = new BasicRequestFactory(jsonHandler);
private final GenericServiceTemplate serviceTemplate = new GenericServiceTemplate(client, jsonHandler, requestFactory);

private final ServiceTemplate serviceTemplate;
serviceTemplate.getProcessor().encode("dummy_data");
```
<b>Creating Custom JsonbJsonHandler</b><br>
Another method of creating a JSON handler is to create an object of JsonbJsonHandler and send the Jsonb object in the parameterized constructor.<br>
```
Jsonb jsonb = JsonbBuilder.create();
private JsonbJsonHandler jsonbHandler = new JsonbJsonHandler(jsonb);
jsonbHandler.encode("dummy_data");
```

#### Custom Client
To create a custom Client handler, create an object of <b>Client</b> and set the required parameters. The supported options are as follows:
Config: Config is class which has 2 member variables <br>
(a) hostUrl <br>
(b)apiKey<br>

```
Config config = new Config(“dummy_url”,”dummy_key”);
return new Client(config);
```
The Client(config) constructor sets the config instance to the member variable. It also sets the 3 other instances namely - <b>gson(),  IndexesHandler(config) and DumpHandler(config).</b>

<b>Using the custom Client: </b>
```
Config config = new Config("http://localhost:7700", "masterKey");
HttpAsyncClient client = HttpAsyncClients.createDefault();
ApacheHttpClient customClient = new ApacheHttpClient(config, client);
customClient.index("movies").search("American ninja");
```

#### Custom Http Request
To create a custom HTTP request, create an object of BasicHttpRequest and set the required parameters.The supported options are as follows:<br>
1. HTTP method (It can consume the following values: HEAD, GET, POST, PUT, DELETE). [Datatype : String]<br>
2. Path : It accepts the endpoint details of the api [Datatype : String]<br>
3. Headers: It accepts a Map containing the header parameters in the form of key-value pair. [Datatype : Map<String,String>]<br>
4. Content of String type<br>

```
return new BasicHttpRequest(
                    method,
                    path,
                    headers,
                    content == null ? null : this.jsonHandler.encode(content));
```
Alternatively, there is an interface <b>RequestFactory</b> which has a method ‘create’.
In order to call this method, create an object of RequestFactory and call the method by passing the required parameters.<br>
<b>Using the custom Http Request: </b>
```
public interface RequestFactory {
    <T> HttpRequest<?> create(
            HttpMethod method, String path, Map<String, String> headers, T content);
 }

private final RequestFactory requestFactory;
requestFactory.create(HttpMethod.GET, "/health", Collections.emptyMap(), {"movie_id":"1"});
```
## 🤖 Compatibility with MeiliSearch

This package only guarantees the compatibility with the [version v0.22.0 of MeiliSearch](https://github.com/meilisearch/MeiliSearch/releases/tag/v0.22.0).

## 💡 Learn More

The following sections may interest you:

- **Manipulate documents**: see the [API references](https://docs.meilisearch.com/reference/api/documents.html) or read more about [documents](https://docs.meilisearch.com/learn/core_concepts/documents.html).
- **Search**: see the [API references](https://docs.meilisearch.com/reference/api/search.html) or follow our guide on [search parameters](https://docs.meilisearch.com/reference/features/search_parameters.html).
- **Manage the indexes**: see the [API references](https://docs.meilisearch.com/reference/api/indexes.html) or read more about [indexes](https://docs.meilisearch.com/learn/core_concepts/indexes.html).
- **Configure the index settings**: see the [API references](https://docs.meilisearch.com/reference/api/settings.html) or follow our guide on [settings parameters](https://docs.meilisearch.com/reference/features/settings.html).

## ⚙️ Development Workflow and Contributing

Any new contribution is more than welcome in this project!

If you want to know more about the development workflow or want to contribute, please visit our [contributing guidelines](/CONTRIBUTING.md) for detailed instructions!

<hr>

**MeiliSearch** provides and maintains many **SDKs and Integration tools** like this one. We want to provide everyone with an **amazing search experience for any kind of project**. If you want to contribute, make suggestions, or just know what's going on right now, visit us in the [integration-guides](https://github.com/meilisearch/integration-guides) repository.
