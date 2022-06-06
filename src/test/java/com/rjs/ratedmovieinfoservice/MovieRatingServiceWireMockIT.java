package com.rjs.ratedmovieinfoservice;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

class MovieRatingServiceWireMockIT {

    @RegisterExtension
    static WireMockExtension movieRatingsServiceMock = WireMockExtension.newInstance()
            .options(wireMockConfig().port(8083))
            .build();

    @Test
    void wireMockMovieRatingService() {

        movieRatingsServiceMock.stubFor(get(urlEqualTo("/movieRatings/joe"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"movieId\":\"Jaws\",\"rating\":4},{\"movieId\":\"Star Wars\",\"rating\":3}]")));

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<MovieRating[]> responseEntity = restTemplate.getForEntity("http://localhost:8083/movieRatings/{userId}",
                MovieRating[].class, "joe");


        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Collection<MovieRating> expectedMovieRatings =
                Arrays.asList(new MovieRating("Jaws", 4), new MovieRating("Star Wars", 3));
        List<MovieRating> movieRatings = Arrays.asList(responseEntity.getBody());
        Assertions.assertThat(movieRatings).isEqualTo(expectedMovieRatings);
    }
}