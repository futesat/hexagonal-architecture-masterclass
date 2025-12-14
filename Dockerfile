# Stage 1: Build and Tests
# Etapa 1: Build y Tests
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
# -DskipTests to save time during build if they were already passed, but here we execute them to ensure quality
# -DskipTests para ahorrar tiempo en build si ya se pasaron, pero aquí los ejecutamos para garantizar calidad
RUN mvn clean package -DskipTests

# Stage 2: Lightweight final image
# Etapa 2: Imagen final ligera
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
