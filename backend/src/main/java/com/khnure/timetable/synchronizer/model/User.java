package com.khnure.timetable.synchronizer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "google_refresh_token")
    private String googleRefreshToken;

    @Column(name = "jwt_refresh_token")
    private String jwtRefreshToken;

    @Column(name = "jwt_refresh_token_expired_at")
    private LocalDateTime jwtRefreshTokenExpiredAt;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<LinksCoordinatorTimetablePermission> permissionList;
    public RefreshToken getRefreshToken(){
        return RefreshToken.builder()
                .token(jwtRefreshToken)
                .expiredAt(jwtRefreshTokenExpiredAt)
                .build();
    }
    public void addTimetablePermission(LinksCoordinatorTimetablePermission permission){
        this.permissionList.add(permission);
    }}
