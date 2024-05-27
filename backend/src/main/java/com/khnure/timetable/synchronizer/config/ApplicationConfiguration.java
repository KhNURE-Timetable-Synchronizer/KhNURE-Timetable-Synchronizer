package com.khnure.timetable.synchronizer.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.SneakyThrows;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ApplicationConfiguration {
    @Value("${jwt.signature-secret}")
    private String signatureSecret;
    @Value("${google.clientId}")
    private String clientId;


    @Bean
    public HttpClient httpClient() {
        return HttpClientBuilder.create().build();
    }

    @Bean
    public JWTVerifier jwtVerifier() {
        JWTVerifier.BaseVerification verification = (JWTVerifier.BaseVerification) JWT.require(Algorithm.HMAC256(signatureSecret));
        Clock clock = Clock.systemUTC();
        return verification.build(clock);
    }

    @Bean
    public JsonFactory jsonFactory() {
        return new GsonFactory();
    }

    @SneakyThrows
    @Bean
    public HttpTransport httpTransport() {
        return GoogleNetHttpTransport.newTrustedTransport();
    }
}
