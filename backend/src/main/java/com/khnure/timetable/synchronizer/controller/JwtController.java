package com.khnure.timetable.synchronizer.controller;

import com.khnure.timetable.synchronizer.service.JwtService;
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

    @PostMapping("/jwt/create")
    public ResponseEntity<?> createJwt(@RequestParam String email, @RequestParam String accessToken){
        String jwt = jwtService.create(email);

        return ResponseEntity.ok(jwt);
    }
}
