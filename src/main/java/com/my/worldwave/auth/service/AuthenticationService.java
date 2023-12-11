package com.my.worldwave.auth.service;

import com.my.worldwave.auth.dto.TokenDto;
import com.my.worldwave.auth.dto.response.AccessTokenResponse;
import com.my.worldwave.exception.auth.AuthenticationFailureException;
import com.my.worldwave.member.entity.*;
import com.my.worldwave.member.repository.MemberRepository;
import com.my.worldwave.security.JwtTokenService;
import com.my.worldwave.util.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;
    private final RedisService redisService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${guest.email}")
    private String guestEmail;

    @Value("${guest.password}")
    private String guestPassword;

    public TokenDto login(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            String accessToken = jwtTokenService.generateAccessToken(email, authentication.getAuthorities());
            String refreshToken = jwtTokenService.generateRefreshToken(email);
            redisService.saveRefreshToken(refreshToken, email);
            return new TokenDto(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            throw new AuthenticationFailureException();
        }
    }

    /**
     * 유효 기간이 남은 Access Token은 만료 기간까지 접속 차단 리스트에 저장한다.
     * Refresh Token은 삭제 후 Controller에서 쿠키를 만료시킨다.
     */
    public void logout(String accessToken, String refreshToken, String email) {
        redisService.addToBlacklist(accessToken, email);
        redisService.deleteRefreshToken(refreshToken);
    }

    public AccessTokenResponse refreshToken(String refreshToken) {
        try {
            if (jwtTokenService.isValidToken(refreshToken) && redisService.exists(refreshToken)) {
                String username = jwtTokenService.extractUsernameFromToken(refreshToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                String accessToken = jwtTokenService.generateAccessToken(userDetails.getUsername(), userDetails.getAuthorities());
                return new AccessTokenResponse(accessToken);
            }
        } catch (Exception e) {
            throw new AuthenticationFailureException();
        }
        return null;
    }

    @Transactional
    public TokenDto loginGuest() {
        Optional<Member> memberOptional = memberRepository.findByEmail(guestEmail);
        if (!memberOptional.isPresent()) {
            // 게스트 계정이 없으면 생성
            createGuestMember();
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(guestEmail);
        String accessToken = jwtTokenService.generateAccessToken(guestEmail, userDetails.getAuthorities());
        String refreshToken = jwtTokenService.generateRefreshToken(guestEmail);
        redisService.saveRefreshToken(refreshToken, guestEmail);
        return new TokenDto(accessToken, refreshToken);
    }

    private void createGuestMember() {
        Member guestMember = Member.builder()
                .email(guestEmail)
                .password(passwordEncoder.encode(guestPassword))
                .gender(Gender.M)
                .ageRange(20)
                .role(Role.USER)
                .registerType(RegisterType.FORM)
                .profileImage(new ProfileImage("default-profile-image.png"))
                .nickname("GUEST")
                .country("KR")
                .build();

        memberRepository.save(guestMember);
    }

}
