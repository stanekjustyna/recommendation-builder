package com.justynastanek.recommendationbuilder.service;

import com.justynastanek.recommendationbuilder.model.Role;
import com.justynastanek.recommendationbuilder.model.User;
import com.justynastanek.recommendationbuilder.repository.RoleRepo;
import com.justynastanek.recommendationbuilder.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleService {

    private RoleRepo roleRepository;
    private UserRepo userRepository;

    @Autowired
    public RoleService(RoleRepo roleRepository, UserRepo userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public void initRoles() {
        Role role1 = new Role();
        role1.setRoleName("USER");

        Role role2 = new Role();
        role2.setRoleName("ADMIN");

        roleRepository.save(role1);
        roleRepository.save(role2);

        User user = userRepository.findById(2L).get();
        user.setRoles(Set.of(roleRepository.findByRoleName("USER"), roleRepository.findByRoleName("ADMIN")));
        userRepository.save(user);
    }
}
