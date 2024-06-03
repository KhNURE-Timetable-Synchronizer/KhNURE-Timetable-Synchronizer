package com.khnure.timetable.synchronizer.exception.handler;

import com.khnure.timetable.synchronizer.exception.DuplicateRequestException;
import com.khnure.timetable.synchronizer.exception.RequestNotFoundException;
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
public class RequestsExceptionHandler {
    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<CustomErrorResponse> handleDuplicateEntryException(DuplicateRequestException exception) {
        return buildCustomErrorResponse(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleScheduleNotFoundException(ScheduleNotFoundException exception) {
        return buildCustomErrorResponse(String.format("Schedule with id %d not found", exception.getId()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleRequestNotFoundException(RequestNotFoundException exception) {
        return buildCustomErrorResponse(String.format("Request with id %d not found", exception.getId()), HttpStatus.NOT_FOUND);
    }

    private static ResponseEntity<CustomErrorResponse> buildCustomErrorResponse(String exceptionMessage, HttpStatus httpStatus) {
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
