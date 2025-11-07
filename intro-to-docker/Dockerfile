FROM amazoncorretto:17

LABEL version="1.0"

EXPOSE 8081:8081

WORKDIR /app

COPY target/intro-to-docker-0.0.1-SNAPSHOT.jar /app/intro-to-docker.jar

ENTRYPOINT ["java", "-jar", "intro-to-docker.jar"]