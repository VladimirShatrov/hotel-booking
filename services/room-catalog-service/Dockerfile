FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build/room-catalog-service
COPY services/room-catalog-service/pom.xml .
RUN mvn dependency:go-offline -B
COPY services/room-catalog-service/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /build/room-catalog-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]