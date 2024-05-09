package com.khnure.timetable.synchronizer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.khnure.timetable.synchronizer.dto.google.GoogleTokenDto;
import com.khnure.timetable.synchronizer.model.User;
import com.khnure.timetable.synchronizer.service.GoogleService;
import com.khnure.timetable.synchronizer.service.JwtService;
import com.khnure.timetable.synchronizer.service.UserService;
import com.khnure.timetable.synchronizer.util.CalendarHelper;
import com.khnure.timetable.synchronizer.util.GoogleCredentialHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("${api.base-url}")
@RequiredArgsConstructor
public class JwtController {

    private final JwtService jwtService;
    private final UserService userService;
    private final CalendarHelper calendarHelper;
    private final GoogleCredentialHelper googleCredentialHelper;
    private final GoogleService googleService;
    private final ObjectMapper objectMapper;

    @PostMapping("/jwt/create")
    public ResponseEntity<String> createJwt(@RequestBody Map<String,Object> body, HttpServletResponse response) throws JsonProcessingException {

        String code = (String) body.get("code");
        GoogleTokenDto googleTokenDto = googleService.getByCodeToken(code);
        String email = googleService.getUserEmail(googleTokenDto);

        User user = userService.findByEmailOrCreateUser(email, googleTokenDto.getRefreshToken());

        if (!calendarHelper.userHasCalendar(user.getId()) || googleCredentialHelper.credentialsExpired(user.getId())) {
            GoogleCredential googleCredential = googleCredentialHelper.putCredentials(user.getId(), googleTokenDto);
            calendarHelper.createCalendarForUser(user.getId(), googleCredential);
        }

        String jwt = jwtService.create(user);

        Cookie cookie = new Cookie("JWT", jwt);
        cookie.setMaxAge(3600);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setDomain(null);
        response.addCookie(cookie);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(Map.of("JWT", jwt)));
    }
}
