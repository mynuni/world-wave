package com.my.worldwave.auth.controller;

import com.my.worldwave.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final MemberRepository memberRepository;

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailExists(String email) {
        log.info("EMAIL FROM PARAM = {}", email);
        boolean exists = memberRepository.existsByEmail(email);
        log.info("EMAIL EXISTS = {}", exists);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNicknameExists(String nickname) {
        boolean exists = memberRepository.existsByNickname(nickname);
        return ResponseEntity.ok(exists);
    }

}
