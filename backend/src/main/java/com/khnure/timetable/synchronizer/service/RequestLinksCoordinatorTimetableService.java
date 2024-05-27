package com.khnure.timetable.synchronizer.service;


import com.khnure.timetable.synchronizer.dto.RequestLinksCoordinatorTimetablePostDTO;
import com.khnure.timetable.synchronizer.dto.ScheduleDto;
import com.khnure.timetable.synchronizer.exception.DuplicateRequestException;
import com.khnure.timetable.synchronizer.exception.ScheduleNotFoundException;
import com.khnure.timetable.synchronizer.model.CustomUserDetails;
import com.khnure.timetable.synchronizer.model.KhnureTimetables;
import com.khnure.timetable.synchronizer.model.RequestLinksCoordinatorTimetable;
import com.khnure.timetable.synchronizer.model.StatusRequest;
import com.khnure.timetable.synchronizer.repository.RequestLinksCoordinatorTimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestLinksCoordinatorTimetableService {
    private final RequestLinksCoordinatorTimetableRepository requestLinksCoordinatorTimetableRepository;
    private final KhnureTimetablesService khnureTimetablesService;
    private final  NureScheduleService nureScheduleService;
    public RequestLinksCoordinatorTimetable save(RequestLinksCoordinatorTimetablePostDTO requestLinksCoordinatorTimetablePostDTO, CustomUserDetails userDetails) {
        Optional<ScheduleDto> timetableOptional = nureScheduleService.getScheduleById(requestLinksCoordinatorTimetablePostDTO.getKhnureTimetableId());
        if (!timetableOptional.isPresent()) {
            throw new ScheduleNotFoundException("Schedule not found");
        }

        KhnureTimetables khnureTimetables = khnureTimetablesService.addKhnureTimetable(timetableOptional.get().getName(), timetableOptional.get().getId()).get();




        RequestLinksCoordinatorTimetable requestLinksCoordinatorTimetable = RequestLinksCoordinatorTimetable.builder()
                .userId(userDetails.getUser().getId())
                .contactAccount(requestLinksCoordinatorTimetablePostDTO.getContactAccount())
                .khnureTimetables(khnureTimetables)
                .statusRequest(StatusRequest.CREATED)
                .build();


        Long khnureTimetablesId = requestLinksCoordinatorTimetable.getKhnureTimetables().getId();
        Long userId = requestLinksCoordinatorTimetable.getUserId();
        boolean exists = requestLinksCoordinatorTimetableRepository.existsByUserIdAndKhnureTimetablesIdAndStatusNot(userId, khnureTimetablesId, StatusRequest.DECLINED);
        if (exists) {
            throw new DuplicateRequestException("There is already a request for schedule \"" + requestLinksCoordinatorTimetable.getKhnureTimetables().getName() + "\"");
        }
        return requestLinksCoordinatorTimetableRepository.save(requestLinksCoordinatorTimetable);
    }
}
