package com.khnure.timetable.synchronizer.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class GoogleCalendarDto {
    private Long id;
    @JsonAlias("google_calendar_id")
    private String googleCalendarId;
    private String name;
    @JsonAlias("khnure_timetable_id")
    private Long khnureTimetableId;}
