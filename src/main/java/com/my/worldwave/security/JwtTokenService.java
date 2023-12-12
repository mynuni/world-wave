package com.my.worldwave.security;

import com.my.worldwave.exception.auth.AccessTokenExpiredException;
import com.my.worldwave.exception.auth.AuthenticationFailureException;
import com.my.worldwave.util.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import static com.my.worldwave.auth.util.AuthenticationConstants.SIGN_UP_PENDING_EMAIL_EXP;

@Slf4j
@Component
public class JwtTokenService {

    private final RedisService redisService;
    private final Key SIGNING_KEY;
    private final long ACCESS_TOKEN_EXPIRATION;
    private final long REFRESH_TOKEN_EXPIRATION;
    private static final String AUTHORITY_CLAIM_KEY = "authority";

    public JwtTokenService(RedisService redisService,
                           @Value("${jwt.secret}") String secretKey,
                           @Value("${jwt.access-token-exp}") long accessTokenExpiration,
                           @Value("${jwt.refresh-token-exp}") long refreshTokenExpiration) {
        this.redisService = redisService;
        this.ACCESS_TOKEN_EXPIRATION = accessTokenExpiration;
        this.REFRESH_TOKEN_EXPIRATION = refreshTokenExpiration;
        this.SIGNING_KEY = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateAccessToken(String email, Collection<? extends GrantedAuthority> authorities) {
        Date now = new Date();
        List<String> authorityList = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORITY_CLAIM_KEY, authorityList)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateEmailToken(String email) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + SIGN_UP_PENDING_EMAIL_EXP))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }

    public boolean isValidToken(String token) {
        return (!isExpired(token) && !redisService.isBlacklisted(token));
    }

    public boolean isExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    private Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException();
        } catch (Exception e) {
            throw new AuthenticationFailureException();
        }
    }

}
