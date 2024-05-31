package com.khnure.timetable.synchronizer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestLinksCoordinatorTimetablePostDTO {
    @Pattern(regexp = "^[A-z0-9_]+$",message = "The telegram nickname only can contain A-z, 0-9 or _")
    @Size(min = 5, message = "The minimum length must be 5 characters")
    @JsonProperty("telegramAccount")
    String contactAccount;

    @NotNull(message = "KhNURE timetable's id must be presented.")
    Long khnureTimetableId;
}
