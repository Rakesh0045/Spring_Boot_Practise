# Stage 1: Build the application using Maven
FROM maven:3.8-eclipse-temurin-17 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and download dependencies first to leverage Docker cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Package the application, skipping the tests
RUN mvn clean package -DskipTests


# Stage 2: Create the final, smaller image with only the JRE
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the packaged .jar file from the 'builder' stage
# The wildcard is used to match the jar file regardless of the version
COPY --from=builder /app/target/*.jar app.jar

# Expose the port the application runs on (default for Spring Boot is 8080)
EXPOSE 2801

# The command to run the application
# Spring Boot will automatically pick up the environment variables provided at runtime.
ENTRYPOINT ["java", "-jar", "app.jar"]