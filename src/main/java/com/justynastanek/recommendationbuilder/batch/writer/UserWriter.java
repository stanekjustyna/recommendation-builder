package com.justynastanek.recommendationbuilder.batch.writer;

import com.justynastanek.recommendationbuilder.model.User;
import com.justynastanek.recommendationbuilder.repository.UserRepo;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserWriter implements ItemWriter<User> {

    private UserRepo userRepository;

    @Autowired
    public UserWriter(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void write(List<? extends User> users) throws Exception {

        System.out.println("Data Saved for Users: " + users);
        userRepository.saveAll(users);
    }
}
