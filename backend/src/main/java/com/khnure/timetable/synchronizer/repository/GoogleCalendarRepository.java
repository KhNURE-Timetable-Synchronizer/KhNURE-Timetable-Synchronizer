package com.khnure.timetable.synchronizer.repository;

import com.khnure.timetable.synchronizer.model.GoogleCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoogleCalendarRepository extends JpaRepository<GoogleCalendar,Long> {

    List<GoogleCalendar> findByUserId(Long userId);
}
