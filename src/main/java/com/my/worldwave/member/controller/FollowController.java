package com.my.worldwave.member.controller;

import com.my.worldwave.member.dto.response.FollowResponse;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.member.service.FollowService;
import com.my.worldwave.member.service.MemberService;
import com.my.worldwave.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/members/{id}")
@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowService followService;
    private final MemberService memberService;

    // 특정 회원의 팔로워 리스트 조회
    @GetMapping("/followers")
    public ResponseEntity<Page<FollowResponse>> getFollowers(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), 5, pageable.getSort());
        Page<FollowResponse> followers = followService.getFollowers(id, userDetails.getMemberId(), pageRequest);
        return ResponseEntity.ok(followers);
    }

    // 팔로잉 조회
    @GetMapping("/followings")
    public ResponseEntity<Page<FollowResponse>> getFollowings(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), 5, pageable.getSort());
        Page<FollowResponse> followingMembers = followService.getFollowingMembers(id, userDetails.getMemberId(), pageRequest);
        return ResponseEntity.ok(followingMembers);
    }

    /**
     * @param targetMemberId 팔로우 대상 사용자 ID
     * @param userDetails    팔로우 주체 (본인)
     */
    @PostMapping("/follow")
    public ResponseEntity<FollowResponse> toggleFollow(@PathVariable("id") Long targetMemberId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member follower = userDetails.getMember();
        Member following = memberService.findById(targetMemberId);
        FollowResponse followResponse = followService.toggleFollow(follower, following);
        return ResponseEntity.ok(followResponse);
    }

}
