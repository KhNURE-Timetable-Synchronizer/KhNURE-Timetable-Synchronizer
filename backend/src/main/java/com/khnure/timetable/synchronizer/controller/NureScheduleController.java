package com.khnure.timetable.synchronizer.controller;

import com.khnure.timetable.synchronizer.dto.ScheduleDto;
import com.khnure.timetable.synchronizer.service.NureScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-url}")
public class NureScheduleController {
    private final NureScheduleService calendarService;

    @GetMapping( "/khnure/timetables")
    public ResponseEntity<List> getAvailableSchedules (){
        List<ScheduleDto> groups = calendarService.getGroups();
        List<ScheduleDto> teachers = calendarService.getTeachers();

        List<ScheduleDto> response = new ArrayList<>();
        response.addAll(groups);
        response.addAll(teachers);

        return ResponseEntity.ok(response);
    }
}
