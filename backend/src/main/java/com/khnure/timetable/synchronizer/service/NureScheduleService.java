package com.khnure.timetable.synchronizer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khnure.timetable.synchronizer.dto.*;
import com.khnure.timetable.synchronizer.mapper.PairMapper;
import com.khnure.timetable.synchronizer.mapper.ScheduleDtoMapper;
import com.khnure.timetable.synchronizer.model.Pair;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
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
    private final PairMapper pairMapper;

    private static final ZoneId ZONE_ID = ZoneId.of("Europe/Kiev");
    private static final String GROUP_API = "/groups";
    private static final String TEACHERS_API = "/teachers";
    private static final String SCHEDULE_API = "/schedule";


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

    public String getScheduleNameByIdAndType(Long id, TypeScheduleDto typeScheduleDto){
        if (typeScheduleDto.equals(TypeScheduleDto.GROUP)) {
            return getGroupScheduleNameById(id);
        }
        if (typeScheduleDto.equals(TypeScheduleDto.TEACHER)){
            return getTeacherScheduleNameById(id);
        }
        throw new RuntimeException("Not defined schedule type");
    }
    public String getGroupScheduleNameById(Long id){
        return getGroups().stream()
                .filter(scheduleDto ->  scheduleDto.getId().equals(String.valueOf(id)))
                .map(ScheduleDto::getName)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Group by id {%d} didn't found", id)));
    }

    public String getTeacherScheduleNameById(Long id){
        return getTeachers().stream()
                .filter(scheduleDto ->  scheduleDto.getId().equals(String.valueOf(id)))
                .map(ScheduleDto::getName)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Group by id {%d} didn't found", id)));
    }

    public List<Pair> getSchedule(TimetableExportDto timetableExportDto) {
        try {
            HttpGet request = new HttpGet(getUrl(SCHEDULE_API));
            URI uri = new URIBuilder(request.getURI())
                    .addParameter("id", String.valueOf(timetableExportDto.getTimetableId()))
                    .addParameter("type", "group")
                    .addParameter("start_time", String.valueOf(timetableExportDto.getStartDate().atStartOfDay(ZONE_ID).toEpochSecond()))
                    .addParameter("end_time", String.valueOf(timetableExportDto.getEndDate().atStartOfDay(ZONE_ID).toEpochSecond()))
                    .build();
            request.setURI(uri);
            HttpResponse httpResponse = httpClient.execute(request);
            InputStream content = httpResponse.getEntity().getContent();
            List<PairApiDto> pairList = objectMapper.readValue(content, new TypeReference<>() {
            });
            return pairList.stream()
                    .map(pairMapper::toModel)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }}
