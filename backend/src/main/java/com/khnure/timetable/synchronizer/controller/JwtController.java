package com.khnure.timetable.synchronizer.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.khnure.timetable.synchronizer.model.User;
import com.khnure.timetable.synchronizer.service.JwtService;
import com.khnure.timetable.synchronizer.service.UserService;
import com.khnure.timetable.synchronizer.util.CalendarHelper;
import com.khnure.timetable.synchronizer.util.GoogleCredentialHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-url}")
@RequiredArgsConstructor
public class JwtController {

    private final JwtService jwtService;
    private final UserService userService;
    private final CalendarHelper calendarHelper;
    private final GoogleCredentialHelper googleCredentialHelper;

    @PostMapping("/jwt/create")
    public ResponseEntity<String> createJwt(@RequestParam String email, @RequestParam String accessToken) {
        User user = userService.findByEmailOrCreateUser(email);

        if (!calendarHelper.userHasCalendar(user.getId()) || googleCredentialHelper.credentialsExpired(user.getId())) {
            GoogleCredential googleCredential = googleCredentialHelper.putCredentials(user.getId(), accessToken);
            calendarHelper.createCalendarForUser(user.getId(), googleCredential);
        }

        String jwt = jwtService.create(user);

        return ResponseEntity.ok(jwt);
    }
}
