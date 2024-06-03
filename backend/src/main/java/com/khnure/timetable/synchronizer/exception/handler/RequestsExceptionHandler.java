package com.khnure.timetable.synchronizer.exception.handler;

import com.khnure.timetable.synchronizer.exception.DuplicateRequestException;
import com.khnure.timetable.synchronizer.exception.IllegalRequestStatusTransitionException;
import com.khnure.timetable.synchronizer.exception.RequestNotFoundException;
import com.khnure.timetable.synchronizer.exception.RequestNotFoundException;
import com.khnure.timetable.synchronizer.exception.ScheduleNotFoundException;
import com.khnure.timetable.synchronizer.exception.response.CustomErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RequestsExceptionHandler extends com.khnure.timetable.synchronizer.exception.handler.ExceptionHandler{
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

    @ExceptionHandler(IllegalRequestStatusTransitionException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalRequestStatusTransitionException(IllegalRequestStatusTransitionException exception) {
        String message = String.format("Request with id \"%d\" can't make a transition from current status \"%s\" to specified status \"%s\"",
                exception.getRequestId(), exception.getCurrentRequestStatus(), exception.getSpecifiedRequestStatus());
        return buildCustomErrorResponse(message, HttpStatus.CONFLICT);
    }
}
