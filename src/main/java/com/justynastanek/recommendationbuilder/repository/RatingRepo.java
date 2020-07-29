package com.justynastanek.recommendationbuilder.repository;

import com.justynastanek.recommendationbuilder.dto.MovieAvgRating;
import com.justynastanek.recommendationbuilder.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RatingRepo extends JpaRepository<Rating, LocalDateTime> {

    @Query("select new com.justynastanek.recommendationbuilder.dto.MovieAvgRating(i.movieId, avg(r.rating)) " +
            "from Rating r join r.item i group by i.movieId")
    List<MovieAvgRating> getAvgRatings();

}
