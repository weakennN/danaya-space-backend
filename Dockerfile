# Stage 1: Build the application using Maven
FROM maven:3.9.5-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jdk
WORKDIR /danayaspace

# Copy JAR from builder stage
COPY --from=builder /app/target/*.jar danayaspace-1.0-SNAPSHOT.jar

# Copy production configuration
COPY /src/resources/config/application-prod.yml /danayaspace/application-prod.yml

# Set environment to use 'prod' profile
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application
ENTRYPOINT ["java", "-jar", "danayaspace-1.0-SNAPSHOT.jar", "-Dspring.profiles.active=prod"]
