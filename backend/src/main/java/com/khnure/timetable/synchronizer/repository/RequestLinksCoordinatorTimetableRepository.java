package com.khnure.timetable.synchronizer.repository;

import com.khnure.timetable.synchronizer.model.RequestLinksCoordinatorTimetable;
import com.khnure.timetable.synchronizer.model.StatusRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RequestLinksCoordinatorTimetableRepository  extends JpaRepository<RequestLinksCoordinatorTimetable,Long> {
    @Query("SELECT COUNT(*) > 0  FROM RequestLinksCoordinatorTimetable r WHERE r.userId = :userId AND r.khnureTimetables.id = :khnureTimetablesId AND r.statusRequest != :status")
    boolean existsByUserIdAndKhnureTimetablesIdAndStatusNot(@Param("userId") Long userId, @Param("khnureTimetablesId") Long khnureTimetablesId, @Param("status") StatusRequest status);
}
