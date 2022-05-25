package com.rjs.moviecatalogueservice;

import com.rjs.moviecatalogueservice.resource.MovieCatalogueResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private MovieCatalogueResource movieCatalogueResource;

    @Test
    public void checkControllerPresent(){
        assertThat(movieCatalogueResource).isNotNull();
    }
}
