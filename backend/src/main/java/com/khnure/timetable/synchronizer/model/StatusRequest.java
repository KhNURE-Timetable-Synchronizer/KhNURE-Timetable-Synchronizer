package com.khnure.timetable.synchronizer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusRequest {
    CREATED("CREATED"),
    ON_PROCESSING("ON PROCESSING"),
    PROCESSED("PROCESSED"),
    DECLINED("DECLINED");

    private final String value;

    StatusRequest(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}