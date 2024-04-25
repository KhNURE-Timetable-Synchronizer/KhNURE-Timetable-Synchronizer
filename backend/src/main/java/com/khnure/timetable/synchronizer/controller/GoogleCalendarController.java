package com.khnure.timetable.synchronizer.controller;

import com.khnure.timetable.synchronizer.dto.GoogleCalendarDto;
import com.khnure.timetable.synchronizer.service.GoogleCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-url}/timetables/google")
public class GoogleCalendarController {
    private final GoogleCalendarService googleCalendarService;
    @GetMapping
    public ResponseEntity<List> getTimetablesByUserId(@RequestParam Long userId){
        List<GoogleCalendarDto> response = googleCalendarService.getTimetablesByUserId(userId);
        return ResponseEntity.ok(response);
    }
}
