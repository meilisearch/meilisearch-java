<p align="center">
  <img src="https://raw.githubusercontent.com/meilisearch/integration-guides/main/assets/logos/meilisearch_java.svg" alt="Meilisearch Java" width="200" height="200" />
</p>

<h1 align="center">Meilisearch Java</h1>

<h4 align="center">
  <a href="https://github.com/meilisearch/meilisearch">Meilisearch</a> |
<a href="https://www.meilisearch.com/cloud?utm_campaign=oss&utm_source=github&utm_medium=meilisearch-java">Meilisearch Cloud</a> |
  <a href="https://www.meilisearch.com/docs">Documentation</a> |
  <a href="https://discord.meilisearch.com">Discord</a> |
  <a href="https://roadmap.meilisearch.com/tabs/1-under-consideration">Roadmap</a> |
  <a href="https://www.meilisearch.com">Website</a> |
  <a href="https://www.meilisearch.com/docs/faq">FAQ</a>
</h4>

<p align="center">
  <a href="https://maven-badges.herokuapp.com/maven-central/com.meilisearch.sdk/meilisearch-java"><img src="https://maven-badges.herokuapp.com/maven-central/com.meilisearch.sdk/meilisearch-java/badge.svg" alt="Version"></a>
  <a href="https://github.com/meilisearch/meilisearch-java/actions"><img src="https://github.com/meilisearch/meilisearch-java/workflows/Tests/badge.svg" alt="Tests"></a>
  <a href="https://github.com/meilisearch/meilisearch-java/blob/main/LICENSE"><img src="https://img.shields.io/badge/license-MIT-informational" alt="License"></a>
  <a href="https://ms-bors.herokuapp.com/repositories/60"><img src="https://bors.tech/images/badge_small.svg" alt="Bors enabled"></a>
  <a href="https://codecov.io/gh/meilisearch/meilisearch-java"><img src="https://codecov.io/gh/meilisearch/meilisearch-java/branch/main/graph/badge.svg" alt="Code Coverage"></a>
</p>

<p align="center">⚡ The Meilisearch API client written for Java ☕️</p>

**Meilisearch Java** is the Meilisearch API client for Java developers.

