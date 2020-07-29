package com.justynastanek.recommendationbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetails {

    private String title;
    private String original_language;
    private String overview;
    private String poster_path;

}
