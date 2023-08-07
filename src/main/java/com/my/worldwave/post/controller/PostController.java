package com.my.worldwave.post.controller;

import com.my.worldwave.auth.resolver.AuthenticationPrincipal;
import com.my.worldwave.member.dto.MemberInfoDto;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.member.service.MemberService;
import com.my.worldwave.post.dto.PostRequestDto;
import com.my.worldwave.post.dto.PostResponseDto;
import com.my.worldwave.post.service.PostService;
import com.my.worldwave.util.LocationUrlBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RequestMapping("/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final MemberService memberService;
    private final LocationUrlBuilder locationUrlBuilder;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        PostResponseDto post = postService.findById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> findAllPostsByCountry(String country,
                                                                       @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDto> posts = postService.findAllPostsByCountry(country, pageable);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<?> createPost(@AuthenticationPrincipal MemberInfoDto memberInfoDto, @RequestBody PostRequestDto postDto) {
        Member member = memberService.findMemberById(memberInfoDto.getId());
        Long postId = postService.createPost(member, postDto);
        String location = locationUrlBuilder.buildLocationUri(postId);
        return ResponseEntity.status(HttpStatus.CREATED).location(URI.create(location)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@AuthenticationPrincipal MemberInfoDto memberInfoDto, @PathVariable Long id, @RequestBody PostRequestDto postRequestDto) {
        Member member = memberService.findMemberById(memberInfoDto.getId());
        PostResponseDto postResponseDto = postService.updatePost(member, id, postRequestDto);
        return ResponseEntity.ok(postResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal MemberInfoDto memberInfoDto, @PathVariable Long id) {
        Member member = memberService.findMemberById(memberInfoDto.getId());
        postService.deletePost(member, id);
        return ResponseEntity.ok().build();
    }

}