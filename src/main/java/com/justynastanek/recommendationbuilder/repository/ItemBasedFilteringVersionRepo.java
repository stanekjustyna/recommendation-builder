package com.justynastanek.recommendationbuilder.repository;

import com.justynastanek.recommendationbuilder.model.ItemBasedFilteringVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemBasedFilteringVersionRepo extends JpaRepository<ItemBasedFilteringVersion, Long> {

    ItemBasedFilteringVersion findTopByOrderByVersionDesc();
}
