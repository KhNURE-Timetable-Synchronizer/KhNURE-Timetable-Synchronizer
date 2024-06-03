package com.khnure.timetable.synchronizer.exception;

import lombok.Getter;

@Getter
public class RequestNotFoundException extends RuntimeException {
    private final Long id;

    public RequestNotFoundException(Long id) {
        this.id = id;
    }
}
