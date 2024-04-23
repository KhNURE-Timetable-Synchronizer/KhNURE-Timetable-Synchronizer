package com.khnure.timetable.synchronizer.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

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

    public String create(String email) {
        return JWT.create()
                .withClaim("email", email)
                .withExpiresAt(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant())
                .sign(Algorithm.HMAC256(signatureSecret));
    }
}
