package com.khnure.timetable.synchronizer.exception;

import lombok.Data;

@Data
public class GoogleAuthenticationException extends RuntimeException{
    private final int statusCode;
    public GoogleAuthenticationException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}
