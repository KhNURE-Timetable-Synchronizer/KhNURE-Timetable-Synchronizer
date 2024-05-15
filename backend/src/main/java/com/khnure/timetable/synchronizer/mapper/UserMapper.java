package com.khnure.timetable.synchronizer.mapper;

import com.khnure.timetable.synchronizer.dto.UserDto;
import com.khnure.timetable.synchronizer.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
