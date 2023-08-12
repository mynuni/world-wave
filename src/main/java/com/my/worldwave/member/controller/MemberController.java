package com.my.worldwave.member.controller;

import com.my.worldwave.auth.resolver.AuthenticationPrincipal;
import com.my.worldwave.member.dto.LoginDto;
import com.my.worldwave.member.dto.MemberInfoDto;
import com.my.worldwave.member.dto.SignUpDto;
import com.my.worldwave.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        memberService.signUp(signUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpSession session) {
        MemberInfoDto memberInfo = memberService.login(loginDto);
        session.setAttribute("authUser", memberInfo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/myinfo")
    public ResponseEntity<MemberInfoDto> myinfo(@AuthenticationPrincipal MemberInfoDto memberInfoDto) {

        if (memberInfoDto == null || memberInfoDto.getId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(memberInfoDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok().build();
    }

}
