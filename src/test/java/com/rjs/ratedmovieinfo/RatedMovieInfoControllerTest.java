package com.rjs.ratedmovieinfo;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(RatedMovieInfoController.class)
class RatedMovieInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieInfoProvider movieInfoProvider;

    @MockBean
    private MovieRatingProvider movieRatingProvider;

    @Test
    public void whenUserValidAndMovieRatingsExistMovieInfosExistShouldReturnFullyPopulatedList() throws Exception {

        Mockito.when(movieRatingProvider.getMovieRatings("joe"))
                .thenReturn(Optional.of(
                        Arrays.asList(
                        new MovieRating("Jaws", 4),
                        new MovieRating("Star Wars", 3))
                ));

        Mockito.when(movieInfoProvider.getMovieInfo("Jaws"))
                .thenReturn( Optional.of(
                        new MovieInfo("Jaws", "A man eating shark terrorises seaside resort.")));

        Mockito.when(movieInfoProvider.getMovieInfo("Star Wars"))
                .thenReturn(Optional.of(
                        new MovieInfo("Star Wars", "In a Galaxy, far, far away ....")));

        String expectedContent = "[{\"name\":\"Jaws\",\"description\":\"A man eating shark terrorises seaside resort.\",\"rating\":4},{\"name\":\"Star Wars\",\"description\":\"In a Galaxy, far, far away ....\",\"rating\":3}]";
        mockMvc.perform(MockMvcRequestBuilders.get("/ratedMovieInfos/joe"))
                .andExpect(MockMvcResultMatchers.content().string(expectedContent));
    }

    @Test
    public void whenInvalidUserShouldReturnStatusNotFound() throws Exception {

        Mockito.when(movieRatingProvider.getMovieRatings("foo"))
                .thenThrow(new ResourceNotFoundException());

        String expectedContent = "";
        mockMvc.perform(MockMvcRequestBuilders.get("/ratedMovieInfos/{userId}", "foo"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void whenValidUserAndNoMovieRatingsShouldReturnEmptyList() throws Exception {

        Mockito.when(movieRatingProvider.getMovieRatings("bob"))
                .thenReturn(Optional.empty());


        Mockito.when(movieInfoProvider.getMovieInfo("Star Wars"))
                .thenReturn(Optional.of(new MovieInfo("Star Wars", "In a Galaxy, far, far away ....")));

        String expectedContent = "";
        mockMvc.perform(MockMvcRequestBuilders.get("/ratedMovieInfos/{userId}", "bob"))
                .andExpect(MockMvcResultMatchers.content().string(expectedContent));
    }


    @Test
    public void whenMovieInfoNotAvailableShouldReturnEmptyDescriptionInMovieRating() throws Exception {

        Mockito.when(movieRatingProvider.getMovieRatings("joe"))
                .thenReturn(Optional.of(
                        Arrays.asList(
                                new MovieRating("Jaws", 4),
                                new MovieRating("Star Wars", 3))
                ));

        Mockito.when(movieInfoProvider.getMovieInfo("Jaws"))
                .thenReturn(Optional.empty());

        Mockito.when(movieInfoProvider.getMovieInfo("Star Wars"))
                .thenReturn(Optional.of(new MovieInfo("Star Wars", "In a Galaxy, far, far away ....")));

        String expectedContent = "[{\"name\":\"Jaws\",\"description\":\"\",\"rating\":4},{\"name\":\"Star Wars\",\"description\":\"In a Galaxy, far, far away ....\",\"rating\":3}]";
        mockMvc.perform(MockMvcRequestBuilders.get("/ratedMovieInfos/{userId}", "joe"))
                .andExpect(MockMvcResultMatchers.content().string(expectedContent));
    }
}