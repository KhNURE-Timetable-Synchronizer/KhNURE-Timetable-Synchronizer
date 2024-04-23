package com.khnure.timetable.synchronizer.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Value("${jwt.signature-secret}")
    private String signatureSecret;

    @Bean
    public HttpClient httpClient() {
        return HttpClientBuilder.create().build();
    }

    @Bean
    public JWTVerifier jwtVerifier(){
        JWTVerifier.BaseVerification verification = (JWTVerifier.BaseVerification) JWT.require(Algorithm.HMAC256(signatureSecret));
        Clock clock = Clock.systemUTC();
       return verification.build(clock);
    }

}
