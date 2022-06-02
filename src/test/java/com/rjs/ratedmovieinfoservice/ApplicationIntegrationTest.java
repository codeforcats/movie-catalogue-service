package com.rjs.ratedmovieinfoservice;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest()
public class ApplicationIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void fullyPopulatedRatedMovieInfos()
    {

        Collection<RatedMovieInfo> expectedRatedMovieInfos = Arrays.asList(
                new RatedMovieInfo("Jaws", "A man eating shark terrorises seaside resort.", 4),
                new RatedMovieInfo("Star Wars", "In a Galaxy, far, far away ....",3)
        );

        ResponseEntity<RatedMovieInfo[]> response =
                restTemplate.getForEntity("http://localhost:" + port + "/ratedMovieInfos/{userId}",
                        RatedMovieInfo[].class,
                        "joe");

        Collection<RatedMovieInfo> ratedMovieInfos = Arrays.asList(response.getBody());

        Assertions.assertThat(ratedMovieInfos).isEqualTo(expectedRatedMovieInfos);

    }

}
