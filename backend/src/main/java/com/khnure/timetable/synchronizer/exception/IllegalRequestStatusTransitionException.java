package com.khnure.timetable.synchronizer.exception;

import com.khnure.timetable.synchronizer.model.StatusRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IllegalRequestStatusTransitionException extends RuntimeException{
    private final Long requestId;
    private final StatusRequest currentRequestStatus;
    private final StatusRequest specifiedRequestStatus;
}
