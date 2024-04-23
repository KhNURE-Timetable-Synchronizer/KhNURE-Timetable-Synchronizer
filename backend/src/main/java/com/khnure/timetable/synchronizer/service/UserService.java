package com.khnure.timetable.synchronizer.service;

import com.khnure.timetable.synchronizer.model.User;
import com.khnure.timetable.synchronizer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

}
