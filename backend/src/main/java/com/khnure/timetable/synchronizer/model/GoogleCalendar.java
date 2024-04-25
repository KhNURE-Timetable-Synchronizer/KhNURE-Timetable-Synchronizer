package com.khnure.timetable.synchronizer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "google_calendars")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleCalendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "calendar_id")
    private String calendarId;

    @Column(name = "users_id")
    private Long userId;
}
