package com.khnure.timetable.synchronizer.dto;

import lombok.Data;

@Data
public class ScheduleWithRequestedStatusDto {
    private Long id;
    private String name;
    private TypeScheduleDto type;
    private boolean requested;
}