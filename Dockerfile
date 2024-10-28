# Stage 1: Build
FROM openjdk:21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy Maven wrapper and pom.xml into the container
COPY . .

# Download dependencies to speed up the build process
RUN ./mvnw dependency:go-offline

# Copy the source code into the container
COPY . .

# Build the Spring Boot application
RUN ./mvnw clean package -DskipTests

# Stage 2: Run
FROM openjdk:21

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port that the Spring Boot application runs on
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
