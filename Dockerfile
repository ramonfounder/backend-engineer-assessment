# Stage 1: Build the application
FROM gradle:jdk21 AS builder

WORKDIR /app

# Copy Gradle files
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Copy application source code
COPY src ./src

# Build the application
RUN ./gradlew bootJar

# it should be used slim version of the image to reduce the size.
# also we could use a library to integrate with Gradle to build the image autmaticaly.
# Stage 2: Run the application
FROM azul/zulu-openjdk:21

WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port your application listens on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]