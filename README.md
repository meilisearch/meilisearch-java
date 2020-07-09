# meilisearch-java

**[Important!: this is WIP, and not available for production]**

Java client for MeiliSearch.

MeiliSearch provides an ultra relevant and instant full-text search. Our solution is open-source and you can check out [our repository here](https://github.com/meilisearch/MeiliSearch).

Here is the [MeiliSearch documentation](https://docs.meilisearch.com/) 📖


## Installation

// TODO:


## Getting started

#### Quickstart
```java
import meilisearch.Config;
import meilisearch.Indexes;
import meilisearch.Client;

public class TestApp {

    public static void main(String[] args) throws Exception {
        Client ms = new Client(new Config("http://localhost:7700", ""));
       
        // create new index with primary key(optional)
        ms.createIndex("books", "books_id");
        
        Indexes book = ms.getIndex("books");
        
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("1111", "alice in wonderland");
        jsonArray.add(jsonObject);

        // add new document "[{"1111": "alice in wonderland"}]"
        String response = book.addDocument(jsonObject.toString());

        // response : "{ "updateId": 0 }"
    }
}
```
