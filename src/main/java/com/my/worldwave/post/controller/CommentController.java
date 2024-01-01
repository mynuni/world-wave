package com.my.worldwave.post.controller;

import com.my.worldwave.post.dto.request.CommentRequest;
import com.my.worldwave.post.dto.response.CommentDto;
import com.my.worldwave.post.dto.request.PostSearchRequest;
import com.my.worldwave.post.dto.response.CommentResponse;
import com.my.worldwave.post.service.CommentService;
import com.my.worldwave.security.CustomUserDetails;
import com.my.worldwave.util.LocationUrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<CommentResponse>> findAll(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails, PostSearchRequest request) {
        Page<CommentResponse> comments = commentService.findAll(postId, request);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody CommentRequest commentRequest) {
        CommentDto comment = commentService.createComment(postId, userDetails.getMember(), commentRequest);
        return ResponseEntity.created(LocationUrlBuilder.buildLocationUri(comment.getId())).body(comment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody CommentRequest commentRequest) {
        CommentDto comment = commentService.updateComment(commentId, userDetails.getMember(), commentRequest);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.deleteComment(commentId, userDetails.getMember());
        return ResponseEntity.noContent().build();
    }

}
