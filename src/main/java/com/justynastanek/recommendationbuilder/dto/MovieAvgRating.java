package com.justynastanek.recommendationbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieAvgRating {

    private String movieId;
    private double avgRating;
}
