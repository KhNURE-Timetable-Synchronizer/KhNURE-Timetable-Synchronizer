package com.khnure.timetable.synchronizer.exception.handler;

import com.khnure.timetable.synchronizer.exception.DuplicateRequestException;
import com.khnure.timetable.synchronizer.exception.ScheduleNotFoundException;
import com.khnure.timetable.synchronizer.exception.response.CustomErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class RequestsHandler {
    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<CustomErrorResponse> handleDuplicateEntryException(DuplicateRequestException exception) {
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .status(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleScheduleNotFoundException(ScheduleNotFoundException exception) {
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }
}
