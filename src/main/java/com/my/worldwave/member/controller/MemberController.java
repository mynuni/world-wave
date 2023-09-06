package com.my.worldwave.member.controller;

import com.my.worldwave.member.dto.*;
import com.my.worldwave.member.service.MemberService;
import com.my.worldwave.security.CustomUserDetails;
import com.my.worldwave.util.LocationUrlBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/members")
    public ResponseEntity<List<MemberInfoDto>> getMembers(@AuthenticationPrincipal CustomUserDetails userDetails, MemberSearchDto searchDto) {
        List<MemberInfoDto> members = memberService.getMemberList(userDetails.getMember(), searchDto);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/member/my-info")
    public ResponseEntity<MemberInfoDto> getMemberInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberInfoDto memberInfo = memberService.getMemberInfo(userDetails.getMember().getId());
        return ResponseEntity.ok(memberInfo);
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<MemberInfoDto> getMemberInfo(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (!id.equals(userDetails.getMember().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        MemberInfoDto memberInfo = memberService.getMemberInfo(userDetails.getMember().getId());
        return ResponseEntity.ok(memberInfo);
    }

    @PatchMapping("/member/{id}")
    public ResponseEntity<?> updateMemberInfo(@PathVariable Long id,
                                              @AuthenticationPrincipal CustomUserDetails userDetails,
                                              @RequestBody MemberInfoDto memberInfoDto) {
        if (!id.equals(userDetails.getMember().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/member/{id}")
    @PreAuthorize("#id.equals(authentication.principal.member.id)")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id,
                                             @AuthenticationPrincipal CustomUserDetails userDetails,
                                             @RequestBody WithdrawalRequest withdrawalRequest) {
        memberService.deleteMember(id, withdrawalRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/member/{id}/profile-img")
    @PreAuthorize("#id.equals(authentication.principal.member.id)")
    public ResponseEntity<ProfileImgDto> uploadProfileImg(@PathVariable Long id,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @RequestParam MultipartFile file) throws IOException {
        ProfileImgDto profileImgDto = memberService.uploadProfileImg(userDetails.getMember(), file);
        return ResponseEntity.ok(profileImgDto);
    }

    @GetMapping("/member/suggested-members")
    public ResponseEntity<List<FollowResponse>> getSuggestedMembers(@AuthenticationPrincipal CustomUserDetails userDetails, SuggestedMembersRequest request) {
        List<FollowResponse> members = memberService.getSuggestedMembers(userDetails.getMember().getId(), request);
        return ResponseEntity.ok(members);
    }

}
