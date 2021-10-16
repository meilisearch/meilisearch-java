# Search and Raw Search

Table of contents
=================

* Getting started
* Search
    * Parameters
    * Return
    * Search Requests
    * Accessible methods from the SearchResult instance
* Raw Search
  * Parameters
  * Return
  * Usage
To know how to access the different endpoints: check out the [API references](https://docs.meilisearch.com/reference/api/) in the docs.

🚀 Getting started
=================
```
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
More details: meilisearch-java's [document](https://github.com/meilisearch/meilisearch-java)

Search
=====
`search` is a method of the `Search` class

```
    public SearchResult search(SearchRequest sr) throws Exception {
        return this.search.search(this.uid, sr);
    }
```
### Parameters
* `String q`
* `SearchRequest`
* `String uid`
* `int offset`
* `int limit`
* `String[] attributesToRetrieve`
* `String[] attributesToCrop`
* `int cropLength`
* `String[] attributesToHighlight`
* `String filter`
* `boolean matches`
* `String[] facetsDistribution`
* `String[] sort`

View detailed info for each parameter [here](https://docs.meilisearch.com/reference/api/search.html#search-in-an-index-with-post-route)

### Return
The `search()` method returns a `SearchResult` instance.

💡 If you don't want the `search()` method to return a class instance, use the `rawSearch()` method. No instance class will be created during the process which might improve the performances in some situations.

#### Search Requests

👍 All the supported search requests are described in the [search parameters](https://docs.meilisearch.com/reference/features/search_parameters.html#parameters) section of the documentation.

You will be able to filter, use facets, restrict the fields to retrieve...

Here are different options for a custom search with `SearchRequest`. 

You can create a `SearchRequest` object, and set the parameters that you need.

`setAttributesToRetrieve()`

Configures which attributes will be retrieved in the returned documents.
```
import com.meilisearch.sdk.SearchRequest;

// ...

SearchResult results = index.search(
  new SearchRequest("of")
  .setAttributesToHighlight(new String[]{"genre"})
);
```

`setFilter()`

Refine search result based on filtered keywords
```
import com.meilisearch.sdk.SearchRequest;

// ...

SearchResult results = index.search(
  new SearchRequest("of")
  .setFilter(new String[]{"genre = Fantasy", "genres = Drama"})
);
 
```
`setSort()`

Sorts search results at query time according to the specified attributes and indicated order.

Each attribute in the list must be followed by a colon (`:`) and the preferred sorting order: either ascending (`asc`) or descending (`desc`).

```
import com.meilisearch.sdk.SearchRequest;

// ...

SearchResult results = index.search(
  new SearchRequest("of")
  .setSort(new String[]{"genre:asc", "title:asc"})
);
```

### Accessible methods from the `SearchResult` instance
* All the getter to get the MeiliSearch fields returned by MeiliSearch

```
Search result =  index.search(
  new SearchRequest("of")
  );
  
  int offSet = result.getOffset();
  
  int limit = result.getLimit();
  
  int nbHits = result.getNbHits();
  
  Object facetsDistribution = result.getFacetsDistribution();
  
  int processingTime = result.getProcessingTimeMs();
  
  String query = result.getQuery();
```
* Others:

`toString()`: convert to String

`isExhaustiveNbHits()`: return A true or false value indicating whether the count is exact (`true`) or approximate (`false`)

`isExhaustiveFacetsCount()`: is `false` when the search matches contain too many different values for the given facetNames. In this case, MeiliSearch stops the distribution count to prevent slowing down the request.


Raw Search
=========
### Parameters
Similar to [search()](#search)

### Return
The `rawSearch()` will return a String of raw information returned by the MeiliSearch server in an Array. No class instance will be created.

`rawSearch()` is a method of the `Search` class

```
    public String rawSearch(SearchRequest sr) throws Exception {
        return this.search.rawSearch(this.uid, sr);
    }
```



### Usage
```
import com.meilisearch.sdk.SearchRequest;

// ...

String results = index.rawSearch("movie_id","of");
```
#### Output
```
[{
  "movie_id":3,
  "title":"Life of Pi",
  "genre":["Adventure","Drama"],
  "_formatted":{
    "movie_id":3,
    "title":"Life <em>of</em> Pi",
    "genre":["Adventure","Drama"]
  },
  "_matchesInfo":{
    "title":[{
      "start":5,
      "length":2
    }]
  }
}]

```
