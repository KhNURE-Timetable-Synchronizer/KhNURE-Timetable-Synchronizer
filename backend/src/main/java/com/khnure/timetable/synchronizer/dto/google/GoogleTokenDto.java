package com.khnure.timetable.synchronizer.dto.google;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleTokenDto {
    @JsonAlias("access_token")
    private String accessToken;
    @JsonAlias("expired_in")
    private Long expiresIn;
    @JsonAlias("refresh_token")
    private String refreshToken;
    private String scope;
    @JsonAlias("token_type")
    private String tokenType;
    @JsonAlias("id_token")
    private String idToken;

}
