package com.khnure.timetable.synchronizer.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.khnure.timetable.synchronizer.model.RefreshToken;
import com.khnure.timetable.synchronizer.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.signature-secret}")
    private String signatureSecret;
    @Value("${jwt.token.expires-seconds}")
    private Long jwtExpiresSeconds;
    @Value("${jwt.refresh-token.expires-minutes}")
    private Long jwtRefreshTokenExpiresMinutes;
    private final JWTVerifier jwtVerifier;
    private final UserService userService;

    public Optional<String> verifyOrRefresh(String jwtToken) {
        try {
            jwtVerifier.verify(jwtToken);
        } catch (TokenExpiredException expiredException) {
            User user = userService.findByEmail(getEmail(jwtToken)).get();
            RefreshToken refreshToken = user.getRefreshToken();
            if (refreshToken.getToken().equals(extractJwtRefreshToken(jwtToken))
                    && refreshToken.getExpiredAt().isAfter(LocalDateTime.now())) {
                return Optional.of(createJwtToken(user, refreshToken));
            }
            return Optional.empty();
        } catch (JWTVerificationException verificationException) {
            return Optional.empty();
        }
        return Optional.of(jwtToken);
    }


    public String create(User user) {
        RefreshToken refreshToken = createRefreshToken();
        userService.updateRefreshToken(user.getId(), refreshToken);

        return createJwtToken(user, refreshToken);
    }

    public RefreshToken createRefreshToken() {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(jwtRefreshTokenExpiresMinutes);
        return RefreshToken.builder()
                .token(token)
                .expiredAt(expiredAt)
                .build();
    }

    private String createJwtToken(User user, RefreshToken refreshToken) {
        return JWT.create()
                .withClaim("id", user.getId())
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getRole().name())
                .withClaim("refreshToken", refreshToken.getToken())
                .withExpiresAt(LocalDateTime.now().atZone(ZoneId.of("Europe/Kiev")).toInstant().plusSeconds(jwtExpiresSeconds))
                .sign(Algorithm.HMAC256(signatureSecret));
    }

    public String getEmail(String jwtToken) {
        return JWT.decode(jwtToken)
                .getClaim("email")
                .asString();
    }

    private String extractJwtRefreshToken(String jwtToken) {
        return JWT.decode(jwtToken)
                .getClaim("refreshToken")
                .asString();

    }
}
