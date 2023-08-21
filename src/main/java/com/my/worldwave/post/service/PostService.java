package com.my.worldwave.post.service;

import com.my.worldwave.member.entity.Member;
import com.my.worldwave.post.dto.PageRequestDto;
import com.my.worldwave.post.dto.PostRequestDto;
import com.my.worldwave.post.dto.PostResponseDto;
import com.my.worldwave.post.entity.Post;
import com.my.worldwave.post.repository.CommentRepository;
import com.my.worldwave.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.my.worldwave.post.dto.PostResponseDto.convertToDto;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<PostResponseDto> findAllPosts(PageRequestDto pageRequestDto) {
        Pageable pageable = pageRequestDto.toPageable();
        Page<Post> postPage = postRepository.findAllPosts(pageable);
        return postPage.map(PostResponseDto::convertToDto).getContent();
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> findAllPostsByCountry(String country, PageRequestDto pageRequestDto) {
        Page<Post> postPage = postRepository.findAllPostsByCountry(country, pageRequestDto.toPageable());
        return postPage.map(PostResponseDto::convertToDto);
    }

    @Transactional(readOnly = true)
    public PostResponseDto findById(Long id) {
        Post foundPost = findPostById(id);
        return convertToDto(foundPost);
    }

    public Long createPost(Member member, PostRequestDto postDto) {
        Post newPost = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .author(member)
                .country(postDto.getCountry())
                .build();

        Post savedPost = postRepository.save(newPost);
        return savedPost.getId();
    }

    public PostResponseDto updatePost(Long id, Member member, PostRequestDto postDto) {
        Post foundPost = findPostById(id);
        checkAuthority(member, foundPost);
        foundPost.updateEntity(postDto.getTitle(), postDto.getContent());
        return convertToDto(foundPost);
    }

    public void deletePost(Long id, Member member) {
        Post foundPost = findPostById(id);
        checkAuthority(member, foundPost);
        postRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    private Post findPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("POST NOT FOUND ID:" + id));
    }

    private void checkAuthority(Member member, Post post) {
        if (!post.getAuthor().getId().equals(member.getId())) {
            throw new AccessDeniedException("ACCESS DENIED");
        }
    }

}
