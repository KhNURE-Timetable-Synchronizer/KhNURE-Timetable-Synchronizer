package com.khnure.timetable.synchronizer.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.khnure.timetable.synchronizer.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.signature-secret}")
    private String signatureSecret;
    private final JWTVerifier jwtVerifier;

    public boolean verify(String jwtToken) {
        try {
            jwtVerifier.verify(jwtToken);
        } catch (JWTVerificationException verificationException) {
            return false;
        }
        return true;
    }

    public String create(User user) {
        return JWT.create()
                .withClaim("id", user.getId())
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getRole().name())
                .withExpiresAt(LocalDateTime.now().atZone(ZoneId.of("Europe/Kiev")).toInstant().plus(1, ChronoUnit.HOURS))
                .sign(Algorithm.HMAC256(signatureSecret));
    }

    public String getEmail(String jwtToken) {
        return jwtVerifier.verify(jwtToken)
                .getClaim("email")
                .asString();
    }
}
