# Movie Info Service Notes

## make the executable jar file
* ./mvnw package

## then, EITHER
## run the jar file from the command line
java -jar target/rated-movie-info-service-0.0.1-SNAPSHOT.jar

## OR
## first build a docker image which runs the executable jar file
docker build -t rated-movie-info-service-image .

## and then run the image in a container, and run its dependencies in their containers, 
## all on the same docker host. 
* docker compose up

## REGARDLESS of running in Docker or not
## acceptance test 1  (for movie found)
* curl http://localhost:8081/ratedMovieInfos/joe

## acceptance test 2  (for movie not found)
* curl http://localhost:8081/ratedMovieInfos/foo
