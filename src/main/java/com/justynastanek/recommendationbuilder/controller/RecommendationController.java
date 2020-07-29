package com.justynastanek.recommendationbuilder.controller;

import com.justynastanek.recommendationbuilder.service.ItemRecommendationsService;
import com.justynastanek.recommendationbuilder.service.UserRecommendationsService;
import org.apache.mahout.cf.taste.common.TasteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {    private UserRecommendationsService userRecommendationsService;
    private ItemRecommendationsService itemRecommendationsService;

    @Autowired
    public RecommendationController(UserRecommendationsService userRecommendationsService,
                                    ItemRecommendationsService itemRecommendationsService) {
        this.userRecommendationsService = userRecommendationsService;
        this.itemRecommendationsService = itemRecommendationsService;
    }

    @GetMapping("/users")
    public void createrUserRecommendations() throws TasteException {
        userRecommendationsService.userBasedRecommendations();
    }

    @GetMapping("/items")
    public void createrItemRecommendations() throws TasteException {
        itemRecommendationsService.itemBasedRecommendations();
    }


}
