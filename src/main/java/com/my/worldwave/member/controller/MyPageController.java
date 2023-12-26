package com.my.worldwave.member.controller;

import com.my.worldwave.member.service.MyPageService;
import com.my.worldwave.post.dto.response.CommentResponse;
import com.my.worldwave.post.dto.response.PostSummaryResponse;
import com.my.worldwave.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/api/mypage/comments")
    public ResponseEntity<Page<CommentResponse>> getMyComments(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommentResponse> myComments = myPageService.getMyComments(userDetails.getMemberId(), pageable);
        return ResponseEntity.ok(myComments);
    }

    @GetMapping("/api/mypage/likes")
    public ResponseEntity<Page<PostSummaryResponse>> getMyLikes(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostSummaryResponse> myLikes = myPageService.getMyLikes(userDetails.getMemberId(), pageable);
        return ResponseEntity.ok(myLikes);
    }

    @DeleteMapping("/api/mypage/comments")
    public ResponseEntity<Void> deleteMyComments(@RequestBody List<Long> commentIds, @AuthenticationPrincipal CustomUserDetails userDetails) {
        myPageService.deleteMyComments(commentIds, userDetails.getMemberId());
        return ResponseEntity.noContent().build();
    }

}
