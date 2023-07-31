package com.my.worldwave.post.service;

import com.my.worldwave.post.dto.PostRequestDto;
import com.my.worldwave.post.dto.PostResponseDto;
import com.my.worldwave.post.entity.Post;
import com.my.worldwave.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.my.worldwave.post.dto.PostResponseDto.convertToDto;

@Transactional
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<PostResponseDto> findAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Post> postPage = postRepository.findAll(pageable);
        return postPage.map(PostResponseDto::convertToDto).getContent();
    }

    @Transactional(readOnly = true)
    public PostResponseDto findPostById(Long id) {
        Post foundPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("POST NOT FOUND ID:" + id));

        return convertToDto(foundPost);

//        return PostResponseDto.builder()
//                .id(foundPost.getId())
//                .title(foundPost.getTitle())
//                .content(foundPost.getContent())
//                .author(foundPost.getAuthor())
//                .build();
    }

    public Long createPost(PostRequestDto postDto) {
        Post newPost = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .author(postDto.getAuthor())
                .build();

        Post savedPost = postRepository.save(newPost);
        return savedPost.getId();
    }

    public PostResponseDto updatePost(Long id, PostRequestDto postDto) {
        Post foundPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("POST NOT FOUND ID:" + id));

        foundPost.updateEntity(postDto.getTitle(), postDto.getContent());

        return convertToDto(foundPost);

//        return PostResponseDto.builder()
//                .id(foundPost.getId())
//                .title(foundPost.getTitle())
//                .content(foundPost.getContent())
//                .author(foundPost.getAuthor())
//                .build();
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
