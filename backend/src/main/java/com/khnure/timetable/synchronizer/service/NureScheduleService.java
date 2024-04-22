package com.khnure.timetable.synchronizer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khnure.timetable.synchronizer.dto.GroupAPIDto;
import com.khnure.timetable.synchronizer.dto.ScheduleDto;
import com.khnure.timetable.synchronizer.dto.TeacherAPIDto;
import com.khnure.timetable.synchronizer.dto.TypeScheduleDto;
import com.khnure.timetable.synchronizer.mapper.ScheduleDtoMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NureScheduleService {
    @Value("${schedule-api-url}")
    private String scheduleApiUrl;
    private final HttpClient httpClient;

    private final ObjectMapper objectMapper;
    private final ScheduleDtoMapper scheduleMapper;
    private static final String SCHEDULE_API = "/schedule";
    private static final String GROUP_API = "/groups";
    private static final String TEACHERS_API = "/teachers";

    private String getUrl(String api) {
        return scheduleApiUrl + api;
    }

    public List<ScheduleDto> getGroups() {

        List<GroupAPIDto> groupsAPI = getBaseSchedule(GROUP_API, TypeScheduleDto.GROUP);
        return groupsAPI.stream()
                        .map(scheduleMapper::toDto)
                        .sorted(Comparator.comparing(ScheduleDto::getName))
                        .toList();
    }

    public List<ScheduleDto> getTeachers() {
        List<TeacherAPIDto> teachersAPI = getBaseSchedule(TEACHERS_API, TypeScheduleDto.TEACHER);
        return teachersAPI.stream()
                        .map(scheduleMapper::toDto)
                        .sorted(Comparator.comparing(ScheduleDto::getName))
                        .toList();
    }


    public <T> List<T> getBaseSchedule(String url, TypeScheduleDto type) {
        try {
            HttpResponse httpResponse = httpClient.execute(new HttpGet(getUrl(url)));
            InputStream content = httpResponse.getEntity().getContent();

            Class<T> clazz = switch (type) {
                case GROUP -> (Class<T>) GroupAPIDto.class;
                case TEACHER -> (Class<T>) TeacherAPIDto.class;
                default -> throw new RuntimeException("Not found type Schedule DTO");
            };

            TypeReference<List<T>> typeReference = new TypeReference<>() {
                @Override
                public Type getType() {
                    return objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
                }
            };

            return objectMapper.readValue(content, typeReference);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
