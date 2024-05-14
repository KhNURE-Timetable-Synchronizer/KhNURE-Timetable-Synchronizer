package com.khnure.timetable.synchronizer.util;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public Cookie createJwtCookie(String jwt){
        Cookie cookie = new Cookie("JWT", jwt);
        cookie.setMaxAge(7 * 24 * 3600);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setDomain(null);
        return cookie;
    }
}
