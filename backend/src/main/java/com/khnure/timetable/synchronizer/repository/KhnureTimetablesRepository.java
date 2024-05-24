package com.khnure.timetable.synchronizer.repository;

import com.khnure.timetable.synchronizer.model.KhnureTimetables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
@Repository
public interface KhnureTimetablesRepository  extends JpaRepository<KhnureTimetables,Long> {
    Optional<KhnureTimetables> findByNameAndKhnureTimetableId(String name, Long khnureTimetableId);

}
