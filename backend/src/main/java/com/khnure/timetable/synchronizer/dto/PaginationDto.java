package com.khnure.timetable.synchronizer.dto;

import com.khnure.timetable.synchronizer.validator.RequestLinksCoordinatorTimetableGroup;
import com.khnure.timetable.synchronizer.validator.UserGroup;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;




@Data
public class PaginationDto {

    @NotNull(message = "Page number cannot be null", groups = Default.class)
    @Min(value = 1, message = "Page must start from 1", groups = Default.class)
    private Integer page;

    @NotNull(message = "Page size cannot be null", groups = Default.class)

    @Min(value = 1, message = "Request per page must be greater than 0", groups = RequestLinksCoordinatorTimetableGroup.class)
    @Max(value = 100, message = "Request per page can be max 100", groups = RequestLinksCoordinatorTimetableGroup.class)

    @Min(value = 1, message = "Users per page must be greater than 0", groups = UserGroup.class)
    @Max(value = 100, message = "Users per page can be max 100", groups = UserGroup.class)
    private Integer pageSize;
}
