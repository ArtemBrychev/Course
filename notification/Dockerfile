FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/notification-0.0.1-SNAPSHOT.jar appnotify.jar

ENTRYPOINT ["java", "-jar", "appnotify.jar"]