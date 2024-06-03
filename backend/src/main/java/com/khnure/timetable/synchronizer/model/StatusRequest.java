package com.khnure.timetable.synchronizer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Collections;
import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusRequest {
    CREATED("CREATED") {
        @Override
        public List<StatusRequest> getNextAvailableStatus() {
            return List.of(ON_PROCESSING);
        }
    },
    ON_PROCESSING("ON PROCESSING") {
        @Override
        public List<StatusRequest> getNextAvailableStatus() {
            return List.of(PROCESSED, DECLINED);
        }
    },
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

    public List<StatusRequest> getNextAvailableStatus(){
        return Collections.emptyList();
    }
}