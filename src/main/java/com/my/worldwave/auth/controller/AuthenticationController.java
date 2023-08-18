package com.my.worldwave.auth.controller;

import com.my.worldwave.auth.dto.RefreshTokenRequest;
import com.my.worldwave.auth.dto.RefreshTokenResponse;
import com.my.worldwave.auth.dto.TokenPair;
import com.my.worldwave.auth.service.AuthenticationService;
import com.my.worldwave.member.dto.LoginDto;
import com.my.worldwave.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthenticationService authenticationService;
    private final MemberRepository memberRepository;

    @PostMapping("/login")
    public ResponseEntity<TokenPair> login(@RequestBody LoginDto loginDto) {
        TokenPair tokenPair = authenticationService.login(loginDto);
        return ResponseEntity.ok().body(tokenPair);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenPair tokenPair) {
        authenticationService.logout(tokenPair);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshTokenResponse accessToken = authenticationService.refreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.ok().body(accessToken);
    }

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
