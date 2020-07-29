package com.justynastanek.recommendationbuilder.repository;

import com.justynastanek.recommendationbuilder.model.ItemBasedFilteringRecs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemBasedFilteringRecsRepo extends JpaRepository<ItemBasedFilteringRecs, Long> {
}
