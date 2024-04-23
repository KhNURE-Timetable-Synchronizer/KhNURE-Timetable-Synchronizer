package com.khnure.timetable.synchronizer.controller;

import com.khnure.timetable.synchronizer.model.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<?> test(Authentication authentication){
        CustomUserDetails details = (CustomUserDetails) authentication.getDetails();
        return ResponseEntity.ok(details.getUser().getEmail());
    }
}
