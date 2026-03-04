FROM openjdk:17-jdk-slim

WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/app.jar

COPY src/main/resources/agiliz-logistics-6efd2-firebase-adminsdk-40nmj-7a34820fe8.json /app/agiliz-logistics-6efd2-firebase-adminsdk-40nmj-7a34820fe8.json

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]