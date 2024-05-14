package com.khnure.timetable.synchronizer.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.khnure.timetable.synchronizer.exception.response.CustomErrorResponse;
import com.khnure.timetable.synchronizer.model.CustomUserDetails;
import com.khnure.timetable.synchronizer.model.User;
import com.khnure.timetable.synchronizer.service.JwtService;
import com.khnure.timetable.synchronizer.service.UserService;
import com.khnure.timetable.synchronizer.util.CalendarHelper;
import com.khnure.timetable.synchronizer.util.CookieUtil;
import com.khnure.timetable.synchronizer.util.GoogleCredentialHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;


@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final CalendarHelper calendarHelper;
    private final GoogleCredentialHelper googleCredentialHelper;
    private final ObjectMapper objectMapper;
    private final CookieUtil cookieUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/v1/jwt/create")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getCookies() == null) {
            writeUnauthorizedResponse(response);
            return;
        }

        Optional<Cookie> jwtTokenCookie = Arrays.stream(request.getCookies()).filter(cookie -> "JWT".equals(cookie.getName())).findFirst();
        if (jwtTokenCookie.isEmpty()) {
            writeUnauthorizedResponse(response);
            return;
        }
        Optional<String> jwtToken = jwtService.verifyOrRefresh(jwtTokenCookie.get().getValue());
        if (jwtToken.isEmpty()) {
            writeUnauthorizedResponse(response);
            return;
        }
        addJwtCookie(response,jwtToken.get());

        String email = jwtService.getEmail(jwtTokenCookie.get().getValue());
        CustomUserDetails userDetails = (CustomUserDetails) userService.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());
        authenticationToken.setDetails(userDetails);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        User user = userDetails.getUser();
        if (!calendarHelper.userHasCalendar(user.getId())) {
            String googleRefreshToken = user.getGoogleRefreshToken();
            if (googleRefreshToken == null || googleRefreshToken.isBlank()) {
                writeReauthorizeResponse(response);
                return;
            }
            GoogleCredential googleCredential = googleCredentialHelper.putCredentials(user.getId(), googleRefreshToken);
            calendarHelper.createCalendarForUser(user.getId(), googleCredential);
        }

        filterChain.doFilter(request, response);
    }

    private void addJwtCookie(HttpServletResponse response, String jwtToken) {
        response.addCookie(cookieUtil.createJwtCookie(jwtToken));
    }

    private void writeReauthorizeResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.SC_CONFLICT);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .error(org.springframework.http.HttpStatus.CONFLICT.getReasonPhrase())
                .status(org.springframework.http.HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .message("Server couldn't find your google refresh token. Please, reauthorize using /jwt/create endpoint.")
                .build();
        response.getWriter().print(objectMapper.writeValueAsString(errorResponse));
        response.getWriter().close();
    }

    private void writeUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.SC_UNAUTHORIZED);
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .error(org.springframework.http.HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .status(org.springframework.http.HttpStatus.UNAUTHORIZED.value())
                .timestamp(LocalDateTime.now())
                .message("JWT was expired.")
                .build();
        response.getWriter().print(objectMapper.writeValueAsString(errorResponse));
        response.getWriter().close();
    }
}
