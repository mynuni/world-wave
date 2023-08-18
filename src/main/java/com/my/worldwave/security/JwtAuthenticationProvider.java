package com.my.worldwave.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider {

    private final JwtTokenService jwtTokenService;
    private final CustomUserDetailsService customUserDetailsService;

    public Authentication getAuthentication(String token) {
        String username = jwtTokenService.extractUsernameFromToken(token);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
