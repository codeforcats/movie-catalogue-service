package com.rjs.ratedmovieinfoservice;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class MovieInfoServiceWireMockIT {
    @RegisterExtension
    static WireMockExtension movieInfoServiceMock = WireMockExtension.newInstance()
            .options(wireMockConfig().port(8082))
            .build();

    @Test
    void wireMockMovieInfoService() {

        movieInfoServiceMock.stubFor(get(urlEqualTo("/movies/Jaws"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"movieId\":\"Jaws\",\"description\":\"A man eating shark.\"}")));

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<MovieInfo> responseEntity = restTemplate.getForEntity("http://localhost:8082/movies/{movieId}",
                MovieInfo.class, "Jaws");

        MovieInfo expectedMovieInfo = new MovieInfo("Jaws", "A man eating shark.");
        MovieInfo movieInfo = responseEntity.getBody();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(expectedMovieInfo);
    }

}
