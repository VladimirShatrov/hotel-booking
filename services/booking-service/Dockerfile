FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build/shared
COPY services/shared/pom.xml .
COPY services/shared/src ./src
RUN mvn install -DskipTests

WORKDIR /build/booking-service
COPY services/booking-service/pom.xml .
RUN mvn dependency:go-offline -B
COPY services/booking-service/src ./src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /build/booking-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]