package com.rjs.ratedmovieinfo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RatedMovieInfo {

    String name;
    String description;
    int rating;
}