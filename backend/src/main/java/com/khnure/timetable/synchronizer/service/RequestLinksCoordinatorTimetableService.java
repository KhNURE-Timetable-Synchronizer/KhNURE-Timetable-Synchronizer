package com.khnure.timetable.synchronizer.service;


import com.khnure.timetable.synchronizer.dto.RequestLinksCoordinatorTimetablePostDTO;
import com.khnure.timetable.synchronizer.dto.ScheduleDto;
import com.khnure.timetable.synchronizer.dto.ScheduleWithRequestedStatusDto;
import com.khnure.timetable.synchronizer.exception.DuplicateRequestException;
import com.khnure.timetable.synchronizer.exception.ScheduleNotFoundException;
import com.khnure.timetable.synchronizer.mapper.ScheduleDtoMapper;
import com.khnure.timetable.synchronizer.model.KhnureTimetables;
import com.khnure.timetable.synchronizer.model.RequestLinksCoordinatorTimetable;
import com.khnure.timetable.synchronizer.model.StatusRequest;
import com.khnure.timetable.synchronizer.repository.RequestLinksCoordinatorTimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestLinksCoordinatorTimetableService {
    private final RequestLinksCoordinatorTimetableRepository requestLinksCoordinatorTimetableRepository;
    private final KhnureTimetablesService khnureTimetablesService;
    private final NureScheduleService nureScheduleService;
    private final ScheduleDtoMapper scheduleDtoMapper;

    public RequestLinksCoordinatorTimetable save(RequestLinksCoordinatorTimetablePostDTO requestLinksCoordinatorTimetablePostDTO, Long userId) {
        Optional<ScheduleDto> timetableOptional = nureScheduleService.getScheduleById(requestLinksCoordinatorTimetablePostDTO.getKhnureTimetableId());
        if (timetableOptional.isEmpty()) {
            throw new ScheduleNotFoundException(requestLinksCoordinatorTimetablePostDTO.getKhnureTimetableId());
        }

        KhnureTimetables khnureTimetables = khnureTimetablesService.addKhnureTimetable(timetableOptional.get().getName(), timetableOptional.get().getId()).get();

        RequestLinksCoordinatorTimetable requestLinksCoordinatorTimetable = RequestLinksCoordinatorTimetable.builder()
                .userId(userId)
                .contactAccount(requestLinksCoordinatorTimetablePostDTO.getContactAccount())
                .khnureTimetables(khnureTimetables)
                .statusRequest(StatusRequest.CREATED)
                .build();

        Long khnureTimetablesId = requestLinksCoordinatorTimetable.getKhnureTimetables().getId();
        Optional<RequestLinksCoordinatorTimetable> request = requestLinksCoordinatorTimetableRepository.findByUserIdAndKhnureTimetableId(userId, khnureTimetablesId);
        if (request.isPresent() && request.get().getStatusRequest() != StatusRequest.DECLINED) {
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
}
