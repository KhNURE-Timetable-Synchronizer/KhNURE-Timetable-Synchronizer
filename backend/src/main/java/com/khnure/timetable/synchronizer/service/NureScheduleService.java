package com.khnure.timetable.synchronizer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khnure.timetable.synchronizer.dto.GroupAPIDto;
import com.khnure.timetable.synchronizer.dto.ScheduleDto;
import com.khnure.timetable.synchronizer.dto.TeacherAPIDto;
import com.khnure.timetable.synchronizer.mapper.ScheduleDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
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
    private static final String GROUP_API = "/groups";
    private static final String TEACHERS_API = "/teachers";

    private String getUrl(String api) {
        return scheduleApiUrl + api;
    }

    public List<ScheduleDto> getGroups() {

        List<GroupAPIDto> groupsAPI = getBaseSchedule(GROUP_API, GroupAPIDto.class);
        return groupsAPI.stream()
                        .map(scheduleMapper::toDto)
                        .sorted(Comparator.comparing(ScheduleDto::getName))
                        .toList();
    }

    public List<ScheduleDto> getTeachers() {
        List<TeacherAPIDto> teachersAPI = getBaseSchedule(TEACHERS_API, TeacherAPIDto.class);
        return teachersAPI.stream()
                .map(scheduleMapper::toDto)
                .sorted(Comparator.comparing(ScheduleDto::getName))
                .toList();
    }

    public <T> List<T> getBaseSchedule(String url, Class<T> type) {
        try {
            HttpResponse httpResponse = httpClient.execute(new HttpGet(getUrl(url)));
            InputStream content = httpResponse.getEntity().getContent();

            TypeReference<List<T>> typeReference = new TypeReference<>() {
                @Override
                public Type getType() {
                    return objectMapper.getTypeFactory().constructCollectionType(List.class, type);
                }
            };

            return objectMapper.readValue(content, typeReference);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
