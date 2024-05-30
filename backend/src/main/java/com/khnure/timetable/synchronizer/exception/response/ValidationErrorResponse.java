package com.khnure.timetable.synchronizer.exception.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ValidationErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private List<FieldValidationError> details;

    @Data
    @AllArgsConstructor
    @Builder
    public static class FieldValidationError{
        private String message;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String field;
    }
}

