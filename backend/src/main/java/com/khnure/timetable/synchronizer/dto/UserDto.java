package com.khnure.timetable.synchronizer.dto;

import com.khnure.timetable.synchronizer.model.Role;
import lombok.Data;

@Data

public class UserDto {
    private Long id;
    private String email;
    private Role role;
}
