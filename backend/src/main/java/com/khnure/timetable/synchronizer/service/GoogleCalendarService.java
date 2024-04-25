package com.khnure.timetable.synchronizer.service;

import com.khnure.timetable.synchronizer.dto.GoogleCalendarDto;
import com.khnure.timetable.synchronizer.mapper.GoogleCalendarDtoMapper;
import com.khnure.timetable.synchronizer.model.GoogleCalendar;
import com.khnure.timetable.synchronizer.repository.GoogleCalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleCalendarService {
    private final GoogleCalendarRepository googleCalendarRepository;
    private final GoogleCalendarDtoMapper googleCalendarDtoMapper;

    public List<GoogleCalendarDto> getTimetablesByUserId(Long userId) {
        List<GoogleCalendar> optionalGoogleCalendar = googleCalendarRepository.findByUserId(userId);
        List<GoogleCalendarDto> calendarDtoList = optionalGoogleCalendar.stream()
                .map(googleCalendarDtoMapper::toDto)
                .toList();
        return calendarDtoList;
    }
}
