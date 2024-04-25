package com.khnure.timetable.synchronizer.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.calendar.Calendar;
import com.khnure.timetable.synchronizer.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class CalendarHelper {

    @Value("${google.application.name}")
    private String applicationName;

    private final JsonFactory jsonFactory;
    private final HttpTransport httpTransport;
    private final ConcurrentHashMap<Long, Calendar> calendarsMap;

    public CalendarHelper(JsonFactory jsonFactory, HttpTransport httpTransport) {
        this.jsonFactory = jsonFactory;
        this.httpTransport = httpTransport;
        this.calendarsMap = new ConcurrentHashMap<>();
    }

    public Calendar getUserCalendar(Long userId) {
        return calendarsMap.get(userId);
    }
    public Calendar getUserCalendar(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        CustomUserDetails customUserDetails = (CustomUserDetails) securityContext.getAuthentication().getDetails();

        return getUserCalendar(customUserDetails.getUser().getId());
    }

    public boolean userHasCalendar(Long userId) {
        return calendarsMap.containsKey(userId);
    }

    public Calendar createCalendarForUser(Long userId, GoogleCredential googleCredential) {
        Calendar calendar = new Calendar.Builder(httpTransport, jsonFactory, googleCredential)
                .setApplicationName(applicationName)
                .build();

        calendarsMap.put(userId, calendar);
        return calendar;
    }
}