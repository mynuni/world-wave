package com.my.worldwave.post.controller;

import com.my.worldwave.post.dto.PostRequestDto;
import com.my.worldwave.post.dto.PostResponseDto;
import com.my.worldwave.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/posts")
@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> findAllPosts(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "5") int size) {

        List<PostResponseDto> posts = postService.findAllPosts(page, size);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        PostResponseDto post = postService.findPostById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequestDto postDto) {
        Long postId = postService.createPost(postDto);
        String location = buildLocationUri(postId);

        return ResponseEntity.status(HttpStatus.CREATED).location(URI.create(location)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postDto) {
        PostResponseDto postResponseDto = postService.updatePost(id, postDto);
        return ResponseEntity.ok(postResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    private String buildLocationUri(Long postId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/post/{id}")
                .buildAndExpand(postId)
                .toUriString();
    }

}
