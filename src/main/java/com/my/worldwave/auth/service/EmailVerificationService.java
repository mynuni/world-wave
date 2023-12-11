package com.my.worldwave.auth.service;

import com.my.worldwave.auth.util.EmailRedisUtil;
import com.my.worldwave.exception.auth.EmailVerificationException;
import com.my.worldwave.util.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailVerificationService {

    private final EmailSender emailSender;
    private final EmailRedisUtil emailRedisUtil;

    private static final int MIN_RANDOM_VALUE = 1_000;
    private static final int MAX_RANDOM_VALUE = 10_000; // 4자리(1_000 ~ 9_999)
    private static final String TITLE = "월드 웨이브 인증 메일";

    public void sendVerificationCode(String receiver) {
        String verificationCode = generateVerificationCode(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
        log.info("TO:{}, CODE:{}", receiver, verificationCode);
        emailSender.sendEmail(receiver, TITLE, verificationCode);
        emailRedisUtil.saveVerificationCode(receiver, verificationCode);
    }

    public void verifyCode(String email, String code) {
        boolean isMatch = emailRedisUtil.verifyVerificationCode(email, code);
        if (!isMatch) {
            throw new EmailVerificationException();
        }
    }

    private String generateVerificationCode(int origin, int bound) {
        return Integer.toString(ThreadLocalRandom.current().nextInt(origin, bound));
    }

}
