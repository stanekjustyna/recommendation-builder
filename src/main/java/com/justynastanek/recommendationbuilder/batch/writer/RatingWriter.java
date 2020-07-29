package com.justynastanek.recommendationbuilder.batch.writer;

import com.justynastanek.recommendationbuilder.model.Rating;
import com.justynastanek.recommendationbuilder.repository.RatingRepo;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RatingWriter implements ItemWriter<Rating> {

    private RatingRepo ratingRepository;

    @Autowired
    public RatingWriter(RatingRepo ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public void write(List<? extends Rating> ratings) throws Exception {

        System.out.println("Data Saved for Ratings: " + ratings);
        ratingRepository.saveAll(ratings);
    }
}
