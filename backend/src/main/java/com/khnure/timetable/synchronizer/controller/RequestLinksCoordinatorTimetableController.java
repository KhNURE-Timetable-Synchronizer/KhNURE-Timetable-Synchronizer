package com.khnure.timetable.synchronizer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.khnure.timetable.synchronizer.dto.RequestLinksCoordinatorTimetablePostDTO;
import com.khnure.timetable.synchronizer.dto.ScheduleWithRequestedStatusDto;
import com.khnure.timetable.synchronizer.model.CustomUserDetails;
import com.khnure.timetable.synchronizer.service.RequestLinksCoordinatorTimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-url}" + "/request")
public class RequestLinksCoordinatorTimetableController {
    private final RequestLinksCoordinatorTimetableService requestLinksCoordinatorTimetableService;

    @PostMapping
    public ResponseEntity<String> createRequest(@RequestBody @Validated RequestLinksCoordinatorTimetablePostDTO requestLinksCoordinatorTimetablePostDTO, Authentication authentication) throws JsonProcessingException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getDetails();

        requestLinksCoordinatorTimetableService.save(requestLinksCoordinatorTimetablePostDTO, userDetails.getUser().getId());
        return ResponseEntity.ok().body("{}");
    }

    @GetMapping("/timetable")
    public ResponseEntity<List<ScheduleWithRequestedStatusDto>> getAllSchedulesWithRequestedStatus(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getDetails();

        return ResponseEntity.ok(requestLinksCoordinatorTimetableService.getAllSchedulesWithRequestedStatusByUserId(userDetails.getUser().getId()));
    }
}
