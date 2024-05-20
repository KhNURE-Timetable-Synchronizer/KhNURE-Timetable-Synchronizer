package com.khnure.timetable.synchronizer.controller;

import com.khnure.timetable.synchronizer.dto.GoogleCalendarDto;
import com.khnure.timetable.synchronizer.dto.TimetableExportDto;
import com.khnure.timetable.synchronizer.model.CustomUserDetails;
import com.khnure.timetable.synchronizer.service.GoogleCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "${api.base-url}/timetables/google", produces = MediaType.APPLICATION_JSON_VALUE)
public class GoogleCalendarController {


    private final GoogleCalendarService googleCalendarService;

    @PostMapping
    public ResponseEntity<?> export(@RequestBody TimetableExportDto timetableExportDto, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getDetails();
        googleCalendarService.export(userDetails.getUser().getId(), timetableExportDto);

        return ResponseEntity.ok().body("{}");
    }

    @GetMapping
    public ResponseEntity<?> getTimetablesByUserId(Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getDetails();
        Long userId = userDetails.getUser().getId();
        List<GoogleCalendarDto> response = googleCalendarService.getTimetablesByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCalendarById(@PathVariable Long id, Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getDetails();
        Long userId = userDetails.getUser().getId();
        googleCalendarService.deleteCalendarByIdAndUserId(id, userId);

        return ResponseEntity.ok().body("{}");
    }
}
