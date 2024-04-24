package com.khnure.timetable.synchronizer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TypeScheduleDto {
    GROUP("group"),
    TEACHER("teacher");

    private String value;
    TypeScheduleDto(String value) {
        this.value = value;
    }
    @JsonValue
    public String getValue() {
        return value;
    }
}
