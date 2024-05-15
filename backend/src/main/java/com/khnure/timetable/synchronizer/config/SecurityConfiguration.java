package com.khnure.timetable.synchronizer.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.khnure.timetable.synchronizer.filter.JwtFilter;
import com.khnure.timetable.synchronizer.service.JwtService;
import com.khnure.timetable.synchronizer.service.UserService;
import com.khnure.timetable.synchronizer.util.CalendarHelper;
import com.khnure.timetable.synchronizer.util.CookieUtil;
import com.khnure.timetable.synchronizer.util.GoogleCredentialHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private static final String[] WHITE_LIST_URL = { "/auth/**", "/error", "/account"};
    @Value("${api.base-url}")
    private String apiVersionPath;

    private final JwtService jwtService;
    private final UserService userService;
    private final CalendarHelper calendarHelper;
    private final GoogleCredentialHelper googleCredentialHelper;
    private final ObjectMapper objectMapper;
    private final CookieUtil cookieUtil;

    @Bean
    @Profile("default")
    public SecurityFilterChain withSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .cors().and()
                .authorizeHttpRequests(urlConfig -> {
                    urlConfig.requestMatchers(apiVersionPath + "/jwt/create").permitAll()
                            .requestMatchers(WHITE_LIST_URL).permitAll()
                            .anyRequest().authenticated();
                })
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(config-> config.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);


        return httpSecurity.build();
    }

    private JwtFilter jwtFilter() {
        return new JwtFilter(jwtService, userService, calendarHelper, googleCredentialHelper, objectMapper, cookieUtil);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "OPTIONS", "DELETE", "PATCH", "PUT"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Set-Cookie"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Profile("test")
    public SecurityFilterChain noSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .cors().disable()
                .build();
    }
}
