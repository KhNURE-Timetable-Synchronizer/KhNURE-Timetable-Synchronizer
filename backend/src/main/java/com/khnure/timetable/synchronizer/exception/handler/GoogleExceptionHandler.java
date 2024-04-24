package com.khnure.timetable.synchronizer.exception.handler;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GoogleExceptionHandler {

    @ExceptionHandler({GoogleJsonResponseException.class})
    public ResponseEntity<String> handleGoogleResponseException(GoogleJsonResponseException exception) {
        log.error("Google error occurred", exception);
        if (exception.getDetails().getCode() == 401) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your google access token was incorrect or expired. Create a new one using /jwt/create");
        }
        log.warn("Unknown exception from google!", exception);
        return ResponseEntity.internalServerError().body("Unknown exception from google side. Contact with support.");
    }
}
