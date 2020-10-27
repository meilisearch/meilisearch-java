<p align="center">
  <img src="https://raw.githubusercontent.com/meilisearch/integration-guides/master/assets/logos/logo.svg" alt="MeiliSearch Java" width="200" height="200" />
</p>

<h1 align="center">MeiliSearch Java</h1>

<h4 align="center">
  <a href="https://github.com/meilisearch/MeiliSearch">MeiliSearch</a> |
  <a href="https://docs.meilisearch.com">Documentation</a> |
  <a href="https://www.meilisearch.com">Website</a> |
  <a href="https://blog.meilisearch.com">Blog</a> |
  <a href="https://twitter.com/meilisearch">Twitter</a> |
  <a href="https://docs.meilisearch.com/faq">FAQ</a>
</h4>

<p align="center">
  <a href="https://github.com/meilisearch/meilisearch-java/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-MIT-informational" alt="License"></a>
  <a href="https://slack.meilisearch.com"><img src="https://img.shields.io/badge/slack-MeiliSearch-blue.svg?logo=slack" alt="Slack"></a>
  <a href="https://github.com/meilisearch/MeiliSearch/discussions" alt="Discussions"><img src="https://img.shields.io/badge/github-discussions-red" /></a>
</p>

<p align="center">⚡ The MeiliSearch API client written for Java</p>

**MeiliSearch Java** is the MeiliSearch API client for Java developers. **MeiliSearch** is a powerful, fast, open-source, easy to use and deploy search engine. Both searching and indexing are highly customizable. Features such as typo-tolerance, filters, facets and synonyms are provided out-of-the-box.

### ⚠️ Important!: this is WIP, and not available for production ⚠️

## Table of Contents <!-- omit in toc -->

- [📖 Documentation](#-documentation)
- [🔧 Installation](#-installation)
- [🚀 Getting Started](#-getting-started)
- [🤖 Compatibility with MeiliSearch](#-compatibility-with-meilisearch)
- [💡 Learn More](#-learn-more)
- [⚙️ Development Workflow and Contributing](#️-development-workflow-and-contributing)

## 📖 Documentation

See our [Documentation](https://docs.meilisearch.com/guides/introduction/quick_start_guide.html) or our [API References](https://docs.meilisearch.com/references/).


## 🔧 Installation

// TODO:


## 🚀 Getting Started

#### Add documents <!-- omit in toc -->

```java
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;

public class Main {

	public static void main(String[] args) throws Exception {

		final String documents = "["
			+ "{\"book_id\": 123, \"title\": \"Pride and Prejudice\"},"
			+ "{\"book_id\": 456, \"title\": \"Le Petit Prince\"},"
			+ "{\"book_id\": 1, \"title\": \"Alice In Wonderland\"},"
			+ "{\"book_id\": 1344, \"title\": \"The Hobbit\"},"
			+ "{\"book_id\": 4, \"title\": \"Harry Potter and the Half-Blood Prince\"},"
			+ "{\"book_id\": 2, \"title\": \"The Hitchhiker\'s Guide to the Galaxy\"}"
			+ "]";

		Client client = new Client(new Config("http://localhost:7700", "masterKey"));
		Index index = client.createIndex("books"); // If your index does not exist
		Index index = client.getIndex("books"); // If you already created your index

		index.addDocuments(documents);
	}

}
```

#### Basic Search <!-- omit in toc -->

```java
	// MeiliSearch is typo-tolerant:
	String results = index.search("harry pottre");
	System.out.println(results);
```

Output:
```json
{
	"hits": [{
		"book_id": 4,
		"title": "Harry Potter and the Half-Blood Prince"
	}],
	"offset": 0,
	"limit": 20,
	"nbHits": 1,
	"exhaustiveNbHits": false,
	"processingTimeMs": 2,
	"query": "harry pottre"
}
```

#### Custom Search <!-- omit in toc -->

// TODO:

## 🤖 Compatibility with MeiliSearch

This package only guarantees the compatibility with the [version v0.15.0 of MeiliSearch](https://github.com/meilisearch/MeiliSearch/releases/tag/v0.15.0).

## 💡 Learn More

The following sections may interest you:

- **Manipulate documents**: see the [API references](https://docs.meilisearch.com/references/documents.html) or read more about [documents](https://docs.meilisearch.com/guides/main_concepts/documents.html).
- **Search**: see the [API references](https://docs.meilisearch.com/references/search.html) or follow our guide on [search parameters](https://docs.meilisearch.com/guides/advanced_guides/search_parameters.html).
- **Manage the indexes**: see the [API references](https://docs.meilisearch.com/references/indexes.html) or read more about [indexes](https://docs.meilisearch.com/guides/main_concepts/indexes.html).
- **Configure the index settings**: see the [API references](https://docs.meilisearch.com/references/settings.html) or follow our guide on [settings parameters](https://docs.meilisearch.com/guides/advanced_guides/settings.html).

## ⚙️ Development Workflow and Contributing

Any new contribution is more than welcome in this project!

If you want to know more about the development workflow or want to contribute, please visit our [contributing guidelines](/CONTRIBUTING.md) for detailed instructions!

<hr>

**MeiliSearch** provides and maintains many **SDKs and Integration tools** like this one. We want to provide everyone with an **amazing search experience for any kind of project**. If you want to contribute, make suggestions, or just know what's going on right now, visit us in the [integration-guides](https://github.com/meilisearch/integration-guides) repository.
