package com.khnure.timetable.synchronizer.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimetableExportDto {
    private Long timetableId;

    private TypeScheduleDto typeScheduleDto;

    @JsonSerialize(using = LocalDateSerializer.class)
    @NotNull(message = "Start date must be presented.")
    private LocalDate startDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    @NotNull(message = "End date must be presented.")
    private LocalDate endDate;

    public void setTimetableType(String timetableType) {
        this.typeScheduleDto = TypeScheduleDto.getInstance(timetableType);
    }

}
