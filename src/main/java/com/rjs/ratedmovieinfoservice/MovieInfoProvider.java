package com.rjs.ratedmovieinfoservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class MovieInfoProvider {

    @Value("${movie.info.service.url}")
    private String movieInfoUrl;

    public Optional<MovieInfo> getMovieInfo(String movieId){

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<MovieInfo> responseEntity = restTemplate.getForEntity(movieInfoUrl +"/{movieId}",
                MovieInfo.class, movieId);

        if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Optional.empty();
        }
        else {
            return Optional.of(responseEntity.getBody());
        }
    }
}
