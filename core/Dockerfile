FROM gradle:8.11.1-jdk21 AS build
WORKDIR /stock-service

COPY build.gradle settings.gradle gradlew /stock-service/
COPY gradle /stock-service/gradle/

COPY core /stock-service/core/
COPY stock-domain /stock-service/stock-domain/

WORKDIR /stock-service/core

RUN chmod +x ../gradlew
RUN ../gradlew :core:clean build

FROM openjdk:21-jdk

WORKDIR /stock-service

COPY --from=build /stock-service/core/build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/stock-service/app.jar"]