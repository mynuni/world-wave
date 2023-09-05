package com.my.worldwave.post.controller;

import com.my.worldwave.post.dto.FeedRequest;
import com.my.worldwave.post.dto.PostRequestDto;
import com.my.worldwave.post.dto.PostResponseDto;
import com.my.worldwave.post.dto.PostUpdateRequest;
import com.my.worldwave.post.service.PostService;
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
@RequestMapping("/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final LocationUrlBuilder locationUrlBuilder;

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> findById(@PathVariable Long id) {
        PostResponseDto post = postService.findById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getFeeds(@AuthenticationPrincipal CustomUserDetails userDetails, FeedRequest feedRequest) {
        Page<PostResponseDto> posts = postService.getFeeds(userDetails.getMember(), feedRequest);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<Void> createPost(@AuthenticationPrincipal CustomUserDetails userDetails, PostRequestDto postRequestDto) {
        Long postId = postService.createPost(userDetails.getMember(), postRequestDto);
        String location = locationUrlBuilder.buildLocationUri(postId);
        return ResponseEntity.created(URI.create(location)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, PostUpdateRequest request) {
        PostResponseDto postResponseDto = postService.updatePost(id, userDetails.getMember(), request);
        return ResponseEntity.ok(postResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.deletePost(id, userDetails.getMember());
        return ResponseEntity.ok().build();
    }

}
