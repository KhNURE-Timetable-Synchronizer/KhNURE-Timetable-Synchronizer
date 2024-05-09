package com.khnure.timetable.synchronizer.exception.handler;

import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.khnure.timetable.synchronizer.exception.GoogleAuthenticationException;
import com.khnure.timetable.synchronizer.exception.GoogleCalendarNotFoundException;
import com.khnure.timetable.synchronizer.exception.response.CustomErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GoogleExceptionHandler {

    @ExceptionHandler({GoogleJsonResponseException.class})
    public ResponseEntity<?> handleGoogleResponseException(GoogleJsonResponseException exception) {
        log.error("Google error occurred", exception);
        if (exception.getDetails().getCode() == 401) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your google access token was incorrect or expired. Create a new one using /jwt/create");
        }
        log.warn("Unknown exception from google!", exception);
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .message("Unknown exception from google side. Contact with support.")
                .build();
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler({GoogleAuthenticationException.class})
    public ResponseEntity<?> handleGoogleResponseException(GoogleAuthenticationException exception) {
        log.error("Google error occurred", exception);
        if (exception.getStatusCode() == 400) {
            CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                    .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .timestamp(LocalDateTime.now())
                    .message("Your google code token was incorrect or expired.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        log.warn("Unknown exception from google!", exception);
        return ResponseEntity.internalServerError().body("Unknown exception from google side. Contact with support.");
    }

    @ExceptionHandler(GoogleCalendarNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleGoogleCalendarNotFoundException(GoogleCalendarNotFoundException exception) {
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .message(String.format("Google calendar with id %s wasn't found", exception.getCalendarId()))
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(TokenResponseException.class)
    public ResponseEntity<CustomErrorResponse> handleTokenResponseException(TokenResponseException exception){
        log.warn("Token exception", exception);
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(LocalDateTime.now())
                .message("Server couldn't refresh your Google token. Please, reauthorize using /jwt/create endpoint")
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }
}
