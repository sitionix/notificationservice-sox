# syntax=docker/dockerfile:1.7
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY boot/pom.xml boot/pom.xml
COPY domain/pom.xml domain/pom.xml
COPY application/pom.xml application/pom.xml
COPY pipe/pom.xml pipe/pom.xml
COPY pipe/pipe-consumer-email-verify/pom.xml pipe/pipe-consumer-email-verify/pom.xml
COPY clients/pom.xml clients/pom.xml
COPY clients/client-athssox/pom.xml clients/client-athssox/pom.xml
COPY clients/client-bffssox/pom.xml clients/client-bffssox/pom.xml
COPY jacoco-report/pom.xml jacoco-report/pom.xml
COPY boot/src boot/src
COPY domain/src domain/src
COPY application/src application/src
COPY pipe/pipe-consumer-email-verify/src pipe/pipe-consumer-email-verify/src
COPY clients/client-athssox/src clients/client-athssox/src
COPY clients/client-bffssox/src clients/client-bffssox/src

RUN --mount=type=secret,id=maven_settings,target=/root/.m2/settings.xml \
    mvn -pl boot -am -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/boot/target/boot-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
