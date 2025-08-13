FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

ENV EUREKA_HOSTNAME=discovery-service
ENV SERVER_PORT=8761
ENV SPRING_CONFIG_IMPORT=configserver:http://localhost:8888

COPY --from=build /app/target/discovery-service-0.0.1-SNAPSHOT.jar /app/discovery-service.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app/discovery-service.jar"]
