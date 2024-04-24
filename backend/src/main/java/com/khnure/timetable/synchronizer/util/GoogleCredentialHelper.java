package com.khnure.timetable.synchronizer.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class GoogleCredentialHelper {
    @Value("${google.clientId}")
    private String clientId;
    @Value("${google.clientSecret}")
    private String clientSecret;

    private final ConcurrentHashMap<Long, GoogleCredential> credentialMap;


    public GoogleCredentialHelper() {
        credentialMap = new ConcurrentHashMap<>();
    }

    public GoogleCredential putCredentials(Long userId, String accessToken) {
        GoogleCredential googleCredential = new GoogleCredential.Builder()
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setAccessToken(accessToken);

        credentialMap.put(userId, googleCredential);
        return googleCredential;
    }

    public boolean containsCredentials(Long userId) {
        return credentialMap.containsKey(userId);
    }

    public boolean credentialsExpired(Long userId) {
        Long expiresInSeconds = credentialMap.get(userId).getExpiresInSeconds();
        if (expiresInSeconds == null) {
            return false; //todo stub returning
        }
        return expiresInSeconds <= 0;
    }

    public GoogleCredential getCredentials(Long userId) {
        return credentialMap.get(userId);
    }
}
