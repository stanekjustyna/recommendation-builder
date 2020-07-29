package com.justynastanek.recommendationbuilder.controller;

import com.justynastanek.recommendationbuilder.service.RatingCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rating")
public class AvgRatingController {

    private RatingCalculationService ratingCalculationService;

    @Autowired
    public AvgRatingController(RatingCalculationService ratingCalculationService) {
        this.ratingCalculationService = ratingCalculationService;
    }

    @GetMapping
    public void calculateAvgRatings() {
        ratingCalculationService.calculateAvgRating();
    }
}
