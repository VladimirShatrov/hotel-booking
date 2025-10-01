FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/config-server-0.0.1-SNAPSHOT.jar /app/config-server.jar
ENTRYPOINT ["java", "-jar", "/app/config-server.jar"]
EXPOSE 8888