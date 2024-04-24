package com.khnure.timetable.synchronizer.dto;

import lombok.Data;

@Data
public class ScheduleDto {
    private String id;
    private String name;
    private TypeScheduleDto type;
}