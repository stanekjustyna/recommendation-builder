package com.justynastanek.recommendationbuilder.batch.mapper;

import com.justynastanek.recommendationbuilder.model.Rating;
import com.justynastanek.recommendationbuilder.repository.ItemRepo;
import com.justynastanek.recommendationbuilder.repository.UserRepo;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class RatingFieldSetMapper implements FieldSetMapper {

    private UserRepo userRepository;

    private ItemRepo itemRepository;

    @Autowired
    public RatingFieldSetMapper(UserRepo userRepository, ItemRepo itemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public Rating mapFieldSet(FieldSet fieldSet) throws BindException {
        Rating rating = new Rating();
        rating.setUser(userRepository.findById(fieldSet.readLong("userId")).orElse(null));
        rating.setItem(itemRepository.findById(fieldSet.readString("movieId")).orElse(null));
        rating.setRating(fieldSet.readInt("rating"));
        rating.setRatingTimestamp(LocalDateTime.ofEpochSecond(fieldSet.readLong("ratingTimestamp"),0, ZoneOffset.UTC));
        return rating;
    }
}
