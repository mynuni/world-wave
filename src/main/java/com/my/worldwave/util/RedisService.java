package com.my.worldwave.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisService {

    // 토큰용 Redis 유틸 이메일 Redis와 통합할 것
    private final StringRedisTemplate stringRedisTemplate;
    private static final String REFRESH_PREFIX = "refresh:";
    private static final String BLACKLIST_PREFIX = "blacklist:";

    public void saveRefreshToken(String refreshToken, String email) {
        stringRedisTemplate.opsForValue().set(REFRESH_PREFIX + refreshToken, email);
    }

    public void addToBlacklist(String accessToken, String email) {
        stringRedisTemplate.opsForValue().set(BLACKLIST_PREFIX + accessToken, email);
    }

    public void deleteRefreshToken(String refreshToken) {
        stringRedisTemplate.delete(REFRESH_PREFIX + refreshToken);
    }

    public boolean isBlacklisted(String accessToken) {
        return stringRedisTemplate.hasKey(BLACKLIST_PREFIX + accessToken);
    }

    public boolean exists(String refreshToken) {
        return stringRedisTemplate.hasKey(REFRESH_PREFIX + refreshToken);
    }

}