# ---------- Stage 1: Build JAR ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ---------- Stage 2: Run Spring Boot App ----------
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/cms-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["sh", "-c", "sleep 10 && java -jar app.jar"]

