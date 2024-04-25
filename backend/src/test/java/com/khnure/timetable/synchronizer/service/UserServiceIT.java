package com.khnure.timetable.synchronizer.service;

import com.khnure.timetable.synchronizer.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Sql(scripts = "classpath:sql/user/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:sql/user/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class UserServiceIT {

    @Autowired
    private UserService userService;

    @Test
    void shouldRetrieveUser(){
        Optional<User> user = userService.findByEmail("test@gmail.com");

        assertTrue(user.isPresent());
    }
}