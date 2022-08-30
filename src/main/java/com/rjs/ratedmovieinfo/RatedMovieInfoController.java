package com.rjs.ratedmovieinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/ratedMovieInfos")
public class RatedMovieInfoController {

    @Autowired
    private MovieRatingProvider movieRatingProvider;

    @Autowired
    private MovieInfoProvider movieInfoProvider;

    @RequestMapping("/{userId}")
    public Collection<RatedMovieInfo> getRatedMovieInfoByUserId(@PathVariable String userId) {

        Optional<Collection<MovieRating>> movieRatings = movieRatingProvider.getMovieRatings(userId);

        if (movieRatings.isEmpty()) {
            throw new ResourceNotFoundException();
        } else {

            return movieRatings.get().stream()
                    .map(this::createRatedMovieInfo)
                    .collect(toList());
        }

    }

    private RatedMovieInfo createRatedMovieInfo(MovieRating movieRating) {

        Optional<MovieInfo> movieInfo = movieInfoProvider.getMovieInfo(movieRating.getMovieId());

        return new RatedMovieInfo(  movieRating.getMovieId(),
                                    movieInfo.isEmpty() ? "" : movieInfo.get().getDescription(),
                                    movieRating.getRating() );
    }

}
