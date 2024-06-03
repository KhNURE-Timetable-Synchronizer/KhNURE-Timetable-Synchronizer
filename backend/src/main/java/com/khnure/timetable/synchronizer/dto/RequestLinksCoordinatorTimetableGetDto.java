package com.khnure.timetable.synchronizer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.khnure.timetable.synchronizer.model.KhnureTimetables;
import com.khnure.timetable.synchronizer.model.StatusRequest;
import lombok.Data;

@Data
public class RequestLinksCoordinatorTimetableGetDto {
    private Long id;
    private String email;
    @JsonProperty("requestedTimetable")
    private KhnureTimetables khnureTimetables;
    private StatusRequest status;
    private String telegramAccount;
}
