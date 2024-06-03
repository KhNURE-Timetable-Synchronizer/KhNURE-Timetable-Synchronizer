package com.khnure.timetable.synchronizer.exception.handler;

import com.khnure.timetable.synchronizer.exception.response.CustomErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public abstract class ExceptionHandler {
    protected static ResponseEntity<CustomErrorResponse> buildCustomErrorResponse(String exceptionMessage, HttpStatus httpStatus) {
        CustomErrorResponse customErrorResponse = CustomErrorResponse.builder()
                .error(httpStatus.getReasonPhrase())
                .status(httpStatus.value())
                .timestamp(LocalDateTime.now())
                .message(exceptionMessage)
                .build();
        return ResponseEntity.status(httpStatus)
                .body(customErrorResponse);
    }
}
