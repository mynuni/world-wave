package com.my.worldwave.post.service;

import com.my.worldwave.exception.post.PostNotFoundException;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.post.dto.PageRequestDto;
import com.my.worldwave.post.dto.PostRequestDto;
import com.my.worldwave.post.dto.PostResponseDto;
import com.my.worldwave.post.entity.Post;
import com.my.worldwave.post.repository.LikeRepository;
import com.my.worldwave.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final LikeRepository likeRepository;

    @Transactional(readOnly = true)
    public List<PostResponseDto> findAllPosts(PageRequestDto pageRequestDto) {
        Pageable pageable = pageRequestDto.toPageable();
        Page<Post> postPage = postRepository.findAllPosts(pageable);
        return postPage.map(PostResponseDto::convertToDto).getContent();
    }

//    @Transactional(readOnly = true)
//    public Page<PostResponseDto> findAllPostsByCountry(Member member, String country, PageRequestDto pageRequestDto) {
//        Page<Post> postPage = postRepository.findAllPostsByCountry(country, pageRequestDto.toPageable());
//        return postPage.map(PostResponseDto::convertToDto);
//    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> findAllPostsByCountry(Member member, String country, PageRequestDto pageRequestDto) {
        String sortCondition = pageRequestDto.getSort();
        Page<Post> postPage;

        if ("likes".equals(sortCondition)) {
            postPage = postRepository.findAllPostsByCountryOrderByLikes(country, pageRequestDto.toPageable());
        } else if ("comments".equals(sortCondition)) {
            postPage = postRepository.findAllPostsByCountryOrderByComments(country, pageRequestDto.toPageable());
        } else {
            postPage = postRepository.findAllPostsByCountry(country, pageRequestDto.toPageable());
        }
        return postPage.map(post -> withPermissionAndLikeStatus(post, member));
    }

    @Transactional(readOnly = true)
    public PostResponseDto findById(Long id) {
        Post foundPost = findPostById(id);
        return convertToDto(foundPost);
    }

    public PostResponseDto createPost(Member member, PostRequestDto postDto) {
        Post newPost = Post.builder()
                .content(postDto.getContent())
                .author(member)
                .country(postDto.getCountry())
                .build();

        Post savedPost = postRepository.save(newPost);

        return PostResponseDto.convertToDto(savedPost);
    }

    public PostResponseDto updatePost(Long id, Member member, PostRequestDto postDto) {
        Post foundPost = findPostById(id);
        checkAuthority(member, foundPost);
        foundPost.updateEntity(postDto.getContent());
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
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    private void checkAuthority(Member member, Post post) {
        if (!post.getAuthor().getId().equals(member.getId())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }

    private PostResponseDto withPermission(Post post, Long memberId) {
        PostResponseDto postResponseDto = PostResponseDto.convertToDto(post);
        if (post.getAuthor().getId().equals(memberId)) {
            postResponseDto.setHasPermission(true);
        }
        return postResponseDto;
    }

    private PostResponseDto withPermissionAndLikeStatus(Post post, Member member) {
        PostResponseDto postResponseDto = PostResponseDto.convertToDto(post);

        if (post.getAuthor().getId().equals(member.getId())) {
            postResponseDto.setHasPermission(true);
        }

        boolean isLiked = likeRepository.existsByMemberAndPost(member, post);
        postResponseDto.setLikeStatus(isLiked);

        return postResponseDto;
    }

}
