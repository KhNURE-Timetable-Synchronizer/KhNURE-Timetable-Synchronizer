package com.khnure.timetable.synchronizer.exception;

import lombok.Getter;

@Getter
public class ScheduleNotFoundException extends RuntimeException {
    private final Long id;

    public ScheduleNotFoundException(Long id) {
        this.id = id;
    }
}
