package com.khnure.timetable.synchronizer.controller;

import com.khnure.timetable.synchronizer.dto.ScheduleDto;
import com.khnure.timetable.synchronizer.service.NureScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CalendarController {
    private final NureScheduleService calendarService;

    @ExceptionHandler
    @GetMapping( "/api/v1/khnure/timetables")
    public ResponseEntity<?> getAavailableSchedules (){
        try {
            List<ScheduleDto> groups = calendarService.getGroups();
            List<ScheduleDto> teachers = calendarService.getTeachers();

            List<ScheduleDto> response = new ArrayList<>();
            response.addAll(groups);
            response.addAll(teachers);

            return ResponseEntity.ok(response);

        }catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }

    }
}
