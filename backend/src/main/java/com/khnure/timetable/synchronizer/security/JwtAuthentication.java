package com.khnure.timetable.synchronizer.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class JwtAuthentication implements Authentication {
    private Collection<? extends GrantedAuthority> grantedAuthorityList;
    private UserDetails userDetails;
    private String jwtToken;
    private boolean authenticated;

    public JwtAuthentication(Collection<? extends GrantedAuthority> grantedAuthorityList, UserDetails userDetails) {
        this.grantedAuthorityList = grantedAuthorityList;
        this.userDetails = userDetails;
        this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthorityList;
    }

    @Override
    public Object getCredentials() {
        return jwtToken;
    }

    @Override
    public Object getDetails() {
        return this.userDetails;
    }

    @Override
    public Object getPrincipal() {
        return userDetails.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return userDetails.getUsername();
    }

    public void setDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public void setAuthorities(List<GrantedAuthority> grantedAuthorityList) {
        this.grantedAuthorityList = grantedAuthorityList;
    }
    public void setJwtToken(String jwtToken){
        this.jwtToken = jwtToken;
    }
}
