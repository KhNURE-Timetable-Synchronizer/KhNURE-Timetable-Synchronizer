package com.khnure.timetable.synchronizer.controller;

import com.khnure.timetable.synchronizer.dto.PaginationDto;
import com.khnure.timetable.synchronizer.dto.UserDto;
import com.khnure.timetable.synchronizer.mapper.UserMapper;
import com.khnure.timetable.synchronizer.model.User;
import com.khnure.timetable.synchronizer.service.UserService;
import com.khnure.timetable.synchronizer.validator.UserGroup;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-url}")
@Validated(User.class)
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/users")
    public ResponseEntity<?> getUsersWithPagination(
            @Validated({Default.class, UserGroup.class}) @ModelAttribute PaginationDto paginationDto) {
        Page<User> userPage = userService.getUsers(paginationDto);
        List<UserDto> userDtoList = userPage.getContent().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("totalUsersNumber", userPage.getTotalElements());
        response.put("totalPageNumber", userPage.getTotalPages());
        response.put("currentPageNumber", userPage.getNumber() + 1);
        response.put("users", userDtoList);

        return ResponseEntity.ok(response);
    }
}
