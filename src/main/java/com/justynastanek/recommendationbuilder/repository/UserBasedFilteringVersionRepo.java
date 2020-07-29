package com.justynastanek.recommendationbuilder.repository;

import com.justynastanek.recommendationbuilder.model.UserBasedFilteringVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBasedFilteringVersionRepo extends JpaRepository<UserBasedFilteringVersion, Long> {

    UserBasedFilteringVersion findTopByOrderByVersionDesc();
}
