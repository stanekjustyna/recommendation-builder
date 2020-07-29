package com.justynastanek.recommendationbuilder.batch.mapper;

import com.justynastanek.recommendationbuilder.model.User;
import com.justynastanek.recommendationbuilder.repository.RoleRepo;
import com.justynastanek.recommendationbuilder.repository.UserRepo;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.util.Set;

@Component
public class UserFieldSetMapper implements FieldSetMapper {

    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private PasswordEncoder passwordEncoder;

    public UserFieldSetMapper(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Object mapFieldSet(FieldSet fieldSet) throws BindException {
        User user = new User();
        long userId = fieldSet.readLong("userId");
        user.setUserId(userId);
        user.setTwitterId(fieldSet.readLong("twitterId"));
        user.setUsername("user" + userId);
        user.setPassword(passwordEncoder.encode("password" + userId));
        user.setEmail("user" + userId + "@gmail.com");
        user.setRoles(Set.of(roleRepo.findByRoleName("ROLE_USER")));
        return user;
    }
}
