package com.my.worldwave.post.controller;

import com.my.worldwave.post.dto.CommentRequestDto;
import com.my.worldwave.post.dto.CommentResponseDto;
import com.my.worldwave.post.service.CommentService;
import com.my.worldwave.util.LocationUrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final LocationUrlBuilder locationUrlBuilder;

    @GetMapping("/{commentId}")
    public ResponseEntity<?> findById(@PathVariable Long postId, @PathVariable Long commentId) {
        CommentResponseDto comment = commentService.findById(commentId);
        return ResponseEntity.ok(comment);
    }

    @PostMapping
    public ResponseEntity<Long> createComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto) {
        Long commentId = commentService.createComment(postId, commentRequestDto);
        String location = locationUrlBuilder.buildLocationUri(commentId);
        return ResponseEntity.status(HttpStatus.CREATED).location(URI.create(location)).build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto comment = commentService.updateComment(commentId, commentRequestDto);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

}
