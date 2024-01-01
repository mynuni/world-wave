package com.my.worldwave.post.service;

import com.my.worldwave.exception.post.PostNotFoundException;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.post.dto.request.PostCreateRequest;
import com.my.worldwave.post.dto.request.PostUpdateRequest;
import com.my.worldwave.post.dto.response.PostAttachedFileResponse;
import com.my.worldwave.post.dto.response.PostResponse;
import com.my.worldwave.post.entity.Post;
import com.my.worldwave.post.entity.PostFile;
import com.my.worldwave.post.repository.CommentRepository;
import com.my.worldwave.post.repository.LikeRepository;
import com.my.worldwave.post.repository.PostFileRepository;
import com.my.worldwave.post.repository.PostRepository;
import com.my.worldwave.util.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final FileUploadService fileUploadService;

    @Transactional(readOnly = true)
    public Page<PostResponse> getPosts(Long memberId, Pageable pageable) {
        return postRepository.findAllWithFilesAndLikes(memberId, pageable)
                .map(post -> mapToPostResponse(post, memberId));
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getPostsByMemberId(Long targetMemberId, Long currentMemberId, Pageable pageable) {
        return postRepository.findPostsByAuthorId(targetMemberId, pageable)
                .map(post -> mapToPostResponse(post, currentMemberId));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId, Long memberId) {
        return postRepository.getPostByIdWithFilesAndLikes(postId)
                .map(post -> mapToPostResponse(post, memberId))
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    @Transactional
    public Long createPost(Member member, PostCreateRequest postCreateRequest) {
        Post post = Post.builder()
                .content(postCreateRequest.getContent())
                .author(member)
                .build();

        uploadPostFiles(postCreateRequest.getFiles(), post);
        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    @Transactional
    public void updatePost(Long postId, Long memberId, PostUpdateRequest postUpdateRequest) {
        Post post = findPostById(postId);
        checkAuthority(post, memberId);
        post.updateContent(postUpdateRequest.getContent());

        // 새로 추가할 파일이 있는 경우
        uploadPostFiles(postUpdateRequest.getNewFiles(), post);

        // 삭제할 파일이 있는 경우
        deletePostFiles(postUpdateRequest.getDeleteFileIds(), post);
    }

    @Transactional
    public void deletePost(Long id, Long memberId) {
        Post post = findPostById(id);
        checkAuthority(post, memberId);
        post.removeAllFiles();
        postRepository.delete(post);
    }

    @Transactional
    public void deletePostsByMember(Member member) {
        List<Post> posts = postRepository.findAllByAuthor(member);
        postFileRepository.deleteAllByPosts(posts);
        commentRepository.deleteAllCommentsByPosts(posts);
        likeRepository.deleteAllLikesByPosts(posts);
    }

    private Post findPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    private void checkAuthority(Post post, Long memberId) {
        if (!post.getAuthor().getId().equals(memberId)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }

    // 첨부 파일 로컬 저장 및 데이터베이스에 파일 정보 저장
    private void uploadPostFiles(List<MultipartFile> files, Post post) {
        if (files != null && !files.isEmpty()) {
            List<PostFile> postFiles = files.stream()
                    .map(fileUploadService::uploadFile)
                    .map(result -> PostFile.of(result, post))
                    .collect(Collectors.toList());

            postFileRepository.saveAll(postFiles);
        }
    }

    // 첨부 파일 삭제 시 연관 관계 제거
    private void deletePostFiles(List<Long> deleteFileIds, Post post) {
        if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
            post.getFiles().stream()
                    .filter(file -> deleteFileIds.contains(file.getId()))
                    .forEach(file -> file.setPost(null));
        }

    }

    // 좋아요 상태 및 이미지 파일 정보를 가공하여 DTO로 변환
    private PostResponse mapToPostResponse(Post post, Long memberId) {
        List<PostAttachedFileResponse> postAttachedFileResponses = post.getFiles().stream()
                .map(file -> new PostAttachedFileResponse(file.getId(), file.getStoredFileName()))
                .collect(Collectors.toList());

        return PostResponse.of(post,
                postAttachedFileResponses,
                isLiked(post, memberId));
    }

    private boolean isLiked(Post post, Long memberId) {
        return post.getLikes().stream()
                .anyMatch(like -> like.getMember().getId().equals(memberId));
    }

}
