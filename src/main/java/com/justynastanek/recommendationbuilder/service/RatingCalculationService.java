package com.justynastanek.recommendationbuilder.service;

import com.justynastanek.recommendationbuilder.dto.MovieAvgRating;
import com.justynastanek.recommendationbuilder.repository.ItemRepo;
import com.justynastanek.recommendationbuilder.repository.RatingRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RatingCalculationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatingCalculationService.class);
    private ItemRepo itemRepository;
    private RatingRepo ratingRepo;

    @Autowired
    public RatingCalculationService(ItemRepo itemRepository, RatingRepo ratingRepo) {
        this.itemRepository = itemRepository;
        this.ratingRepo = ratingRepo;
    }

    public void calculateAvgRating() {
        for(MovieAvgRating movieRating  : ratingRepo.getAvgRatings()) {
            itemRepository.updateAvgRating(movieRating.getMovieId(), movieRating.getAvgRating());
            LOGGER.info("Saved rating for movie: " + movieRating.getMovieId());
        }

        LOGGER.info("CALCULATING AVERAGE MOVIE RATINGS COMPLETED");
    }
}
