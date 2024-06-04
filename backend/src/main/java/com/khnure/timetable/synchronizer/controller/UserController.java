package com.khnure.timetable.synchronizer.controller;

import com.khnure.timetable.synchronizer.dto.PaginationDto;
import com.khnure.timetable.synchronizer.dto.PaginationResponse;
import com.khnure.timetable.synchronizer.dto.UserDto;
import com.khnure.timetable.synchronizer.mapper.UserMapper;
import com.khnure.timetable.synchronizer.model.User;
import com.khnure.timetable.synchronizer.service.UserService;
import com.khnure.timetable.synchronizer.validator.UserGroup;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.AbstractMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-url}")
@Validated(User.class)
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/users")
    public PaginationResponse<UserDto> getUsersWithPagination(
            @Validated({Default.class, UserGroup.class}) @ModelAttribute PaginationDto paginationDto) {
        Page<User> userPage = userService.getUsers(paginationDto);
        List<UserDto> userDtoList = userPage.getContent().stream()
                .map(userMapper::toDto)
                .toList();

        return PaginationResponse.<UserDto>builder()
                .totalItemNumber(new AbstractMap.SimpleEntry<>("totalUsersNumber", userPage.getTotalElements()))
                .totalPageNumber(userPage.getTotalPages())
                .currentPageNumber(userPage.getNumber() + 1)
                .data(new AbstractMap.SimpleEntry<>("users", userDtoList))
                .build();
    }
}
