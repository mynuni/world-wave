package com.my.worldwave.auth.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailRedisUtil {

    private final StringRedisTemplate stringRedisTemplate;
    private static final String EMAIL_PREFIX = "email:verification:";
    private static final int EMAIL_VERIFICATION_EXP_SECONDS = 3 * 60; // 3분

    public void saveVerificationCode(String email, String code) {
        stringRedisTemplate.opsForValue().set(EMAIL_PREFIX + email, code, EMAIL_VERIFICATION_EXP_SECONDS, TimeUnit.SECONDS);
    }

    public boolean verifyVerificationCode(String email, String code) {
//      POP 동작은 7버전 이상에서 사용
//      stringRedisTemplate.opsForValue().getAndDelete(EMAIL_PREFIX + email);
        String storedCode = stringRedisTemplate.opsForValue().get(EMAIL_PREFIX + email);
        if (storedCode != null && storedCode.equals(code)) {
            stringRedisTemplate.delete(EMAIL_PREFIX + email);
            return true;
        }
        return false;
    }

}
