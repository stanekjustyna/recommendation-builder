package com.justynastanek.recommendationbuilder.repository;

import com.justynastanek.recommendationbuilder.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long>{

    Role findByRoleName(String name);
}
