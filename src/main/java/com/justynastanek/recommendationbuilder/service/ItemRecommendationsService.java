package com.justynastanek.recommendationbuilder.service;

import com.justynastanek.recommendationbuilder.model.Item;
import com.justynastanek.recommendationbuilder.model.ItemBasedFilteringRecs;
import com.justynastanek.recommendationbuilder.model.ItemBasedFilteringVersion;
import com.justynastanek.recommendationbuilder.repository.ItemBasedFilteringRecsRepo;
import com.justynastanek.recommendationbuilder.repository.ItemBasedFilteringVersionRepo;
import com.justynastanek.recommendationbuilder.repository.ItemRepo;
import org.apache.mahout.cf.taste.common.NoSuchItemException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemRecommendationsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemRecommendationsService.class);
    private final ItemBasedRecommender itemBasedRecommender;
    private final ItemBasedFilteringVersionRepo itemBasedFilteringVersionRepo;
    private final ItemBasedFilteringRecsRepo itemBasedFilteringRecsRepo;
    private final ItemRepo itemRepo;

    @Autowired
    public ItemRecommendationsService(ItemBasedRecommender itemBasedRecommender,
                                      ItemBasedFilteringVersionRepo itemBasedFilteringVersionRepo,
                                      ItemBasedFilteringRecsRepo itemBasedFilteringRecsRepo, ItemRepo itemRepo) {
        this.itemBasedRecommender = itemBasedRecommender;
        this.itemBasedFilteringVersionRepo = itemBasedFilteringVersionRepo;
        this.itemBasedFilteringRecsRepo = itemBasedFilteringRecsRepo;
        this.itemRepo = itemRepo;
    }

    public void itemBasedRecommendations() throws TasteException {

        LOGGER.info("Item-Based Filtering Recommendations Calculation Process started...");

        ItemBasedFilteringVersion newVersion = new ItemBasedFilteringVersion();
        newVersion.setTimestamp(LocalDateTime.now());
        itemBasedFilteringVersionRepo.save(newVersion);

        ItemBasedFilteringVersion actualVersion = itemBasedFilteringVersionRepo.findTopByOrderByVersionDesc();

        for (Item baseItem : itemRepo.findAll()) {

            try {
                List<RecommendedItem> recommendations = itemBasedRecommender.mostSimilarItems(
                        Long.parseLong(baseItem.getMovieId()), 10);

                for (RecommendedItem recommendedItem : recommendations) {

                    if(recommendedItem.getValue() > 0.5) {
                        ItemBasedFilteringRecs itemBasedFilteringRecs = new ItemBasedFilteringRecs();
                        itemBasedFilteringRecs.setTimestamp(LocalDateTime.now());
                        itemBasedFilteringRecs.setBaseItem(baseItem);
                        String movieId = recommendedItem.getItemID() + "";
                        if (movieId.length() < 7) {
                            movieId = ("0000000" + movieId).substring(movieId.length());
                        }
                        itemBasedFilteringRecs.setSimilarItem(itemRepo.findById(movieId).get());
                        itemBasedFilteringRecs.setSimilarity(recommendedItem.getValue());
                        itemBasedFilteringRecs.setVersion(actualVersion);
                        itemBasedFilteringRecsRepo.save(itemBasedFilteringRecs);
                    }
                }
            } catch(NoSuchItemException ex) {

                LOGGER.warn("No such item exception for Item: " + baseItem.getMovieId());

            }

            LOGGER.info("Item-Based Filtering Recommendations for Item: " + baseItem.getMovieId() + " saved.");
        }

        LOGGER.info("Item-Based Filtering Recommendations Calculation Process ended.");
    }
}
