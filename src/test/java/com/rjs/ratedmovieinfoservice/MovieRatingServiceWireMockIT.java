package com.rjs.ratedmovieinfoservice;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest
class MovieRatingServiceWireMockIT {

    @RegisterExtension
    static WireMockExtension movieRatingsServiceMock = WireMockExtension.newInstance()
            .options(wireMockConfig().port(8083))
            .build();

    @Value("${movie.rating.service.url}")
    private String movieRatingServiceUrl;

    @Test
    void getMovieRatingsShouldReturnPopulatedListWhenResourceFound() {

        movieRatingsServiceMock.stubFor(get(urlEqualTo("/movieRatings/joe"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"movieId\":\"Jaws\",\"rating\":4},{\"movieId\":\"Star Wars\",\"rating\":3}]")));

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<MovieRating[]> responseEntity = restTemplate.getForEntity(movieRatingServiceUrl + "/" + "{userId}",
                MovieRating[].class, "joe");


        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Collection<MovieRating> expectedMovieRatings =
                Arrays.asList(new MovieRating("Jaws", 4), new MovieRating("Star Wars", 3));
        List<MovieRating> movieRatings = Arrays.asList(responseEntity.getBody());
        Assertions.assertThat(movieRatings).isEqualTo(expectedMovieRatings);
    }

    @Test ()
    void getMovieRatingsShouldReturnEmptyListWhenNoResourceFound()
    {
        movieRatingsServiceMock.stubFor(get(urlEqualTo("/movieRatings/foo"))
                .willReturn(aResponse()
                        .withStatus(404)));

        RestTemplate restTemplate = new RestTemplate();

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                HttpClientErrorException.NotFound.class,
                ()-> restTemplate.getForEntity(movieRatingServiceUrl + "/" + "{userId}",
                MovieRating[].class, "foo"));


        Assertions.assertThat(exception.getMessage().equals("404 NotFound"));

    }
}