package com.khnure.timetable.synchronizer.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.khnure.timetable.synchronizer.dto.google.GoogleTokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class GoogleCredentialHelper {
    @Value("${google.clientId}")
    private String clientId;
    @Value("${google.clientSecret}")
    private String clientSecret;
    private final HttpTransport httpTransport;
    private final JsonFactory jsonFactory;

    private final ConcurrentHashMap<Long, GoogleCredential> credentialMap;


    public GoogleCredentialHelper(HttpTransport httpTransport, JsonFactory jsonFactory) {
        this.httpTransport = httpTransport;
        this.jsonFactory = jsonFactory;
        credentialMap = new ConcurrentHashMap<>();
    }

    public GoogleCredential putCredentials(Long userId, GoogleTokenDto googleTokenDto) {
        GoogleCredential googleCredential = new GoogleCredential.Builder()
                .setClientSecrets(clientId, clientSecret)
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .build()
                .setAccessToken(googleTokenDto.getAccessToken())
                .setExpiresInSeconds(googleTokenDto.getExpiresIn())
                .setRefreshToken(googleTokenDto.getRefreshToken());

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
