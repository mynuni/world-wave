package com.my.worldwave.security;

import com.my.worldwave.util.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenService {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORITY = "authority";

    private final RedisService redisService;
    private final String SECRET_KEY;
    private final long ACCESS_TOKEN_EXPIRATION;
    private final long REFRESH_TOKEN_EXPIRATION;
    private final Key SIGNING_KEY;

    public JwtTokenService(RedisService redisService,
                           @Value("${jwt.secret-key}") String secretKey,
                           @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
                           @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.redisService = redisService;
        this.SECRET_KEY = secretKey;
        this.ACCESS_TOKEN_EXPIRATION = accessTokenExpiration;
        this.REFRESH_TOKEN_EXPIRATION = refreshTokenExpiration;
        this.SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateAccessToken(String email, Collection<? extends GrantedAuthority> authorities) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION);

        List<String> authorityList = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORITY, authorityList)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
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
        } catch (Exception e) {
            // 예외 처리 마저 할 것
            throw new RuntimeException(e);
        }
    }

}
