package com.khnure.timetable.synchronizer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.khnure.timetable.synchronizer.dto.RequestLinksCoordinatorTimetablePostDTO;
import com.khnure.timetable.synchronizer.model.CustomUserDetails;
import com.khnure.timetable.synchronizer.service.RequestLinksCoordinatorTimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-url}")
public class RequestLinksCoordinatorTimetableController {
    private final RequestLinksCoordinatorTimetableService requestLinksCoordinatorTimetableService;

    @PostMapping("/request")
    public ResponseEntity<String> createRequest(@RequestBody RequestLinksCoordinatorTimetablePostDTO requestLinksCoordinatorTimetablePostDTO, Authentication authentication) throws JsonProcessingException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getDetails();

        requestLinksCoordinatorTimetableService.save(requestLinksCoordinatorTimetablePostDTO, userDetails.getUser().getId());
        return ResponseEntity.ok().body("{}");
    }
}
