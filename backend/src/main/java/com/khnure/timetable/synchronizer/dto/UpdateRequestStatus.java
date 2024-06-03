package com.khnure.timetable.synchronizer.dto;

import com.khnure.timetable.synchronizer.model.StatusRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRequestStatus {
    @NotNull(message = "Status must be presented")
    private StatusRequest status;
}
