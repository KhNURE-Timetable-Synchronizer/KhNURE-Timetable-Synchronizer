package com.khnure.timetable.synchronizer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.khnure.timetable.synchronizer.dto.*;
import com.khnure.timetable.synchronizer.mapper.RequestLinksCoordinatorTimetableMapper;
import com.khnure.timetable.synchronizer.model.CustomUserDetails;
import com.khnure.timetable.synchronizer.model.RequestLinksCoordinatorTimetable;
import com.khnure.timetable.synchronizer.service.RequestLinksCoordinatorTimetableService;
import com.khnure.timetable.synchronizer.validator.RequestLinksCoordinatorTimetableGroup;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-url}" + "/request")
@Validated
public class RequestLinksCoordinatorTimetableController {
    private final RequestLinksCoordinatorTimetableService requestLinksCoordinatorTimetableService;
    private final RequestLinksCoordinatorTimetableMapper requestLinksCoordinatorTimetableMapper;

    @PostMapping
    public ResponseEntity<String> createRequest(@RequestBody @Validated RequestLinksCoordinatorTimetablePostDto requestLinksCoordinatorTimetablePostDto, Authentication authentication) throws JsonProcessingException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getDetails();

        requestLinksCoordinatorTimetableService.save(requestLinksCoordinatorTimetablePostDto, userDetails.getUser().getId());
        return ResponseEntity.ok().body("{}");
    }

    @GetMapping("/timetable")
    public ResponseEntity<List<ScheduleWithRequestedStatusDto>> getAllSchedulesWithRequestedStatus(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getDetails();

        return ResponseEntity.ok(requestLinksCoordinatorTimetableService.getAllSchedulesWithRequestedStatusByUserId(userDetails.getUser().getId()));
    }

    @GetMapping
    public PaginationResponse<RequestLinksCoordinatorTimetableGetDto> getRequestsLinksCoordinatorTimetableWithPagination(
            @Validated({Default.class, RequestLinksCoordinatorTimetableGroup.class}) @ModelAttribute PaginationDto paginationDto) {
        Page<RequestLinksCoordinatorTimetable> requestLinksCoordinatorTimetablePage = requestLinksCoordinatorTimetableService.getAllRequestLinksCoordinatorTimetable(paginationDto);
        List<RequestLinksCoordinatorTimetableGetDto> requestLinksCoordinatorTimetableGetDtoList = requestLinksCoordinatorTimetablePage.getContent().stream()
                .map(requestLinksCoordinatorTimetableMapper::toDto)
                .toList();

        return PaginationResponse.<RequestLinksCoordinatorTimetableGetDto>builder()
                .totalItemNumber(new AbstractMap.SimpleEntry<>("totalRequestsNumber", requestLinksCoordinatorTimetablePage.getTotalElements()))
                .totalPageNumber(requestLinksCoordinatorTimetablePage.getTotalPages())
                .currentPageNumber(requestLinksCoordinatorTimetablePage.getNumber() + 1)
                .data(new AbstractMap.SimpleEntry<>("requests", requestLinksCoordinatorTimetableGetDtoList))
                .build();
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Map<String,Object>> getNextAvailableStatus(@PathVariable("id") Long requestId) {
        RequestLinksCoordinatorTimetable request = requestLinksCoordinatorTimetableService.getRequestById(requestId);
        return ResponseEntity.ok(Map.of("nextAvailableStatusList",request.getStatusRequest().getNextAvailableStatus()));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateRequestStatus(@PathVariable("id") Long requestId, @Validated @RequestBody UpdateRequestStatus updateRequestStatus){
        requestLinksCoordinatorTimetableService.updateStatus(requestId, updateRequestStatus.getStatus());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestLinksCoordinatorTimetableGetDto> getRequestLinksCoordinatorTimetableById(@PathVariable("id") Long requestId) {
        RequestLinksCoordinatorTimetable requestLinksCoordinatorTimetable =  requestLinksCoordinatorTimetableService.getRequestLinksCoordinatorTimetableById(requestId);
        return ResponseEntity.ok(requestLinksCoordinatorTimetableMapper.toDto(requestLinksCoordinatorTimetable));
    }
}
