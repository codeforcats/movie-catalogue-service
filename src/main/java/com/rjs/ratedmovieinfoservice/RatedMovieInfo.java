package com.rjs.ratedmovieinfoservice;

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