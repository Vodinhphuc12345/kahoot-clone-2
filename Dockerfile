FROM java:8
FROM maven:3.6.0-jdk-11-slim AS build

WORKDIR /app/kahoot-clone

COPY . /app/kahoot-clone

RUN mvn clean install -DskipTests
#
ENTRYPOINT ["java","-Dspring.profiles.active=docker", "-jar", "/app/kahoot-clone/target/kahootClone-0.0.1-SNAPSHOT.jar"]
