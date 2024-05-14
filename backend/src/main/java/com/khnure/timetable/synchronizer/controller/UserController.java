package com.khnure.timetable.synchronizer.controller;

import com.khnure.timetable.synchronizer.dto.ScheduleDto;
import com.khnure.timetable.synchronizer.dto.UserDto;
import com.khnure.timetable.synchronizer.mapper.UserMapper;
import com.khnure.timetable.synchronizer.model.User;
import com.khnure.timetable.synchronizer.service.UserService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-url}")
public class UserController {
    private final UserService userService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping( "/users")
    public ResponseEntity<?> getUsersWithPagination(
            @RequestParam(value = "page", defaultValue = "1", required = false) @Min(1) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "20", required = false) @Min(1) @Max(100)   Integer pageSize) {
        Page<User> userPage = userService.getUsers(page - 1, pageSize);
        List<UserDto> response = userPage.getContent().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
