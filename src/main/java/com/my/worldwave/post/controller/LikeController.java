package com.my.worldwave.post.controller;

import com.my.worldwave.post.dto.response.LikeResponse;
import com.my.worldwave.post.service.LikeService;
import com.my.worldwave.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/posts")
@RequiredArgsConstructor
@RestController
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/{postId}/like/status")
    public boolean isAlreadyLiked(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return likeService.isAlreadyLiked(postId, userDetails.getMember());
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<LikeResponse> toggleLike(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long postId) {
        LikeResponse likeResponse = likeService.toggleLike(postId, userDetails.getMember());
        return ResponseEntity.ok(likeResponse);
    }

}
