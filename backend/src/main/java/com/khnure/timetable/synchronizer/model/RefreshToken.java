package com.khnure.timetable.synchronizer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class RefreshToken {
    private String token;
    private LocalDateTime expiredAt;
}
