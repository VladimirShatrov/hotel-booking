FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build/api-gateway
COPY services/api-gateway/pom.xml .
RUN mvn dependency:go-offline -B
COPY services/api-gateway/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /build/api-gateway/target/api-gateway-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]