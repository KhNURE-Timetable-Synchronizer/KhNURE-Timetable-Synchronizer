package com.khnure.timetable.synchronizer.exception.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.khnure.timetable.synchronizer.exception.response.ValidationErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@ControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ValidationErrorResponse.FieldValidationError> details = ex.getConstraintViolations().stream()
                .map(error -> ValidationErrorResponse.FieldValidationError.builder()
                        .field(error.getPropertyPath().toString().split("\\.")[1])
                        .message(error.getMessage())
                        .build()).toList();

        return getValidationResponse(details);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<ValidationErrorResponse.FieldValidationError> details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> ValidationErrorResponse.FieldValidationError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        return getValidationResponse(details);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (exception.getCause() instanceof InvalidFormatException invalidFormatException) {
            List<ValidationErrorResponse.FieldValidationError> details = List.of(ValidationErrorResponse.FieldValidationError.builder()
                    .field(invalidFormatException.getPath().get(0).getFieldName()) //can be only one error at a time
                    .message("Invalid format or type of passed value. Check type, value and format.")
                    .build());
            return getValidationResponse(details);
        }
        if (exception.getCause() instanceof MismatchedInputException mismatchedInputException) {
            if (!mismatchedInputException.getPath().isEmpty()) {
                List<ValidationErrorResponse.FieldValidationError> details = List.of(ValidationErrorResponse.FieldValidationError.builder()
                        .field(mismatchedInputException.getPath().get(0).getFieldName()) //can be only one error at a time
                        .message("Invalid type of passed value. Check type and format.")
                        .build());
                return getValidationResponse(details);
            }
        }
        if (exception.getCause() instanceof JsonParseException jsonParseException) {
            String passedValue = jsonParseException.getMessage().split("'")[1];
            List<ValidationErrorResponse.FieldValidationError> details = List.of(ValidationErrorResponse.FieldValidationError.builder()
                    .message(String.format("Invalid type of passed value. Check type and format for value '%s'", passedValue))
                    .build());
            return getValidationResponse(details);
        }


        if (exception.getCause() instanceof ConversionFailedException conversionFailedException) {
            List<ValidationErrorResponse.FieldValidationError> details = List.of(ValidationErrorResponse.FieldValidationError.builder()
                    .message(String.format("Invalid format or type of passed value. Check type, value and format. %s", conversionFailedException.getSourceType()))
                    .build());
            return getValidationResponse(details);
        }
        log.warn("Unknown exception occurred in HttpMessageNotReadableException", exception);
        return super.handleHttpMessageNotReadable(exception, headers, status, request);
    }


    private static ResponseEntity<Object> getValidationResponse(List<ValidationErrorResponse.FieldValidationError> details) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ValidationErrorResponse validationErrorResponse = ValidationErrorResponse.builder()
                .error(httpStatus.getReasonPhrase())
                .status(httpStatus.value())
                .timestamp(LocalDateTime.now())
                .details(details)
                .build();

        return new ResponseEntity<>(validationErrorResponse, httpStatus);
    }

}
