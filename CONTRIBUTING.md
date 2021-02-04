# Contributing

First of all, thank you for contributing to MeiliSearch! The goal of this document is to provide everything you need to know in order to contribute to MeiliSearch and its different integrations.

<!-- MarkdownTOC autolink="true" style="ordered" indent="   " -->

- [Assumptions](#assumptions)
- [How to Contribute](#how-to-contribute)
- [Development Workflow](#development-workflow)
- [Git Guidelines](#git-guidelines)
- [Release Process (for internal team only)](#release-process-for-internal-team-only)

<!-- /MarkdownTOC -->

## Assumptions

1. **You're familiar with [GitHub](https://github.com) and the [Pull Request](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/about-pull-requests)(PR) workflow.**
2. **You've read the MeiliSearch [documentation](https://docs.meilisearch.com) and the [README](/README.md).**
3. **You know about the [MeiliSearch community](https://docs.meilisearch.com/learn/what_is_meilisearch/contact.html). Please use this for help.**

## How to Contribute

1. Make sure that the contribution you want to make is explained or detailed in a GitHub issue! Find an [existing issue](https://github.com/meilisearch/meilisearch-java/issues/) or [open a new one](https://github.com/meilisearch/meilisearch-java/issues/new).
2. Once done, [fork the meilisearch-java repository](https://help.github.com/en/github/getting-started-with-github/fork-a-repo) in your own GitHub account. Ask a maintainer if you want your issue to be checked before making a PR.
3. [Create a new Git branch](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-and-deleting-branches-within-your-repository).
4. Review the [Development Workflow](#workflow) section that describes the steps to maintain the repository.
5. Make the changes on your branch.
6. [Submit the branch as a PR](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request-from-a-fork) pointing to the `master` branch of the main meilisearch-java repository. A maintainer should comment and/or review your Pull Request within a few days. Although depending on the circumstances, it may take longer.<br>
 We do not enforce a naming convention for the PRs, but **please use something descriptive of your changes**, having in mind that the title of your PR will be automatically added to the next [release changelog](https://github.com/meilisearch/meilisearch-java/releases/).

## Development Workflow

### Setup

```bash
$ ./gradlew install
```

### Tests and linter

Integration and unit tests will be run in your PR to check everything is OK. Each PR should pass all the tests to be accepted.

To run the unit tests in your local environment, use:

```bash
$ ./gradlew test
```

You can also launch the integration tests, which run against a local MeiliSearch instance. To make it run in your local environment, use:

```bash
$ docker pull getmeili/meilisearch:latest # Fetch the latest version of MeiliSearch image from Docker Hub
$ docker run -p 7700:7700 getmeili/meilisearch:latest ./meilisearch --master-key=masterKey --no-analytics=true
$ ./gradlew test IntegrationTest
```

⚠️ No linter has been set for the moment, but please try to keep the code clean and tidy!

## Git Guidelines

### Git Branches

All changes must be made in a branch and submitted as PR.
We do not enforce any branch naming style, but please use something descriptive of your changes.

### Git Commits

As minimal requirements, your commit message should:
- be capitalized
- not finish by a dot or any other punctuation character (!,?)
- start with a verb so that we can read your commit message this way: "This commit will ...", where "..." is the commit message.
  e.g.: "Fix the home page button" or "Add more tests for create_index method"

We don't follow any other convention, but if you want to use one, we recommend [this one](https://chris.beams.io/posts/git-commit/).

### GitHub Pull Requests

Some notes on GitHub PRs:

- [Convert your PR as a draft](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/changing-the-stage-of-a-pull-request) if your changes are a work in progress: no one will review it until you pass your PR as ready for review.<br>
  The draft PR can be very useful if you want to show that you are working on something and make your work visible.
- The branch related to the PR must be **up-to-date with `master`** before merging. Fortunately, this project [integrates a bot](https://github.com/meilisearch/integration-guides/blob/master/guides/bors.md) to automatically enforce this requirement without the PR author having to do it manually.
- All PRs must be reviewed and approved by at least one maintainer.
- The PR title should be accurate and descriptive of the changes. The title of the PR will be indeed automatically added to the next [release changelogs](https://github.com/meilisearch/meilisearch-java/releases/).

## Release Process (for internal team only)

MeiliSearch tools follow the [Semantic Versioning Convention](https://semver.org/).

### Automation to Rebase and Merge the PRs

This project integrates a bot that helps us manage pull requests merging.<br>
_[Read more about this](https://github.com/meilisearch/integration-guides/blob/master/guides/bors.md)._

### Automated Changelogs

This project integrates a tool to create automated changelogs.<br>
_[Read more about this](https://github.com/meilisearch/integration-guides/blob/master/guides/release-drafter.md)._

### How to Publish the Release

⚠️ Before doing anything, make sure you got through the guide about [Releasing an Integration Tool](https://github.com/meilisearch/integration-guides/blob/master/guides/integration-tool-release.md).

#### Create signature credentials (first time)

⚠️ All these steps (create and publish a GPG key) have already been done by the Meili team and the key is shared internally. Please ask a maintainer to get the credentials if needed.

Steps:

1. Install `gpg`

```bash
$ sudo apt install gnupg
```

or

```bash
$ brew install gpg
```

2. Create a `genkey` file

```
Key-Type: 1
Key-Length: 4096
Subkey-Type: 1
Subkey-Length: 4096
Name-Real: <your-name>
Name-Email: <your-email>
Expire-Date: 0
Passphrase: <your-passphrase>
```

3. Create a gpg key

```bash
# May need sudo privilege
$ gpg --gen-key --batch genkey
```

4. Publish your public key to a public repository:

```bash
$ gpg --keyserver hkp://pool.sks-keyservers.net --send-keys <last-8-digits-of-your-key-hash>
```

#### Update the version

Make a PR modifying the following files with the right version:

- [`build.gradle`](/build.gradle)

```java
version = 'X.X.X'
```

- [`README.md`](/README.md) in the `Installation` section

```xml
<version>X.X.X</version>
```

```groovy
implementation 'com.meilisearch.sdk:meilisearch-java:X.X.X'
```

Once the changes are merged on `master`, you can publish the current draft release via the [GitHub interface](https://github.com/meilisearch/meilisearch-java/releases).

#### Sign your files and upload to Maven Repository

1. Prepare the environment by filling the `gradle.properties` file with all the credentials:

```
ossrhUsername=<maven-username>
ossrhPassword=<maven-password>

signing.gnupg.executable=gpg
signing.gnupg.keyName=<email-associated-to-the-gpg-key>
signing.gnupg.passphrase=<passphrase-associated-to-the-gpg-key>
```

2. Sign your files and upload them to Maven repository:

```bash
# May need sudo privilege
$ ./gradlew build -P releaseSDK
$ ./gradlew uploadArchive -P releaseSDK
```

3. Login to [Sonatype Nexus](https://oss.sonatype.org).
4. Navigate to `Staging repositories`.
5. Close your repository by clicking on the `Close` button. Checks will be made by nexus. It might take time. If any error occurs, it will be visible in the `Activity` tab.
6. Once the check have succeeded, you should be able to click on the `Release` button. The release will be now processed and might take a long time to appear in [Maven Central](https://search.maven.org/artifact/com.meilisearch.sdk/meilisearch-java).

<hr>

Thank you again for reading this through, we can not wait to begin to work with you if you made your way through this contributing guide ❤️
