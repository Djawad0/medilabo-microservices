FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/info-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8081
