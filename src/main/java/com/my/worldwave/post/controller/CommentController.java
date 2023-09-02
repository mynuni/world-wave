package com.my.worldwave.post.controller;

import com.my.worldwave.post.dto.CommentRequestDto;
import com.my.worldwave.post.dto.CommentResponseDto;
import com.my.worldwave.post.dto.FeedRequest;
import com.my.worldwave.post.service.CommentService;
import com.my.worldwave.security.CustomUserDetails;
import com.my.worldwave.util.LocationUrlBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final LocationUrlBuilder locationUrlBuilder;

    @GetMapping
    public ResponseEntity<Page<CommentResponseDto>> findAll(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails, FeedRequest feedRequest) {
        Page<CommentResponseDto> comments = commentService.findAll(postId, userDetails.getMember(), feedRequest);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<Long> createComment(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody CommentRequestDto commentRequestDto) {
        Long commentId = commentService.createComment(postId, userDetails.getMember(), commentRequestDto);
        String location = locationUrlBuilder.buildLocationUri(commentId);
        return ResponseEntity.created(URI.create(location)).build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto comment = commentService.updateComment(commentId, userDetails.getMember(), commentRequestDto);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("POST ID:{}, COMMENT ID:{}", postId, commentId);
        commentService.deleteComment(commentId, userDetails.getMember());
        return ResponseEntity.ok().build();
    }

}
