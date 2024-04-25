package com.khnure.timetable.synchronizer.filter;

import com.khnure.timetable.synchronizer.service.JwtService;
import com.khnure.timetable.synchronizer.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;


@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/v1/jwt/create")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getCookies() == null){
            writeUnauthorizedResponse(response);
            return;
        }

        Optional<Cookie> jwtTokenCookie = Arrays.stream(request.getCookies()).filter(cookie -> "JWT".equals(cookie.getName())).findFirst();
        if (jwtTokenCookie.isEmpty() || !jwtService.verify(jwtTokenCookie.get().getValue())) {

            writeUnauthorizedResponse(response);
            return;
        }

        String email = jwtService.getEmail(jwtTokenCookie.get().getValue());
        UserDetails userDetails = userService.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email,
                        userDetails.getPassword(), userDetails.getAuthorities());
        authenticationToken.setDetails(userDetails);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    private static void writeUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.SC_UNAUTHORIZED);
        response.getWriter().print("JWT was expired");
        response.getWriter().close();
    }
}
