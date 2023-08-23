package com.my.worldwave.auth.service;

import com.my.worldwave.auth.dto.RefreshTokenResponse;
import com.my.worldwave.auth.dto.TokenPair;
import com.my.worldwave.exception.member.AuthenticationFailureException;
import com.my.worldwave.exception.member.RefreshTokenExpiredException;
import com.my.worldwave.member.dto.LoginDto;
import com.my.worldwave.security.JwtTokenService;
import com.my.worldwave.util.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;
    private final RedisService redisService;

    public TokenPair login(LoginDto loginDto) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        try {
            Authentication authenticate = authenticationManager.authenticate(authentication);
            String email = authenticate.getName();
            String accessToken = jwtTokenService.generateAccessToken(email, authenticate.getAuthorities());
            String refreshToken = jwtTokenService.generateRefreshToken(email);
            redisService.saveRefreshToken(refreshToken, email);
            return new TokenPair(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            throw new AuthenticationFailureException();
        }
    }

    public void logout(TokenPair tokenPair, String email) {
        redisService.addToBlacklist(tokenPair.getAccessToken(), email);
        redisService.deleteRefreshToken(tokenPair.getRefreshToken());
    }

    public RefreshTokenResponse refreshToken(String refreshToken) {
        try {
            if (!jwtTokenService.isExpired(refreshToken) && redisService.exists(refreshToken)) {
                String username = jwtTokenService.extractUsernameFromToken(refreshToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                String accessToken = jwtTokenService.generateAccessToken(userDetails.getUsername(), userDetails.getAuthorities());
                return new RefreshTokenResponse(accessToken);
            }
        } catch (Exception e) {
            throw new RefreshTokenExpiredException();
        }
        return null;
    }

}