package com.khnure.timetable.synchronizer.mapper;

import com.khnure.timetable.synchronizer.dto.RequestLinksCoordinatorTimetableGetDto;
import com.khnure.timetable.synchronizer.model.RequestLinksCoordinatorTimetable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequestLinksCoordinatorTimetableMapper {
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "statusRequest", target = "status")
    @Mapping(source = "contactAccount", target = "telegramAccount")
    RequestLinksCoordinatorTimetableGetDto toDto(RequestLinksCoordinatorTimetable requestLinksCoordinatorTimetable);
}
