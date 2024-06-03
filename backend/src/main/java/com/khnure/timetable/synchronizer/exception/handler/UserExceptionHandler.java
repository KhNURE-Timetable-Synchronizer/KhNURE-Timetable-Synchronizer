package com.khnure.timetable.synchronizer.exception.handler;

import com.khnure.timetable.synchronizer.exception.RequestNotFoundException;
import com.khnure.timetable.synchronizer.exception.UserNotFoundException;
import com.khnure.timetable.synchronizer.exception.response.CustomErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler extends com.khnure.timetable.synchronizer.exception.handler.ExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleRequestNotFoundException(RequestNotFoundException exception) {
        return buildCustomErrorResponse(String.format("User with id \"%d\" not found", exception.getId()), HttpStatus.NOT_FOUND);
    }

}
