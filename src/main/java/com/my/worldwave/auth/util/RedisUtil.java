package com.my.worldwave.auth.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisUtil {

    // Redis 이메일 인증 유틸 클래스 토큰 Redis와 통합할 것
    private final StringRedisTemplate stringRedisTemplate;

    public String getValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void setData(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void setData(String key, String value, long duration) {
        stringRedisTemplate.opsForValue().set(key, value, duration, TimeUnit.SECONDS);
    }

    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }

    public boolean exists(String key) {
        return stringRedisTemplate.hasKey(key);
    }

}
