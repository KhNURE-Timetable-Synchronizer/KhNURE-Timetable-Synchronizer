package com.khnure.timetable.synchronizer.mapper;

import com.khnure.timetable.synchronizer.dto.GroupAPIDto;
import com.khnure.timetable.synchronizer.model.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {
	Group toModel(GroupAPIDto groupDto);
}
