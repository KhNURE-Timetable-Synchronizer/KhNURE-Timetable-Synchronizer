package com.khnure.timetable.synchronizer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "request_links_coordinator_timetable")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestLinksCoordinatorTimetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "khnure_timetables_id")
    private KhnureTimetables khnureTimetables;

    @Column(name = "status_request")
    @Enumerated(EnumType.STRING)
    private StatusRequest statusRequest;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @Column(name = "contact_account")
    private String contactAccount;
}
