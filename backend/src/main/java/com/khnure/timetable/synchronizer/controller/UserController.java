package com.khnure.timetable.synchronizer.controller;

import com.khnure.timetable.synchronizer.dto.UserDto;
import com.khnure.timetable.synchronizer.mapper.UserMapper;
import com.khnure.timetable.synchronizer.model.User;
import com.khnure.timetable.synchronizer.service.UserService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-url}")
@Validated
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/users")
    public ResponseEntity<?> getUsersWithPagination(
            @RequestParam(value = "page", defaultValue = "1", required = false) @Min(value = 1, message = "Page must start from 1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "20", required = false) @Min(value = 1, message = "Users per page must be greater 0") @Max(value = 100, message = "Users per page can be max 100") Integer pageSize) {
        Page<User> userPage = userService.getUsers(page - 1, pageSize);
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
