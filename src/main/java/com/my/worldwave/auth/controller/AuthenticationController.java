package com.my.worldwave.auth.controller;

import com.my.worldwave.auth.dto.TokenDto;
import com.my.worldwave.auth.dto.request.EmailVerificationRequest;
import com.my.worldwave.auth.dto.request.LoginRequest;
import com.my.worldwave.auth.dto.response.AccessTokenResponse;
import com.my.worldwave.auth.dto.response.LoginResponse;
import com.my.worldwave.auth.service.AuthenticationService;
import com.my.worldwave.auth.service.EmailVerificationService;
import com.my.worldwave.auth.util.AuthCookieUtil;
import com.my.worldwave.exception.auth.AuthenticationFailureException;
import com.my.worldwave.member.dto.request.PasswordResetRequest;
import com.my.worldwave.member.service.MemberService;
import com.my.worldwave.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final MemberService memberService;
    private final EmailVerificationService emailVerificationService;
    private final AuthCookieUtil authCookieUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        TokenDto tokenDto = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
        String refreshTokenCookie = authCookieUtil.createRefreshTokenCookie(tokenDto.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie)
                .body(new LoginResponse(tokenDto.getAccessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody TokenDto tokenDto,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        String refreshToken = authCookieUtil.getRefreshTokenFromCookie(request);
        authenticationService.logout(tokenDto.getAccessToken(), refreshToken, userDetails.getUsername());
        authCookieUtil.deleteRefreshTokenCookie(request, response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = authCookieUtil.getRefreshTokenFromCookie(request);
        if (refreshToken == null) {
            throw new AuthenticationFailureException();
        }
        AccessTokenResponse accessToken = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(accessToken);
    }

    @PostMapping("/mail/send")
    public ResponseEntity<Void> sendVerificationCode(@Valid @RequestBody EmailVerificationRequest request) {
        emailVerificationService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mail/verify/code")
    public ResponseEntity<String> verifyCode(@Valid @RequestBody EmailVerificationRequest request) {
        emailVerificationService.verifyCode(request.getEmail(), request.getVerificationCode());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mail/verify/duplication")
    public ResponseEntity<Boolean> checkEmailExists(@Valid @RequestBody EmailVerificationRequest request) {
        String email = request.getEmail();
        return ResponseEntity.ok().body(memberService.isEmailExists(email));
    }

    @PostMapping("/help/reset-password")
    public ResponseEntity<Void> sendPasswordResetToken(@Valid @RequestBody PasswordResetRequest request) {
        emailVerificationService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/help/reset-password/confirm")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        emailVerificationService.verifyCode(request.getEmail(), request.getPasswordChangeToken());
        memberService.changePassword(request.getEmail(), request.getPassword(), request.getPasswordConfirm());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login/guest")
    public ResponseEntity<LoginResponse> loginGuest() {
        TokenDto tokenDto = authenticationService.loginGuest();
        String refreshTokenCookie = authCookieUtil.createRefreshTokenCookie(tokenDto.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie)
                .body(new LoginResponse(tokenDto.getAccessToken()));
    }

}
