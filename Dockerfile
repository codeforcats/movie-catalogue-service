FROM amazoncorretto:17-alpine
COPY target/movie-catalogue-service-0.0.1-SNAPSHOT.jar movie-catalogue-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/movie-catalogue-service-0.0.1-SNAPSHOT.jar"]