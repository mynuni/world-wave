package com.my.worldwave.member.controller;

import com.my.worldwave.auth.dto.response.AccessTokenResponse;
import com.my.worldwave.auth.util.AuthCookieUtil;
import com.my.worldwave.member.dto.request.*;
import com.my.worldwave.member.dto.response.MemberSearchResponse;
import com.my.worldwave.member.dto.response.MyInfoSummaryResponse;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.member.service.MemberService;
import com.my.worldwave.security.CustomUserDetails;
import com.my.worldwave.security.JwtTokenService;
import com.my.worldwave.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.my.worldwave.auth.util.AuthenticationConstants.REFRESH_TOKEN_COOKIE_NAME;

@Slf4j
@RequestMapping("/api/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;
    private final CookieUtil cookieUtil;
    private final AuthCookieUtil authCookieUtil;

    @GetMapping("/search")
    public ResponseEntity<Page<MemberSearchResponse>> searchMembers(@AuthenticationPrincipal CustomUserDetails userDetails, MemberSearchParam searchParam) {
        Page<MemberSearchResponse> memberSearchResponses = memberService.searchMembers(userDetails.getMemberId(), searchParam);
        return ResponseEntity.ok(memberSearchResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberSearchResponse> getMemberInfo(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberSearchResponse memberInfo = memberService.getMemberInfo(id, userDetails.getMember().getId());
        return ResponseEntity.ok(memberInfo);
    }

    // 현재 회원의 Public한 정보 반환
    @GetMapping("/my-info")
    public ResponseEntity<MyInfoSummaryResponse> getMyInfoSummary(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MyInfoSummaryResponse myInfoSummary = memberService.getMyInfoSummary(userDetails.getMember().getId());
        return ResponseEntity.ok(myInfoSummary);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUpWithForm(@Valid @RequestBody SignUpRequest signUpRequest) {
        memberService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sign-up/oauth2")
    public ResponseEntity<AccessTokenResponse> signUpWithOAuth2(@Valid @RequestBody OAuth2SignUpRequest signUpRequest) {
        Member member = memberService.signUpWithOAuth2(signUpRequest);
        CustomUserDetails userDetails = new CustomUserDetails(member);
        String accessToken = jwtTokenService.generateAccessToken(userDetails.getUsername(), userDetails.getAuthorities());
        String refreshToken = jwtTokenService.generateRefreshToken(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Set-Cookie", authCookieUtil.createRefreshTokenCookie(refreshToken))
                .body(new AccessTokenResponse(accessToken));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("#id eq (authentication.principal.member.id)")
    public ResponseEntity<Void> updateMemberInfo(@PathVariable Long id,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @RequestBody MemberUpdateRequest memberUpdateRequest) {
        memberService.updateMemberInfo(userDetails.getMemberId(), memberUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/profile-img")
    @PreAuthorize("#id eq (authentication.principal.member.id)")
    public ResponseEntity<String> uploadProfileImg(@PathVariable Long id,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @RequestPart("profileImg") MultipartFile file) {
        String profileImgPath = memberService.uploadProfileImg(userDetails.getMemberId(), file);
        return ResponseEntity.ok(profileImgPath);
    }

    @PatchMapping("/me/modify/password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody PasswordChangeRequest passwordChangeRequest) {
        memberService.changePassword(userDetails.getMember(), passwordChangeRequest.getCurrentPw(), passwordChangeRequest.getNewPw(), passwordChangeRequest.getNewPwConfirm());
        return ResponseEntity.ok().build();
    }

    // OAuth2 가입자 탈퇴
    @DeleteMapping("/withdraw/oauth2")
    public ResponseEntity<Void> oAuth2UserWithdraw(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @RequestHeader("Authorization") String accessToken,
                                                   HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshTokenCookie = cookieUtil.getCookie(request, REFRESH_TOKEN_COOKIE_NAME);
        memberService.oAuth2UserWithdraw(userDetails.getMemberId(), accessToken, refreshTokenCookie.getValue());
        cookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        return ResponseEntity.noContent().build();
    }

    // 이메일 가입자 탈퇴
    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> formUserWithdraw(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @RequestHeader("Authorization") String accessToken,
                                                 @RequestBody WithdrawalRequest withdrawalRequest,
                                                 HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshTokenCookie = cookieUtil.getCookie(request, REFRESH_TOKEN_COOKIE_NAME);
        memberService.formUserWithdraw(userDetails.getMemberId(), withdrawalRequest.getPassword(), accessToken, refreshTokenCookie.getValue());
        cookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        return ResponseEntity.noContent().build();
    }

}
