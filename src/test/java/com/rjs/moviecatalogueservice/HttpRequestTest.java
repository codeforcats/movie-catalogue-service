package com.rjs.moviecatalogueservice;

import com.rjs.moviecatalogueservice.model.CatalogueItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnHardCodedResult() {

        ResponseEntity<List<CatalogueItem>> responseEntity =
                restTemplate.exchange("http://localhost:" + port + "/catalogues/1",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                );

        List<CatalogueItem> catalogueItemList = responseEntity.getBody();

        assert catalogueItemList != null;
        assertThat(catalogueItemList.size()).isEqualTo(1);

        CatalogueItem expectedCatalogueItem =
                new CatalogueItem("Jaws", "A man eating shark terrorises seaside resort.", 4);

        assertThat(catalogueItemList.get(0)).isEqualTo(expectedCatalogueItem);
    }
}
