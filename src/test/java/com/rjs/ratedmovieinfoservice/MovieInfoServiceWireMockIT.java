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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest
public class MovieInfoServiceWireMockIT {
    @RegisterExtension
    static WireMockExtension movieInfoServiceMock = WireMockExtension.newInstance()
            .options(wireMockConfig().port(8082))
            .build();

    @Value("${movie.info.service.url}")
    private String movieInfoServiceUrl;

    @Test
    void whenMovieIdIsKnownShouldReturnPopulatedMovieInfo() {

        movieInfoServiceMock.stubFor(get(urlEqualTo("/movies/Jaws"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"movieId\":\"Jaws\",\"description\":\"A man eating shark.\"}")));

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<MovieInfo> responseEntity = restTemplate.getForEntity(movieInfoServiceUrl + "/" + "{movieId}",
                MovieInfo.class, "Jaws");

        MovieInfo expectedMovieInfo = new MovieInfo("Jaws", "A man eating shark.");
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(expectedMovieInfo);
    }

    @Test
    void whenMovieIdNotKnownShouldReturnStatusNotFound() {

        movieInfoServiceMock.stubFor(get(urlEqualTo("/movies/Jaws"))
                .willReturn(aResponse().withStatus(404)));

        RestTemplate restTemplate = new RestTemplate();

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                HttpClientErrorException.NotFound.class,
                ()-> restTemplate.getForEntity(movieInfoServiceUrl + "/" + "{movieId}",
                        MovieInfo.class, "Jaws"));

        Assertions.assertThat(exception.getMessage().equals("404 NotFound"));
    }
}
