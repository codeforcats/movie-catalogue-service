package com.rjs.ratedmovieinfoservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class MovieInfoProvider {
    public Optional<MovieInfo> getMovieInfo(String movieId){

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<MovieInfo> responseEntity = restTemplate.getForEntity("http://localhost:8082/movies/{movieId}",
                MovieInfo.class, movieId);

        if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Optional.empty();
        }
        else {
            return Optional.of(responseEntity.getBody());
        }
    }
}
