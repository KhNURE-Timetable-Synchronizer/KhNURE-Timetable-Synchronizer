package com.khnure.timetable.synchronizer.repository;

import com.khnure.timetable.synchronizer.model.RequestLinksCoordinatorTimetable;
import com.khnure.timetable.synchronizer.model.StatusRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface RequestLinksCoordinatorTimetableRepository  extends JpaRepository<RequestLinksCoordinatorTimetable,Long> {
    @Query("SELECT r  FROM RequestLinksCoordinatorTimetable r WHERE r.user.id = :userId AND r.khnureTimetables.id = :khnureTimetablesId AND r.statusRequest NOT IN :permissibleStatus")
    Optional<RequestLinksCoordinatorTimetable> findByUserIdAndKhnureTimetablesIdAndStatusRequestNotIn(Long userId, Long khnureTimetablesId, List<StatusRequest> permissibleStatus);

    List<RequestLinksCoordinatorTimetable> findByUserId(Long userId);

    @Override
    Page<RequestLinksCoordinatorTimetable> findAll(Pageable pageable);
}
