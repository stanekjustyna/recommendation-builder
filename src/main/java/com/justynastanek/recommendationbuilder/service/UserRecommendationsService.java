package com.justynastanek.recommendationbuilder.service;


import com.justynastanek.recommendationbuilder.model.User;
import com.justynastanek.recommendationbuilder.model.UserBasedFilteringRecs;
import com.justynastanek.recommendationbuilder.model.UserBasedFilteringVersion;
import com.justynastanek.recommendationbuilder.repository.ItemRepo;
import com.justynastanek.recommendationbuilder.repository.UserBasedFilteringRecsRepo;
import com.justynastanek.recommendationbuilder.repository.UserBasedFilteringVersionRepo;
import com.justynastanek.recommendationbuilder.repository.UserRepo;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserRecommendationsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRecommendationsService.class);
    private final UserBasedRecommender userBasedRecommender;
    private final UserBasedFilteringVersionRepo userBasedFilteringVersionRepo;
    private final UserBasedFilteringRecsRepo userBasedFilteringRecsRepo;
    private final UserRepo userRepo;
    private final ItemRepo itemRepo;

    @Autowired
    public UserRecommendationsService(UserBasedRecommender userBasedRecommender,
                                      UserBasedFilteringVersionRepo userBasedFilteringVersionRepo,
                                      UserBasedFilteringRecsRepo userBasedFilteringRecsRepo,
                                      UserRepo userRepo, ItemRepo itemRepo) {
        this.userBasedRecommender = userBasedRecommender;
        this.userBasedFilteringVersionRepo = userBasedFilteringVersionRepo;
        this.userBasedFilteringRecsRepo = userBasedFilteringRecsRepo;
        this.userRepo = userRepo;
        this.itemRepo = itemRepo;
    }


    public void userBasedRecommendations() throws TasteException {

        LOGGER.info("User-Based Filtering Recommendations Calculation Process started...");

        UserBasedFilteringVersion newVersion = new UserBasedFilteringVersion();
        newVersion.setTimestamp(LocalDateTime.now());
        userBasedFilteringVersionRepo.save(newVersion);

        UserBasedFilteringVersion actualVersion = userBasedFilteringVersionRepo.findTopByOrderByVersionDesc();


        for (User user : userRepo.findAll()) {

            List<RecommendedItem> recommendations = userBasedRecommender.recommend(
                    user.getUserId(), 10);

            for (RecommendedItem item : recommendations) {

                if(item.getValue() >= 5.0 ) {
                    UserBasedFilteringRecs userBasedFilteringRecs = new UserBasedFilteringRecs();
                    userBasedFilteringRecs.setTimestamp(LocalDateTime.now());
                    userBasedFilteringRecs.setUser(user);
                    String movieId = item.getItemID() + "";
                    if (movieId.length() < 7) {
                        movieId = ("0000000" + movieId).substring(movieId.length());
                    }
                    userBasedFilteringRecs.setItem(itemRepo.findById(movieId).get());
                    userBasedFilteringRecs.setPreference(item.getValue());
                    userBasedFilteringRecs.setVersion(actualVersion);
                    userBasedFilteringRecsRepo.save(userBasedFilteringRecs);
                }
            }

            LOGGER.info("User-Based Filtering Recommendations for User: " + user.getUserId() + " saved.");
        }

        LOGGER.info("User-Based Filtering Recommendations Calculation Process ended.");
    }
}
