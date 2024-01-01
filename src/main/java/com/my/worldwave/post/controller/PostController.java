package com.my.worldwave.post.controller;

import com.my.worldwave.post.dto.request.PostCreateRequest;
import com.my.worldwave.post.dto.request.PostRequestParam;
import com.my.worldwave.post.dto.request.PostUpdateRequest;
import com.my.worldwave.post.dto.response.PostResponse;
import com.my.worldwave.post.service.PostService;
import com.my.worldwave.security.CustomUserDetails;
import com.my.worldwave.util.LocationUrlBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getPosts(@AuthenticationPrincipal CustomUserDetails userDetails, PostRequestParam postRequestParam) {
        Page<PostResponse> posts = postService.getPosts(userDetails.getMemberId(), postRequestParam.toPageable());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        PostResponse post = postService.getPost(id, userDetails.getMemberId());
        return ResponseEntity.ok(post);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<Page<PostResponse>> getPostsByMemberId(@PathVariable Long memberId, @AuthenticationPrincipal CustomUserDetails userDetails, PostRequestParam postRequestParam) {
        Page<PostResponse> posts = postService.getPostsByMemberId(memberId, userDetails.getMemberId(), postRequestParam.toPageable());
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@AuthenticationPrincipal CustomUserDetails userDetails, PostCreateRequest postCreateRequest) {
        Long postId = postService.createPost(userDetails.getMember(), postCreateRequest);
        return ResponseEntity.created(LocationUrlBuilder.buildLocationUri(postId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, PostUpdateRequest postUpdateRequest) {
        postService.updatePost(id, userDetails.getMemberId(), postUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.deletePost(id, userDetails.getMemberId());
        return ResponseEntity.noContent().build();
    }

}
