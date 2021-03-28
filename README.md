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
  <version>0.3.0</version>
  <type>pom</type>
</dependency>
```

### Gradle <!-- omit in toc -->

Add the following line to the `dependencies` section of your `build.gradle`:

```groovy
implementation 'com.meilisearch.sdk:meilisearch-java:0.3.0'
```

## 🚀 Getting Started

#### Add documents <!-- omit in toc -->

```java
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.ClientBuilder;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.api.documents.DocumentHandler;
import com.meilisearch.sdk.api.documents.Update;
import com.meilisearch.sdk.api.index.Index;
import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;
import com.meilisearch.sdk.http.ApacheHttpClient;
import com.meilisearch.sdk.json.GsonJsonHandler;

import java.util.Collections;
import java.util.Map;

public class TestMeiliSearch {
  public static void main(String[] args) throws Exception {

    final String documents = "["
      + "{\"id\": 123, \"title\": \"Pride and Prejudice\"},"
      + "{\"id\": 456, \"title\": \"Le Petit Prince\"},"
      + "{\"id\": 1, \"title\": \"Alice In Wonderland\"},"
      + "{\"id\": 1344, \"title\": \"The Hobbit\"},"
      + "{\"id\": 4, \"title\": \"Harry Potter and the Half-Blood Prince\"},"
      + "{\"id\": 2, \"title\": \"The Hitchhiker\'s Guide to the Galaxy\"}"
      + "]";

    Map<String, Class<?>> modelMapping = Collections.singletonMap("books", Book.class);
    Config config = new Config("http://localhost:7700", "masterKey", modelMapping);
    // set httpClient and JsonHandler explicit
    ApacheHttpClient httpClient = new ApacheHttpClient(config);
    GsonJsonHandler handler = new GsonJsonHandler();
    Client client = ClientBuilder.withConfig(config).withHttpClient(httpClient).withJsonHandler(handler).build();
    // let the sdk autodetect jsonhandler and httpclient based on the classes in the classpath
    client = ClientBuilder.withConfig(config).build();

    try {
      Index index = client.index().getOrCreateIndex("books", "id");
      DocumentHandler<Book> bookHandler = client.documents("books");
      Update update = bookHandler.addDocuments(documents);
      bookHandler.waitForPendingUpdate(update.getUpdateId());
      
      
    } catch (MeiliSearchRuntimeException e) {
      // an MeiliSearchRuntimeException will be thrown in case something went wrong
      e.printStackTrace();
    }
  }
}

public class Book {
  int id;
  String title;

  public int getId() {
    return id;
  }

  public Book setId(int id) {
    this.id = id;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public Book setTitle(String title) {
    this.title = title;
    return this;
  }
}
```

With the `updateId`, you can check the status (`enqueued`, `processed` or `failed`) of your documents addition using the [update endpoint](https://docs.meilisearch.com/reference/api/updates.html#get-an-update-status).

#### Basic Search <!-- omit in toc -->

A basic search can be performed by calling `search()` method on the handler, with a simple string query.

```java
import com.meilisearch.sdk.model.SearchResult;

DocumentHandler<Book> bookHandler = client.documents("books");
SearchResponse<Book> alice = bookHandler.search("Alice");
```

- Output:

```
SearchResponse(hits=[{id=4.0, title=Harry Potter and the Half-Blood Prince}], offset=0, limit=20, nbHits=1, exhaustiveNbHits=false, facetsDistribution=null, exhaustiveFacetsCount=false, processingTimeMs=3, query=harry pottre)
```

#### Custom Search <!-- omit in toc -->

If you want a custom search, the easiest way is to create a `SearchRequest` object, and set the parameters that you need.<br>
All the supported options are described in the [search parameters](https://docs.meilisearch.com/reference/features/search_parameters.html) section of the documentation.

```java
import com.meilisearch.sdk.SearchRequest;

// ...

SearchResult<Book> result = bookHandler.search(
  new SearchRequest("in")
  .setMatches(true)
  .setAttributesToHighlight(Arrays.asList("title"))
);
System.out.println(results.getHits());
```

- Output:

```json
[{
  "id":1,
  "title":"Alice In Wonderland",
  "_formatted":{
    "id":1,
    "title":"Alice <em>In</em> Wonderland"
  },
  "_matchesInfo":{
    "title":[{
      "start":6,
      "length":2
    }]
  }
}]
```

## 🤖 Compatibility with MeiliSearch

This package only guarantees the compatibility with the [version v0.19.0 of MeiliSearch](https://github.com/meilisearch/MeiliSearch/releases/tag/v0.19.0).

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
