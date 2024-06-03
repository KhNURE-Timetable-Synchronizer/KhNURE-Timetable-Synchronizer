package com.khnure.timetable.synchronizer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "links_coordinator_has_permission_to_khnure_timetable")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinksCoordinatorTimetablePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "khnure_timetable_id")
    private Long khnureTimetableIid;

    @Column(name = "user_id")
    private Long userId;

}
