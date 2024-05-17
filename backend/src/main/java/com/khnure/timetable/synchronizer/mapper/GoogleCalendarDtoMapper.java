package com.khnure.timetable.synchronizer.mapper;

import com.khnure.timetable.synchronizer.dto.GoogleCalendarDto;
import com.khnure.timetable.synchronizer.model.GoogleCalendar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GoogleCalendarDtoMapper {
     @Mapping(source = "calendarId", target = "googleCalendarId")
     @Mapping(source = "khnureTimetables.name", target = "name")
     @Mapping(source = "khnureTimetables.khnureTimetableId", target = "khnureTimetableId")
     GoogleCalendarDto toDto(GoogleCalendar googleCalendar);
}
