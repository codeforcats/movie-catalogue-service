# Movie Info Service No

## make the executable jar file
* ./mvnw package

## run jar file from the command line
java -jar target/rated-movie-info-service-0.0.1-SNAPSHOT.jar

## build a docker image which runs the executable jar file
docker build -t rated-movie-info-service-image .

## run the image (MORE WORK NEEDED ON THIS -- NEED TO THINK ABOUT networks)
* docker run --name rated-movie-info-service-container -d -p 8081:8081 rated-movie-info-service-image 

## test the application (for movie found)
* curl http://localhost:8081/ratedMovieInfos/joe

## test the application (for movie not found)
* curl http://localhost:8081/ratedMovieInfos/foo
