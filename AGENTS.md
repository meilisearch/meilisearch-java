# AGENTS.md

## Development

This repository provides a `docker-compose.yml` that lets you develop and run tests without installing Java locally.

To speed up local development, always use Docker Compose with the `-v gradle-cache:/root/.gradle` option to **persist the Gradle cache across Docker runs**.

- Lint the code with `docker compose run --rm package bash -c "bash ./scripts/lint.sh"`
