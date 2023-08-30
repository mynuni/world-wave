package com.my.worldwave.member.controller;

import com.my.worldwave.member.dto.FollowResponse;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.member.service.FollowService;
import com.my.worldwave.member.service.MemberService;
import com.my.worldwave.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowService followService;
    private final MemberService memberService;

    @GetMapping("/member/{id}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable Long id, Pageable pageable) {
        List<FollowResponse> followers = followService.getFollowers(id, pageable);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/member/{id}/following")
    public ResponseEntity<?> getFollowingMembers(@PathVariable Long id, Pageable pageable) {
        List<FollowResponse> followingMembers = followService.getFollowingMembers(id, pageable);
        return ResponseEntity.ok(followingMembers);
    }

    /**
     * @param targetMemberId 팔로우 대상 사용자 ID
     * @param userDetails    팔로우 주체 (본인)
     * @return 팔로우 상태(boolean)를 포함한 DTO로 반환
     */
    @PostMapping("/member/{id}/follow")
    public ResponseEntity<FollowResponse> toggleFollow(@PathVariable("id") Long targetMemberId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member follower = userDetails.getMember();
        Member following = memberService.findById(targetMemberId);
        FollowResponse followResponse = followService.toggleFollow(follower, following);
        return ResponseEntity.ok(followResponse);
    }

}
