package com.rjs.ratedmovieinfo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class MovieRatingProvider {

    @Value("${movie.rating.service.url}")
    private String movieRatingsUrl;

    public Optional<Collection<MovieRating>> getMovieRatings(String userId){


        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List<MovieRating>> responseEntity =
                    restTemplate.exchange(movieRatingsUrl + "/{userId}",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }, userId);
            return Optional.of(responseEntity.getBody());
        }
        catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }
}
