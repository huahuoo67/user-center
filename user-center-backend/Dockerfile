# syntax=docker/dockerfile:1.7

FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY settings-docker.xml /root/.m2/settings.xml
COPY pom.xml .

# Maven 依赖独立成一层，并持久复用本机 BuildKit 缓存。
RUN --mount=type=cache,id=user-center-maven,target=/root/.m2/repository \
    mvn -B dependency:go-offline -Dmaven.test.skip=true

COPY src/main ./src/main

RUN --mount=type=cache,id=user-center-maven,target=/root/.m2/repository \
    mvn -B package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/user-center-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
