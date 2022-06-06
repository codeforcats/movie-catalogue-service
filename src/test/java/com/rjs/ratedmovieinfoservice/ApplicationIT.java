package com.rjs.ratedmovieinfoservice;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collection;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @RegisterExtension
    static WireMockExtension movieInfoServiceMock = WireMockExtension.newInstance()
            .options(wireMockConfig().port(8082))
            .build();

    @RegisterExtension
    static WireMockExtension movieRatingsServiceMock = WireMockExtension.newInstance()
            .options(wireMockConfig().port(8083))
            .build();

    @Test
    void ratedMovieInfosFound() {

        movieRatingsServiceMock.stubFor(get(urlEqualTo("/movieRatings/joe"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"movieId\":\"Jaws\",\"rating\":4},{\"movieId\":\"Star Wars\",\"rating\":3}]")));

        movieInfoServiceMock.stubFor(get(urlEqualTo("/movies/Jaws"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"movieId\":\"Jaws\",\"description\":\"A man eating shark.\"}")));

        movieInfoServiceMock.stubFor(get(urlEqualTo("/movies/Star%20Wars"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"movieId\":\"Star Wars\",\"description\":\"In a Galaxy, far, far away ....\"}")));


        ResponseEntity<RatedMovieInfo[]> responseEntity =
                restTemplate.getForEntity("http://localhost:" + port + "/ratedMovieInfos/{userId}",
                RatedMovieInfo[].class, "joe");

        Collection<RatedMovieInfo> ratedMovies = Arrays.asList(responseEntity.getBody());

        Collection<RatedMovieInfo> expectedRatedMovieInfos = Arrays.asList(
                new RatedMovieInfo("Jaws", "A man eating shark.", 4),
                new RatedMovieInfo("Star Wars", "In a Galaxy, far, far away ....", 3)
        );

        Assertions.assertThat(ratedMovies).isEqualTo(expectedRatedMovieInfos);
    }
}
