package com.khnure.timetable.synchronizer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TypeScheduleDto {
    GROUP("group"),
    TEACHER("teacher");

    private final String value;

    TypeScheduleDto(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
    public static TypeScheduleDto getInstance(String value){
        return Arrays.stream(TypeScheduleDto.values())
                .filter(typeScheduleDto -> typeScheduleDto.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can't find enum value for passed" + value));
    }

}
