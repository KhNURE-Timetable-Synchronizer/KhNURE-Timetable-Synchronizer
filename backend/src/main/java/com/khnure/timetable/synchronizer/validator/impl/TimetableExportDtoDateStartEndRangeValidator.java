package com.khnure.timetable.synchronizer.validator.impl;

import com.khnure.timetable.synchronizer.dto.TimetableExportDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class TimetableExportDtoDateStartEndRangeValidator implements Validator {

    private static final String END_DATE_FIELD_NAME = "endDate";
    private static final String DATE_MUST_BE_AFTER_START_DATE_VALUE  = "End date must be after or equal to start date";

    @Override
    public boolean supports(Class<?> clazz) {
        return TimetableExportDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TimetableExportDto dto = (TimetableExportDto) target;
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();

        if (endDate != null && startDate != null && startDate.isAfter(endDate)) {
            errors.rejectValue(END_DATE_FIELD_NAME, "", DATE_MUST_BE_AFTER_START_DATE_VALUE);
        }
    }
}