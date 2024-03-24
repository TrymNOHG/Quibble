# Stage 1: Build the application
FROM maven:3.9.6-amazoncorretto-17 as builder

# Copy the project files to the container
COPY . /usr/src/app

# Set the working directory
WORKDIR /usr/src/app

# Use Maven to build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17

# Copy the built application from the builder stage
COPY --from=builder /usr/src/app/target/*.jar app.jar

# Expose the application's port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
#ENTRYPOINT ["mvn", "spring-boot:run"]

