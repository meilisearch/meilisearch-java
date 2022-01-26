# Contributing <!-- omit in TOC -->

First of all, thank you for contributing to Meilisearch! The goal of this document is to provide everything you need to know in order to contribute to Meilisearch and its different integrations.

- [Assumptions](#assumptions)
- [How to Contribute](#how-to-contribute)
- [Development Workflow](#development-workflow)
- [Git Guidelines](#git-guidelines)
- [Release Process (for internal team only)](#release-process-for-internal-team-only)

## Assumptions

1. **You're familiar with [GitHub](https://github.com) and the [Pull Request](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/about-pull-requests)(PR) workflow.**
2. **You've read the Meilisearch [documentation](https://docs.meilisearch.com) and the [README](/README.md).**
3. **You know about the [Meilisearch community](https://docs.meilisearch.com/learn/what_is_meilisearch/contact.html). Please use this for help.**

## How to Contribute

1. Make sure that the contribution you want to make is explained or detailed in a GitHub issue! Find an [existing issue](https://github.com/meilisearch/meilisearch-java/issues/) or [open a new one](https://github.com/meilisearch/meilisearch-java/issues/new).
2. Once done, [fork the meilisearch-java repository](https://help.github.com/en/github/getting-started-with-github/fork-a-repo) in your own GitHub account. Ask a maintainer if you want your issue to be checked before making a PR.
3. [Create a new Git branch](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-and-deleting-branches-within-your-repository).
4. Review the [Development Workflow](#development-workflow) section that describes the steps to maintain the repository.
5. Make the changes on your branch.
6. [Submit the branch as a PR](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request-from-a-fork) pointing to the `main` branch of the main meilisearch-java repository. A maintainer should comment and/or review your Pull Request within a few days. Although depending on the circumstances, it may take longer.<br>
 We do not enforce a naming convention for the PRs, but **please use something descriptive of your changes**, having in mind that the title of your PR will be automatically added to the next [release changelog](https://github.com/meilisearch/meilisearch-java/releases/).

## Development Workflow

### Tests <!-- omit in TOC -->

Integration and unit tests will be run in your PR to check everything is OK. Each PR should pass all the tests to be accepted.

To run the unit tests in your local environment, use:

```bash
./gradlew test
```

You can also launch the integration tests, which run against a local Meilisearch instance. To make it run in your local environment, use:

```bash
curl -L https://install.meilisearch.com | sh # download Meilisearch
./meilisearch --master-key=masterKey --no-analytics=true # run Meilisearch
./gradlew test IntegrationTest
```

### Linter <!-- omit in TOC -->

Run:

```bash
bash ./scripts/lint.sh
```

## Git Guidelines

### Git Branches <!-- omit in TOC -->

All changes must be made in a branch and submitted as PR.
We do not enforce any branch naming style, but please use something descriptive of your changes.

### Git Commits <!-- omit in TOC -->

As minimal requirements, your commit message should:
- be capitalized
- not finish by a dot or any other punctuation character (!,?)
- start with a verb so that we can read your commit message this way: "This commit will ...", where "..." is the commit message.
  e.g.: "Fix the home page button" or "Add more tests for create_index method"

We don't follow any other convention, but if you want to use one, we recommend [this one](https://chris.beams.io/posts/git-commit/).

### GitHub Pull Requests <!-- omit in TOC -->

Some notes on GitHub PRs:

- [Convert your PR as a draft](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/changing-the-stage-of-a-pull-request) if your changes are a work in progress: no one will review it until you pass your PR as ready for review.<br>
  The draft PR can be very useful if you want to show that you are working on something and make your work visible.
- The branch related to the PR must be **up-to-date with `main`** before merging. Fortunately, this project [integrates a bot](https://github.com/meilisearch/integration-guides/blob/main/resources/bors.md) to automatically enforce this requirement without the PR author having to do it manually.
- All PRs must be reviewed and approved by at least one maintainer.
- The PR title should be accurate and descriptive of the changes. The title of the PR will be indeed automatically added to the next [release changelogs](https://github.com/meilisearch/meilisearch-java/releases/).

## Release Process (for internal team only)

Meilisearch tools follow the [Semantic Versioning Convention](https://semver.org/).

### Automation to Rebase and Merge the PRs <!-- omit in TOC -->

This project integrates a bot that helps us manage pull requests merging.<br>
_[Read more about this](https://github.com/meilisearch/integration-guides/blob/main/resources/bors.md)._

### Automated Changelogs <!-- omit in TOC -->

This project integrates a tool to create automated changelogs.<br>
_[Read more about this](https://github.com/meilisearch/integration-guides/blob/main/resources/release-drafter.md)._

### How to Publish the Release <!-- omit in TOC -->

⚠️ Before doing anything, make sure you got through the guide about [Releasing an Integration](https://github.com/meilisearch/integration-guides/blob/main/resources/integration-release.md).

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

- [.code-samples.meilisearch.yaml](/.code-samples.meilisearch.yaml) in the `getting_started_add_documents_md` field

```groovy
implementation 'com.meilisearch.sdk:meilisearch-java:X.X.X'
```

Once the changes are merged on `main`, you can publish the current draft release via the [GitHub interface](https://github.com/meilisearch/meilisearch-java/releases): on this page, click on `Edit` (related to the draft release) > update the description (be sure you apply [these recommandations](https://github.com/meilisearch/integration-guides/blob/main/resources/integration-release.md#writting-the-release-description)) > when you are ready, click on `Publish release`.

A GitHub Action will be triggered and publish a new release to Maven repository.

### How to Manually Publish the Release to Maven repository  <!-- omit in TOC -->

⚠️ These following steps should only be applied if it's impossible to release the current version in the Maven repository via the CI. Keep in mind publishing via the CI should always be privileged.

#### Create signature credentials (first time) <!-- omit in TOC -->

⚠️ These step (create and publish a GPG key) has already been done by the Meili team and the key is shared internally. Please ask a maintainer to get the credentials if needed.

Steps:

1. Install `gpg`

```bash
sudo apt install gnupg
```

or

```bash
brew install gpg
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
gpg --gen-key --batch genkey
```

4. Publish your public key to a public repository:

```bash
gpg --keyserver hkp://keyserver.ubuntu.com --send-keys <last-8-digits-of-your-key-hash>
```

5. Export the gpg key in a secring.gpg file

```bash
gpg --keyring secring.gpg --export-secret-keys > ~/.gnupg/secring.gpg
```

6. Encode the secring in base64 and store it (Used by the ossrh-publish workflow)

```bash
base64 ~/.gnupg/secring.gpg > secring.gpg.b64
```

#### Sign your files and upload to Maven Repository manually <!-- omit in TOC -->

1. Set the environment variables listed below with the required credentials:

```bash
export OSSRH_USERNAME=<maven-username>
export OSSRH_PASSWORD=<maven-password>

export SIGNINT_KEY_ID=<id-associated-to-the-gpg-key>
export SIGNING_PASSWORD=<passphrase-associated-to-the-gpg-key>
export SIGNING_SECRET_KEY_RING_FILE=<path-to-gpg-key-encoded-in-base64>
```

2. Decode the gpg key

```bash
base64 -d $SIGNING_SECRET_KEY_RING_FILE > secring.gpg
```

3. Build, sign your files and upload them to Maven repository:

```bash
# May need sudo privilege and JDK8
./gradlew build
./gradlew publish -Psigning.keyId=$SIGNING_KEY_ID -Psigning.password=$SIGNING_PASSWORD -Psigning.secretKeyRingFile=$(echo secring.gpg)
```

4. Login to [Sonatype Nexus](https://oss.sonatype.org).
5. Navigate to `Staging repositories`.
6. Close your repository by clicking on the `Close` button. Checks will be made by nexus. It might take time. If any error occurs, it will be visible in the `Activity` tab.
7. Once the check have succeeded, you should be able to click on the `Release` button. The release will be now processed and might take a long time to appear in [Maven Central](https://search.maven.org/artifact/com.meilisearch.sdk/meilisearch-java).

<hr>

Thank you again for reading this through, we can not wait to begin to work with you if you made your way through this contributing guide ❤️
