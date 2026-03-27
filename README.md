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
  <version>0.20.0</version>
  <type>pom</type>
</dependency>
```

### Gradle <!-- omit in toc -->

Add the following line to the `dependencies` section of your `build.gradle`:

```gradle
implementation 'com.meilisearch.sdk:meilisearch-java:0.20.0'
```

## 🚀 Getting started

To get started, you first need to initialize the Meilisearch client.

```java
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;

Client client = new Client(new Config("http://localhost:7700", "masterKey"));
```

## ⚙️ Contributing

Contributions are welcome! If you want to contribute to this project, please check our [contributing guide](CONTRIBUTING.md). You can help by reporting bugs, suggesting new features, or submitting pull requests to improve the codebase.