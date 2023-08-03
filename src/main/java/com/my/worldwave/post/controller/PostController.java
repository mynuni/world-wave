package com.my.worldwave.post.controller;

import com.my.worldwave.post.dto.PostRequestDto;
import com.my.worldwave.post.dto.PostResponseDto;
import com.my.worldwave.post.service.PostService;
import com.my.worldwave.util.LocationUrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping("/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final LocationUrlBuilder locationUrlBuilder;

//    @GetMapping
//    public ResponseEntity<List<PostResponseDto>> findAllPosts(@RequestParam(defaultValue = "0") int page,
//                                                              @RequestParam(defaultValue = "5") int size) {
//        List<PostResponseDto> posts = postService.findAllPosts(page, size);
//        return ResponseEntity.ok(posts);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        PostResponseDto post = postService.findPostById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> findAllPostsByCountry(String country,
                                                                       @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDto> posts = postService.findAllPostsByCountry(country, pageable);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequestDto postDto) {
        Long postId = postService.createPost(postDto);
        String location = locationUrlBuilder.buildLocationUri(postId);
        return ResponseEntity.status(HttpStatus.CREATED).location(URI.create(location)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto) {
        PostResponseDto postResponseDto = postService.updatePost(id, postRequestDto);
        return ResponseEntity.ok(postResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

}