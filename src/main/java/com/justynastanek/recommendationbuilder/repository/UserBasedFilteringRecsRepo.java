package com.justynastanek.recommendationbuilder.repository;

import com.justynastanek.recommendationbuilder.model.UserBasedFilteringRecs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBasedFilteringRecsRepo  extends JpaRepository<UserBasedFilteringRecs, Long> {
}
