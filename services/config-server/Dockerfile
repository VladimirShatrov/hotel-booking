FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=git
ENV SPRING_CLOUD_CONFIG_SERVER_GIT_URI=https://github.com/portner-boop/kon-repo-proj

COPY --from=build /app/target/config-server-0.0.1-SNAPSHOT.jar /app/config-server.jar
ENTRYPOINT ["java", "-jar", "/app/config-server.jar"]
EXPOSE 8888