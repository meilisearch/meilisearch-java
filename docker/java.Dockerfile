FROM azul/zulu-openjdk-alpine:17

# GNU xargs is required by the Gradle wrapper. Bash runs lint and Compose commands.
RUN apk add --no-cache bash ca-certificates findutils git

WORKDIR /home/package

CMD ["bash"]
