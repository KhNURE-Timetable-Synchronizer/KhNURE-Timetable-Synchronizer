package com.khnure.timetable.synchronizer.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class TeacherAPIDto {
    private String id;
    @JsonAlias("full_name")
    private String fullName;
    @JsonAlias("short_name")
    private String shortName;
}
