package com.my.worldwave.member.controller;

import com.my.worldwave.member.dto.SignUpDto;
import com.my.worldwave.member.service.MemberService;
import com.my.worldwave.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpDto signUpDto) {
        memberService.signUp(signUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/myinfo")
    public ResponseEntity<?> myinfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // 임시
        if (userDetails == null) {
            log.info("userDetails is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userDetails.getMember().getNickname());
    }

}
