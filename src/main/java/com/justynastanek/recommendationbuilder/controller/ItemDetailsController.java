package com.justynastanek.recommendationbuilder.controller;

import com.justynastanek.recommendationbuilder.dto.MovieDetails;
import com.justynastanek.recommendationbuilder.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/movies/details")
public class ItemDetailsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemDetailsController.class);
    private ItemService itemService;
    private WebClient.Builder webClient;

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    public ItemDetailsController(ItemService itemService, WebClient.Builder webClient) {
        this.itemService = itemService;
        this.webClient = webClient;
    }

    @GetMapping
    public String loadDetails() {
        for (String movieId : itemService.getAllMovieIds()) {

            MovieDetails movieDetails = webClient.build().get().uri("https://api.themoviedb.org/3/movie/tt" + movieId +
                        "?external_source=imdb_id&api_key=" + apiKey).retrieve()
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.empty())
                    .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.empty())
                    .bodyToMono(MovieDetails.class).block();

            itemService.updateDetails(movieId, movieDetails.getOriginal_language(), movieDetails.getPoster_path(),
                        movieDetails.getOverview(), movieDetails.getTitle());

            LOGGER.info("Saved details for movie: " + movieId);
            }
        LOGGER.info("LOADING DETAILS COMPLETED");
        return "LOADING DETAILS COMPLETED";
    }
}
