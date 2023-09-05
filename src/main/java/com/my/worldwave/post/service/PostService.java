package com.my.worldwave.post.service;

import com.my.worldwave.exception.post.PostNotFoundException;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.post.dto.FeedRequest;
import com.my.worldwave.post.dto.PostRequestDto;
import com.my.worldwave.post.dto.PostResponseDto;
import com.my.worldwave.post.dto.PostUpdateRequest;
import com.my.worldwave.post.entity.Post;
import com.my.worldwave.post.entity.PostFile;
import com.my.worldwave.post.repository.LikeRepository;
import com.my.worldwave.post.repository.PostFileRepository;
import com.my.worldwave.post.repository.PostRepository;
import com.my.worldwave.util.FileUploadService;
import com.my.worldwave.util.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.my.worldwave.post.dto.PostResponseDto.convertToDto;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;
    private final LikeRepository likeRepository;
    private final FileUploadService fileUploadService;

    /**
     *  게시글 조회
     * @param id 게시글 식별자
     * @return Post를 응답 dto로 변환 후 반환
     */
    @Transactional(readOnly = true)
    public PostResponseDto findById(Long id) {
        Post foundPost = findPostById(id);
        return convertToDto(foundPost);
    }

    /**
     * 게시글 목록(Feed) 조회
     *
     * @param member 게시글 목록 중 좋아요 상태 및 수정, 삭제 권한을 표시하기 위한 회원 객체
     * @param feedRequest 페이징 데이터, 정렬 기준을 포함하는 dto
     * @return
     */
    @Transactional(readOnly = true)
    public Page<PostResponseDto> getFeeds(Member member, FeedRequest feedRequest) {
        Page<Post> posts = postRepository.findAllPosts(feedRequest.toPageable());
        return posts.map(post -> withPermissionAndLikeStatus(post, member));
    }

    /**
     * 게시글 작성
     *
     * @param member 연관 관계 설정을 위한 회원 객체(작성자)
     * @param postRequestDto 텍스트, 파일을 포함하는 글 작성 요청 dto
     * @return 생성된 자원의 id를 반환
     */
    public Long createPost(Member member, PostRequestDto postRequestDto) {
        Post newPost = Post.builder()
                .content(postRequestDto.getContent())
                .author(member)
                .build();

        // 첨부 파일이 있는 경우
        if (postRequestDto.getFiles() != null && !postRequestDto.getFiles().isEmpty()) {
            List<PostFile> postFiles = postRequestDto.getFiles().stream()
                    .map(file -> {
                        FileUploadResponse response = fileUploadService.uploadFile(file);
                        return createPostFile(response, newPost);
                    }).collect(Collectors.toList());
            newPost.updateFiles(postFiles);
        }

        Post savedPost = postRepository.save(newPost);
        return savedPost.getId();
    }

    /**
     * 게시글 수정
     *
     * @param id 수정할 게시글 식별자
     * @param member 수정 권한을 체크하기 위한 회원 객체
     * @param request 추가할 사진, 삭제할 사진, 변경 등의 정보를 담고 있는 dto
     * @return 수정 로직 수행 후 dto로 변환하여 반환
     */
    public PostResponseDto updatePost(Long id, Member member, PostUpdateRequest request) {
        Post foundPost = findPostById(id);
        checkAuthority(foundPost, member);

        // 사용자가 일부 사진을 삭제한 경우
        List<Long> deleteFileIds = request.getDeleteFileIds();
        if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
            deleteFileIds.forEach(fileId -> {
                postFileRepository.deleteById(fileId + 1);
            });
        }

        // 사용자가 새로운 사진을 추가한 경우
        List<MultipartFile> newFiles = request.getNewFiles();
        if (newFiles != null && !newFiles.isEmpty()) {
            List<PostFile> files = newFiles.stream()
                    .map(fileUploadService::uploadFile)
                    .map(response -> createPostFile(response, foundPost))
                    .collect(Collectors.toList());
            foundPost.getFiles().addAll(files);
        }

        foundPost.updateContent(request.getContent());
        Post savedPost = postRepository.save(foundPost);
        return convertToDto(savedPost);
    }

    /**
     * 게시글 삭제
     *
     * @param id 게시글 식별자
     * @param member 시큐리티 인증 객체(회원)
     */
    public void deletePost(Long id, Member member) {
        Post foundPost = findPostById(id);
        checkAuthority(foundPost, member);
        postRepository.deleteById(id);
    }

    /**
     * 수정, 삭제 시 접근 권한 검사를 위한 메서드
     *
     * @param post 의 작성자 식별자와
     * @param member 의 식별자를
     */
    private void checkAuthority(Post post, Member member) {
        if (!post.getAuthor().getId().equals(member.getId())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }

    /**
     * Post 식별자로 Post 객체를 반환하기 위한 메서드
     * 중복 코드 제거를 위해 추출
     *
     * @param id 게시글 식별자
     * @return
     */
    @Transactional(readOnly = true)
    private Post findPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    /**
     * 게시글 수정, 삭제 권한 및 좋아요 상태 처리를 위한 메서드
     *
     * @param post 의 작성자 식별자와
     * @param member 식별자를 비교 후
     * @return hasPermission 설정 및 해당 게시글의 좋아요 상태를 대입 후 반환
     */
    private PostResponseDto withPermissionAndLikeStatus(Post post, Member member) {
        PostResponseDto postResponseDto = convertToDto(post);
        if (post.getAuthor().getId().equals(member.getId())) {
            postResponseDto.setHasPermission(true);
        }
        boolean isLiked = likeRepository.existsByMemberAndPost(member, post);
        postResponseDto.setAlreadyLiked(isLiked);
        return postResponseDto;
    }

    /**
     * PostFile 타입 객체 생성을 위한 메서드
     *
     * @param response 파일 업로드 관련 정보
     * @param post 연관 관계를 위한 post 객체
     * @return 파일 업로드 정보와 연관 관계 설정 후 PostFile 객체 반환
     */
    private PostFile createPostFile(FileUploadResponse response, Post post) {
        return PostFile.builder()
                .originalFileName(response.getOriginalFileName())
                .storedFileName(response.getStoredFileName())
                .storedFilePath(response.getStoredFilePath())
                .extension(response.getExtension())
                .fileSize(response.getFileSize())
                .post(post)
                .build();
    }

}
