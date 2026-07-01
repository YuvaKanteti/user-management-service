# Use Java 21 Runtime
FROM eclipse-temurin:21-jre

# Create working directory
WORKDIR /app

# Copy the Spring Boot JAR
COPY build/libs/user-management-service-0.0.1-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8085

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]