**Meilisearch** is an open-source search engine. [Learn more about Meilisearch.](https://github.com/meilisearch/meilisearch)

## Table of Contents <!-- omit in TOC -->

- [📖 Documentation](#-documentation)
- [🔧 Installation](#-installation)
- [🚀 Getting started](#-getting-started)
- [🛠 Customization](#-customization)
- [🤖 Compatibility with Meilisearch](#-compatibility-with-meilisearch)
- [💡 Learn more](#-learn-more)
- [⚙️ Contributing](#️-contributing)

## 📖 Documentation

This readme contains all the documentation you need to start using this Meilisearch SDK.

For general information on how to use Meilisearch—such as our API reference, tutorials, guides, and in-depth articles—refer to our [main documentation website](https://www.meilisearch.com/docs/).



## 🔧 Installation

`meilisearch-java` is available from JCentral official repository. To be able to import this package, declare it as a dependency in your project:

### Maven <!-- omit in toc -->

Add the following code to the `<dependencies>` section of your project:

```xml
<dependency>
  <groupId>com.meilisearch.sdk</groupId>
  <artifactId>meilisearch-java</artifactId>
  <version>0.15.0</version>
  <type>pom</type>
</dependency>
```

### Gradle <!-- omit in toc -->

Add the following line to the `dependencies` section of your `build.gradle`:

```groovy
implementation 'com.meilisearch.sdk:meilisearch-java:0.15.0'
```

:warning: `meilisearch-java` also requires `okhttp` as a peer dependency.

### Run Meilisearch <!-- omit in toc -->

⚡️ **Launch, scale, and streamline in minutes with Meilisearch Cloud**—no maintenance, no commitment, cancel anytime. [Try it free now](https://cloud.meilisearch.com/login?utm_campaign=oss&utm_source=github&utm_medium=meilisearch-java).

🪨  Prefer to self-host? [Download and deploy](https://www.meilisearch.com/docs/learn/self_hosted/getting_started_with_self_hosted_meilisearch?utm_campaign=oss&utm_source=github&utm_medium=meilisearch-java) our fast, open-source search engine on your own infrastructure.

## 🚀 Getting started

#### Add documents <!-- omit in toc -->

```java
package com.meilisearch.sdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

class TestMeilisearch {
  public static void main(String[] args) throws Exception {

    JSONArray array = new JSONArray();
    ArrayList items = new ArrayList() {{
      add(new JSONObject().put("id", "1").put("title", "Carol").put("genres",new JSONArray("[\"Romance\",\"Drama\"]")));
      add(new JSONObject().put("id", "2").put("title", "Wonder Woman").put("genres",new JSONArray("[\"Action\",\"Adventure\"]")));
      add(new JSONObject().put("id", "3").put("title", "Life of Pi").put("genres",new JSONArray("[\"Adventure\",\"Drama\"]")));
      add(new JSONObject().put("id", "4").put("title", "Mad Max: Fury Road").put("genres",new JSONArray("[\"Adventure\",\"Science Fiction\"]")));
      add(new JSONObject().put("id", "5").put("title", "Moana").put("genres",new JSONArray("[\"Fantasy\",\"Action\"]")));
      add(new JSONObject().put("id", "6").put("title", "Philadelphia").put("genres",new JSONArray("[\"Drama\"]")));
    }};

    array.put(items);
    String documents = array.getJSONArray(0).toString();
    Client client = new Client(new Config("http://localhost:7700", "masterKey"));

    // An index is where the documents are stored.
    Index index = client.index("movies");

    // If the index 'movies' does not exist, Meilisearch creates it when you first add the documents.
    index.addDocuments(documents); // => { "taskUid": 0 }
  }
}
```

With the `taskUid`, you can check the status (`enqueued`, `canceled`, `processing`, `succeeded` or `failed`) of your documents addition using the [task endpoint](https://www.meilisearch.com/docs/reference/api/tasks).

#### Basic Search <!-- omit in toc -->

A basic search can be performed by calling `index.search()` method, with a simple string query.

```java
import com.meilisearch.sdk.model.SearchResult;

// Meilisearch is typo-tolerant:
SearchResult results = index.search("carlo");
System.out.println(results);
```

- Output:

```
SearchResult(hits=[{id=1.0, title=Carol, genres:[Romance, Drama]}], offset=0, limit=20, estimatedTotalHits=1, facetDistribution=null, processingTimeMs=3, query=carlo)
```

#### Custom Search <!-- omit in toc -->

If you want a custom search, the easiest way is to create a `SearchRequest` object, and set the parameters that you need.<br>
All the supported options are described in the [search parameters](https://www.meilisearch.com/docs/reference/api/search#search-parameters) section of the documentation.

```java
import com.meilisearch.sdk.SearchRequest;

// ...

SearchResult results = (SearchResult) index.search(
    new SearchRequest("of")
        .setShowMatchesPosition(true)
        .setAttributesToHighlight(new String[]{"title"})
);
System.out.println(results.getHits());
```

- Output:

```json
[{
  "id":3,
  "title":"Life of Pi",
  "genres":["Adventure","Drama"],
  "_formatted":{
    "id":3,
    "title":"Life <em>of</em> Pi",
    "genres":["Adventure","Drama"]
  },
  "_matchesPosition":{
    "title":[{
      "start":5.0,
      "length":2.0
    }]
  }
}]
```
#### Custom Search With Filters <!-- omit in toc -->

If you want to enable filtering, you must add your attributes to the `filterableAttributes` index setting.

```java
index.updateFilterableAttributesSettings(new String[]
{
  "id",
  "genres"
});
```

You only need to perform this operation once.

Note that Meilisearch will rebuild your index whenever you update `filterableAttributes`. Depending on the size of your dataset, this might take time. You can track the process using the [task status](https://www.meilisearch.com/docs/reference/api/tasks).

Then, you can perform the search:

```java
index.search(
  new SearchRequest("wonder")
  .setFilter(new String[] {"id > 1 AND genres = Action"})
);
```

```json
{
  "hits": [
    {
      "id": 2,
      "title": "Wonder Woman",
      "genres": ["Action","Adventure"]
    }
  ],
  "offset": 0,
  "limit": 20,
  "estimatedTotalHits": 1,
  "processingTimeMs": 0,
  "query": "wonder"
}
```
#### Custom Search With Pagination <!-- omit in toc -->

```java
import com.meilisearch.sdk.SearchResultPaginated;

// ...

SearchResultPaginated results = (SearchResultPaginated) index.search(
    new SearchRequest("wonder")
        .setPage(1)
        .setHitsPerPage(20)
);
```

```json
{
    "hits": [
        {
            "id": 2,
            "title": "Wonder Woman",
            "genres": ["Action","Adventure"]
        }
    ], 
    "query": "wonder",
    "processingTimeMs": 0,
    "hitsPerPage": 20,
    "page": 1,
    "totalPages": 1,
    "totalHits": 1
}
```

## 🛠 Customization

### JSON <!-- omit in toc -->

#### Default JSON `GsonJsonHandler` <!-- omit in toc -->

The default JSON library is `Gson`. You can however use another library with the `JsonHandler` Class.

*Notes*: We strongly recommend using the `Gson` library.

#### Using `JacksonJsonHandler` <!-- omit in toc -->

Initialize your `Config` and assign it a new `JacksonJsonHandler` object as `JsonHandler`.
Set up your `Client` with it.

```java
import com.meilisearch.sdk.json.JacksonJsonHandler;

Config config = new Config("http://localhost:7700", "masterKey", new JacksonJsonHandler());
Client client = new Client(config);
```

#### Use a Custom `JsonHandler` <!-- omit in toc -->

To create your own JSON handler, you must conform to the `JsonHandler` interface by implementing its two methods.

```java
    String encode(Object o) throws Exception;

    <T> T decode(Object o, Class<?> targetClass, Class<?>... parameters) throws Exception;
```

 Then create your client by initializing your `Config` with your new handler.

```java
Config config = new Config("http://localhost:7700", "masterKey", new myJsonHandler());
Client client = new Client(config);
```

## 🤖 Compatibility with Meilisearch

This package guarantees compatibility with [version v1.x of Meilisearch](https://github.com/meilisearch/meilisearch/releases/latest), but some features may not be present. Please check the [issues](https://github.com/meilisearch/meilisearch-java/issues?q=is%3Aissue+is%3Aopen+label%3A%22good+first+issue%22+label%3Aenhancement) for more info.

This SDK is compatible with the following JDK versions:

| SDK Version | Supported Java Versions |
|-------------|-------------------------|
| v1.x        | JDK 8 and above         |

## 💡 Learn more

The following sections in our main documentation website may interest you:

- **Manipulate documents**: see the [API references](https://www.meilisearch.com/docs/reference/api/documents) or read more about [documents](https://www.meilisearch.com/docs/learn/core_concepts/documents).
- **Search**: see the [API references](https://www.meilisearch.com/docs/reference/api/search) or follow our guide on [search parameters](https://www.meilisearch.com/docs/reference/api/search#search-parameters).
- **Manage the indexes**: see the [API references](https://www.meilisearch.com/docs/reference/api/indexes) or read more about [indexes](https://www.meilisearch.com/docs/learn/core_concepts/indexes).
- **Configure the index settings**: see the [API references](https://www.meilisearch.com/docs/reference/api/settings) or follow our guide on [settings parameters](https://www.meilisearch.com/docs/reference/api/settings#settings_parameters).

## ⚙️ Contributing

Any new contribution is more than welcome in this project!

If you want to know more about the development workflow or want to contribute, please visit our [contributing guidelines](/CONTRIBUTING.md) for detailed instructions!

<hr>

**Meilisearch** provides and maintains many **SDKs and Integration tools** like this one. We want to provide everyone with an **amazing search experience for any kind of project**. If you want to contribute, make suggestions, or just know what's going on right now, visit us in the [integration-guides](https://github.com/meilisearch/integration-guides) repository.
