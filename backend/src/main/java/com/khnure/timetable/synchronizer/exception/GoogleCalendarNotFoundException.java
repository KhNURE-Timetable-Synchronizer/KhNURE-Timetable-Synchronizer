package com.khnure.timetable.synchronizer.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GoogleCalendarNotFoundException extends RuntimeException {
    private final Long calendarId;
}
