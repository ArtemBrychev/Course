FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/analytics-0.0.1-SNAPSHOT.jar appanal.jar

ENTRYPOINT ["java", "-jar", "appanal.jar"]