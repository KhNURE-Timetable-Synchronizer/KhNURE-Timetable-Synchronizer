package com.khnure.timetable.synchronizer.service;


import com.khnure.timetable.synchronizer.dto.PaginationDto;
import com.khnure.timetable.synchronizer.dto.RequestLinksCoordinatorTimetablePostDto;
import com.khnure.timetable.synchronizer.dto.ScheduleDto;
import com.khnure.timetable.synchronizer.dto.ScheduleWithRequestedStatusDto;
import com.khnure.timetable.synchronizer.exception.DuplicateRequestException;
import com.khnure.timetable.synchronizer.exception.IllegalRequestStatusTransitionException;
import com.khnure.timetable.synchronizer.exception.RequestNotFoundException;
import com.khnure.timetable.synchronizer.exception.ScheduleNotFoundException;
import com.khnure.timetable.synchronizer.mapper.ScheduleDtoMapper;
import com.khnure.timetable.synchronizer.model.*;
import com.khnure.timetable.synchronizer.repository.RequestLinksCoordinatorTimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestLinksCoordinatorTimetableService {
    private final RequestLinksCoordinatorTimetableRepository requestLinksCoordinatorTimetableRepository;
    private final KhnureTimetablesService khnureTimetablesService;
    private final NureScheduleService nureScheduleService;
    private final UserService userService;
    private final ScheduleDtoMapper scheduleDtoMapper;

    public RequestLinksCoordinatorTimetable save(RequestLinksCoordinatorTimetablePostDto requestLinksCoordinatorTimetablePostDTO, Long userId) {
        Optional<ScheduleDto> timetableOptional = nureScheduleService.getScheduleById(requestLinksCoordinatorTimetablePostDTO.getKhnureTimetableId());
        if (timetableOptional.isEmpty()) {
            throw new ScheduleNotFoundException(requestLinksCoordinatorTimetablePostDTO.getKhnureTimetableId());
        }

        KhnureTimetables khnureTimetables = khnureTimetablesService.addKhnureTimetable(timetableOptional.get().getName(), timetableOptional.get().getId()).get();

        User user = userService.getUserById(userId).get();
        RequestLinksCoordinatorTimetable requestLinksCoordinatorTimetable = RequestLinksCoordinatorTimetable.builder()
                .user(user)
                .contactAccount(requestLinksCoordinatorTimetablePostDTO.getContactAccount())
                .khnureTimetables(khnureTimetables)
                .statusRequest(StatusRequest.CREATED)
                .build();

        Long khnureTimetablesId = requestLinksCoordinatorTimetable.getKhnureTimetables().getId();

        List<StatusRequest> permissibleStatus = new ArrayList<>();
        permissibleStatus.add(StatusRequest.DECLINED);

        Optional<RequestLinksCoordinatorTimetable> request = requestLinksCoordinatorTimetableRepository.findByUserIdAndKhnureTimetablesIdAndStatusRequestNotIn(userId, khnureTimetablesId, permissibleStatus);
        if (request.isPresent()) {
            throw new DuplicateRequestException(String.format("There is already a request for schedule \"%s\"", requestLinksCoordinatorTimetable.getKhnureTimetables().getName()));
        }

        return requestLinksCoordinatorTimetableRepository.save(requestLinksCoordinatorTimetable);
    }

    public List<ScheduleWithRequestedStatusDto> getAllSchedulesWithRequestedStatusByUserId(Long userId) {
        List<RequestLinksCoordinatorTimetable> requestList = requestLinksCoordinatorTimetableRepository.findByUserId(userId);
        return nureScheduleService.getAllSchedules().stream()
                .map(scheduleDtoMapper::toDtoWithRequestedStatus)
                .map(scheduleWithRequestedStatusDto -> {
                    for (RequestLinksCoordinatorTimetable request : requestList) {
                        if (request.getKhnureTimetables().getKhnureTimetableId().equals(scheduleWithRequestedStatusDto.getId())
                                && request.getStatusRequest() != StatusRequest.DECLINED) {
                            scheduleWithRequestedStatusDto.setRequested(true);
                            return scheduleWithRequestedStatusDto;
                        }
                    }
                    scheduleWithRequestedStatusDto.setRequested(false);
                    return scheduleWithRequestedStatusDto;
                })
                .toList();
    }

    public Page<RequestLinksCoordinatorTimetable> getAllRequestLinksCoordinatorTimetable(PaginationDto paginationDto) {
        PageRequest pageRequest = PageRequest.of(paginationDto.getPage() - 1, paginationDto.getPageSize());
        return requestLinksCoordinatorTimetableRepository.findAll(pageRequest);
    }

    public RequestLinksCoordinatorTimetable getRequestById(Long requestId) {
        return requestLinksCoordinatorTimetableRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    @Transactional
    public void updateStatus(Long requestId, StatusRequest status) {
        RequestLinksCoordinatorTimetable request = getRequestById(requestId);
        List<StatusRequest> nextAvailableStatus = request.getStatusRequest().getNextAvailableStatus();
        if (!nextAvailableStatus.contains(status)) {
            throw new IllegalRequestStatusTransitionException(request.getId(), request.getStatusRequest(), status);
        }
        updateStatus(request, status);
        requestLinksCoordinatorTimetableRepository.saveAndFlush(request);
    }

    private void updateStatus(RequestLinksCoordinatorTimetable request, StatusRequest status) {
        if (StatusRequest.PROCESSED == status) {
            setLinksCoordinatorRoleForUser(request);
            setAccessForSpecifiedTimetable(request);
        }
        request.setStatusRequest(status);
    }

    private void setLinksCoordinatorRoleForUser(RequestLinksCoordinatorTimetable request) {
        User user = userService.findById(request.getUser().getId());
        if (user.getRole() == Role.USER) {
            user.setRole(Role.LINKS_COORDINATOR);
        }
        userService.save(user);
    }

    private void setAccessForSpecifiedTimetable(RequestLinksCoordinatorTimetable request) {
        Long khnureTimetableId = request.getKhnureTimetables().getId();
        User user = userService.findById(request.getUser().getId());
        user.addTimetablePermission(LinksCoordinatorTimetablePermission.builder()
                .khnureTimetableIid(khnureTimetableId)
                .userId(user.getId())
                .build());
        userService.save(user);
    }

    public RequestLinksCoordinatorTimetable getRequestLinksCoordinatorTimetableById(Long requestId) {
        Optional<RequestLinksCoordinatorTimetable> requestLinksCoordinatorTimetableOptional = requestLinksCoordinatorTimetableRepository.getRequestLinksCoordinatorTimetableById(requestId);
        requestLinksCoordinatorTimetableOptional.orElseThrow(() -> new RequestNotFoundException(requestId));
        return requestLinksCoordinatorTimetableOptional.get();
    }
}
