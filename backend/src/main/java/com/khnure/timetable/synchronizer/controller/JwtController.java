package com.khnure.timetable.synchronizer.controller;

import com.khnure.timetable.synchronizer.model.User;
import com.khnure.timetable.synchronizer.service.JwtService;
import com.khnure.timetable.synchronizer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class JwtController {

    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/jwt/create")
    public ResponseEntity<?> createJwt(@RequestParam String email, @RequestParam String accessToken){
        User user =  userService.findByEmailOrCreateUser(email);
        //todo put access token to the concurrent map with key by ID

        String jwt = jwtService.create(user);

        return ResponseEntity.ok(jwt);
    }
}
