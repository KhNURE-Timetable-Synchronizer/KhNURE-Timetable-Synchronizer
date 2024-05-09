package com.khnure.timetable.synchronizer.service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.khnure.timetable.synchronizer.dto.GoogleCalendarDto;
import com.khnure.timetable.synchronizer.dto.TimetableExportDto;
import com.khnure.timetable.synchronizer.exception.GoogleCalendarDeleteException;
import com.khnure.timetable.synchronizer.exception.GoogleCalendarNotFoundException;
import com.khnure.timetable.synchronizer.mapper.GoogleCalendarDtoMapper;
import com.khnure.timetable.synchronizer.model.GoogleCalendar;
import com.khnure.timetable.synchronizer.model.Group;
import com.khnure.timetable.synchronizer.model.Pair;
import com.khnure.timetable.synchronizer.model.Teacher;
import com.khnure.timetable.synchronizer.repository.GoogleCalendarRepository;
import com.khnure.timetable.synchronizer.util.CalendarHelper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class GoogleCalendarService {
    private final CalendarHelper calendarHelper;
    private final NureScheduleService nureScheduleService;
    private final GoogleCalendarRepository googleCalendarRepository;
    private final Map<String, String> pairTypeColorMap;
    private final GoogleCalendarDtoMapper googleCalendarDtoMapper;

    public GoogleCalendarService(CalendarHelper calendarHelper, NureScheduleService nureScheduleService, GoogleCalendarRepository googleCalendarRepository, GoogleCalendarDtoMapper googleCalendarDtoMapper) {
        this.calendarHelper = calendarHelper;
        this.nureScheduleService = nureScheduleService;
        this.googleCalendarRepository = googleCalendarRepository;
        this.googleCalendarDtoMapper = googleCalendarDtoMapper;
        this.pairTypeColorMap = Map.of(
                "Лк", "5",
                "Лб", "10",
                "Пз", "10",
                "Зал", "6",
                "Конс", "7",
                "Екз", "11"
        );
    }

    public void export(Long userId, TimetableExportDto timetableExportDto) {
        String timetableName = nureScheduleService.getScheduleNameByIdAndType(timetableExportDto.getTimetableId(), timetableExportDto.getTypeScheduleDto());

        try {
            Calendar calendar = calendarHelper.getUserCalendar();

            var newCalendar = new com.google.api.services.calendar.model.Calendar()
                    .setSummary(timetableName)
                    .setTimeZone("Europe/Kiev");
            var createdCalendar = calendar.calendars().insert(newCalendar).execute();

            googleCalendarRepository.save(GoogleCalendar.builder()
                    .name(timetableName)
                    .calendarId(createdCalendar.getId())
                    .userId(userId)
                    .build());


            List<Pair> pairs = nureScheduleService.getSchedule(timetableExportDto);
            for (Pair pair : pairs) {
                calendar.events().insert(createdCalendar.getId(), getEvent(pair)).execute();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Event getEvent(Pair pair) {
        DateTime pairStartTime = new DateTime(Date.from(pair.getStartTime().atZone(ZoneId.of("Europe/Kiev")).toInstant()), TimeZone.getTimeZone(ZoneId.of("Europe/Kiev")));
        DateTime pairEndTime = new DateTime(ZonedDateTime.of(pair.getEndTime(), ZoneId.of("Europe/Kiev")).toInstant().toEpochMilli());

        EventDateTime startTime = new EventDateTime().setDateTime(pairStartTime);
        EventDateTime endTime = new EventDateTime().setDateTime(pairEndTime);


        Event event = new Event()
                .setStart(startTime)
                .setSummary(String.format("%s - %s. %s", pair.getSubject().getBrief(), pair.getType(), pair.getSubject().getTitle()))
                .setLocation(pair.getAuditory()) // todo add links here
                .setDescription(getDescription(pair))
                .setColorId(getColor(pair))
                .setEnd(endTime);

        return event;
    }

    private String getColor(Pair pair) {
        return pairTypeColorMap.getOrDefault(pair.getType(), "11");
    }

    protected String getDescription(Pair pair) {
        StringBuilder description = new StringBuilder();
        description.append("Викладач(-і): ");
        Iterator<Teacher> teacherIterator = pair.getTeacherList().iterator();
        while (teacherIterator.hasNext()) {
            description.append(teacherIterator.next().getFullName());
            if (!teacherIterator.hasNext()) {
                break;
            }
            description.append("; ");
        }
        description.append("\n");
        description.append("Група(-и): ");
        Iterator<Group> groupIterator = pair.getGroupList().iterator();
        while (groupIterator.hasNext()) {
            description.append(groupIterator.next().getName());
            if (!groupIterator.hasNext()) {
                break;
            }
            description.append("; ");
        }
        return description.toString();
    }

    public List<GoogleCalendarDto> getTimetablesByUserId(Long userId) {
        List<GoogleCalendar> optionalGoogleCalendar = googleCalendarRepository.findByUserId(userId);
        List<GoogleCalendarDto> calendarDtoList = optionalGoogleCalendar.stream()
                .map(googleCalendarDtoMapper::toDto)
                .toList();
        return calendarDtoList;
    }

    @Transactional
    public void deleteCalendarByIdAndUserId(Long id, Long userId) {
        String calendarId = googleCalendarRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new GoogleCalendarNotFoundException(id))
                .getCalendarId();
        googleCalendarRepository.deleteByIdAndUserId(id, userId);

        Calendar calendar = calendarHelper.getUserCalendar();
        try {
            calendar.calendarList().delete(calendarId).execute();
        }
        catch (GoogleJsonResponseException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                return;
            }
            throw new GoogleCalendarDeleteException(exception);
        }catch (IOException exception) {
            throw new GoogleCalendarDeleteException(exception);
        }
    }
}
