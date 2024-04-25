package com.khnure.timetable.synchronizer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khnure.timetable.synchronizer.dto.google.GoogleTokenDto;
import com.khnure.timetable.synchronizer.exception.GoogleAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleService {
    @Value("${google.redirect-uri}")
    private String redirectUri;
    @Value("${google.clientId}")
    private String clientId;
    @Value("${google.clientSecret}")
    private String clientSecret;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";

    public GoogleTokenDto getByCodeToken(String codeToken) {

        HttpPost httpPost = new HttpPost(TOKEN_URL);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("client_id", clientId));
        params.add(new BasicNameValuePair("client_secret", clientSecret));
        params.add(new BasicNameValuePair("code", codeToken));
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("redirect_uri", redirectUri));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
                String responseBody = EntityUtils.toString(response.getEntity());
                throw new GoogleAuthenticationException("Google return 400 status with:" + responseBody, HttpStatus.SC_BAD_REQUEST);
            }

            String responseBody = EntityUtils.toString(response.getEntity());
            return objectMapper.readValue(responseBody, GoogleTokenDto.class);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public String getUserEmail(GoogleTokenDto googleTokenDto) {
        HttpGet httpGet = new HttpGet(USER_INFO_URL);
        httpGet.addHeader(new BasicHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", googleTokenDto.getAccessToken())));

        try {
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
                String responseBody = EntityUtils.toString(response.getEntity());
                throw new RuntimeException("Google return 400 status with:" + responseBody);
            }

            String responseBody = EntityUtils.toString(response.getEntity());
            Map<String, Object> map = objectMapper.readValue(responseBody, Map.class);
            String email = (String) map.get("email");
            if (email == null) {
                throw new RuntimeException("Can't find email from google response. Maybe scope \'email\' wasn't be set");
            }
            return email;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
