FROM eclipse-temurin:17-jdk

WORKDIR /danayaspace

# Copy the JAR file
COPY target/*.jar danayaspace-1.0-SNAPSHOT.jar

# Copy the application-prod.yml (make sure it's in the same folder as your Dockerfile)
COPY /src/resources/config/application-prod.yml /danayaspace/application-prod.yml

# Set environment variable to specify the Spring profile
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

# Run the application with the correct profile
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "danayaspace-1.0-SNAPSHOT.jar"]