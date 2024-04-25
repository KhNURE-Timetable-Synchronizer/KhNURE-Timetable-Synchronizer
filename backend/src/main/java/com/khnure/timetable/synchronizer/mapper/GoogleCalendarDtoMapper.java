package com.khnure.timetable.synchronizer.mapper;

import com.khnure.timetable.synchronizer.dto.GoogleCalendarDto;
import com.khnure.timetable.synchronizer.model.GoogleCalendar;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GoogleCalendarDtoMapper {
     GoogleCalendarDto toDto(GoogleCalendar googleCalendar);
}
