# 1. Use a lightweight Java 17 image
FROM eclipse-temurin:17-jdk-alpine

# 2. Set the working directory inside the container
WORKDIR /app

# 3. Copy the compiled JAR file from your target folder into the container
COPY target/*.jar app.jar

# 4. Tell Docker which port the app runs on
EXPOSE 8080

# 5. The command to run your app
ENTRYPOINT ["java", "-jar", "app.jar"]