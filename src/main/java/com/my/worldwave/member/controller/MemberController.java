package com.my.worldwave.member.controller;

import com.my.worldwave.member.dto.MemberInfoDto;
import com.my.worldwave.member.dto.ProfileImgDto;
import com.my.worldwave.member.dto.SignUpDto;
import com.my.worldwave.member.service.MemberService;
import com.my.worldwave.security.CustomUserDetails;
import com.my.worldwave.util.LocationUrlBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final LocationUrlBuilder locationUrlBuilder;

    @PostMapping("/auth/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpDto signUpDto) {
        memberService.signUp(signUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/check-auth")
    public ResponseEntity<MemberInfoDto> checkAuthentication(@AuthenticationPrincipal UserDetails userDetails) {
        MemberInfoDto memberInfo = memberService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(memberInfo);
    }

    @PostMapping("/member/profile-img")
    public ResponseEntity<ProfileImgDto> uploadProfileImg(@AuthenticationPrincipal UserDetails userDetails, @RequestParam MultipartFile file) {
        ProfileImgDto profileImgDto = memberService.uploadProfileImg(userDetails.getUsername(), file);
        return ResponseEntity.ok(profileImgDto);
    }

}
