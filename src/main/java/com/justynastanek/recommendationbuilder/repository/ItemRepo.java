package com.justynastanek.recommendationbuilder.repository;

import com.justynastanek.recommendationbuilder.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ItemRepo extends JpaRepository<Item, String> {

    @Query("select movieId from Item")
    List<String> findAllMovieId();

    @Transactional
    @Modifying
    @Query("update Item i set i.language = :language, i.poster = :poster, i.description = :description, i.title = :title " +
            "where i.movieId = :id")
    int updateDetails(@Param("id") String id, @Param("language") String language, @Param("poster") String poster,
                      @Param("description") String description, @Param("title") String title);

    @Transactional
    @Modifying
    @Query("update Item i set i.avgRating = :avgRating where movieId = :movieId")
    int updateAvgRating(@Param("movieId") String movieId, @Param("avgRating") double avgRating);

}
