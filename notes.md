# Movie Info Service Notes

## make the executable jar file
* ./mvnw package

## run the jar file from the command line
java -jar target/rated-movie-info-service-0.0.1-SNAPSHOT.jar

## build a docker image which runs the executable jar file
docker build -t rated-movie-info-service-image .

## run the image in a container, and run its dependencies.
* docker compose up

## test the application (for movie found)
* curl http://localhost:8081/ratedMovieInfos/joe

## test the application (for movie not found)
* curl http://localhost:8081/ratedMovieInfos/foo
