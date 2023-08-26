package com.my.worldwave.post.controller;

import com.my.worldwave.post.dto.PostResponseDto;
import com.my.worldwave.post.service.LikeService;
import com.my.worldwave.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/posts")
@RequiredArgsConstructor
@RestController
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/{postId}/like-status")
    public boolean isAlreadyLiked(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return likeService.isAlreadyLiked(postId, userDetails.getMember());
    }

    @PostMapping("/{postId}/like-toggle")
    public ResponseEntity<Void> toggleLike(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        likeService.toggleLike(postId, userDetails.getMember());
        return ResponseEntity.ok().build();
    }

}
