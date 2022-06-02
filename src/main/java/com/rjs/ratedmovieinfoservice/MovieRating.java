package com.rjs.ratedmovieinfoservice;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MovieRating {
        private String movieId;
        private int rating;
}
