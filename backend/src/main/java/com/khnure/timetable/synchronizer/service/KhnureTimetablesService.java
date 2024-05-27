package com.khnure.timetable.synchronizer.service;

import com.khnure.timetable.synchronizer.model.KhnureTimetables;
import com.khnure.timetable.synchronizer.repository.KhnureTimetablesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KhnureTimetablesService {
    private final KhnureTimetablesRepository khnureTimetablesRepository;

    public Optional<KhnureTimetables> getCalendarByNameAndCalendarId(String name, Long khnureTimetableId) {
        return khnureTimetablesRepository.findByNameAndKhnureTimetableId(name, khnureTimetableId);
    }

    public Optional<KhnureTimetables> getCalendarByCalendarId(Long khnureTimetableId) {
        return khnureTimetablesRepository.findByKhnureTimetableId(khnureTimetableId);
    }

    public Optional<KhnureTimetables> addKhnureTimetable(String name, Long khnureTimetableId) {
        Optional<KhnureTimetables> timetableOptional =getCalendarByNameAndCalendarId(name, khnureTimetableId);
        if (timetableOptional.isPresent()) {
            return timetableOptional;
        } else {
            KhnureTimetables newKhnureTimetable = new KhnureTimetables();
            newKhnureTimetable.setName(name);
            newKhnureTimetable.setKhnureTimetableId(khnureTimetableId);
            KhnureTimetables savedKhnureTimetable = khnureTimetablesRepository.save(newKhnureTimetable);
            return  Optional.of(savedKhnureTimetable);
        }
    }
}
