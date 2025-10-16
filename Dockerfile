# First stage: Build the multi-module Maven project
FROM maven:3.8.7-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .

# Build only the spring-boot-example module
RUN mvn -pl spring-boot-example -am package -DskipTests

# Second stage: Run the built Spring Boot app
FROM eclipse-temurin:17-jre

WORKDIR /app
ENV OPENAI_API_KEY=${OPENAI_API_KEY}
# Copy the final JAR from the module's target directory
COPY --from=build /app/spring-boot-example/target/spring-boot-example-1.5.0-beta11.jar app.jar

EXPOSE 8080
# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
