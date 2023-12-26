package com.my.worldwave.member.service;

import com.my.worldwave.auth.dto.TokenDto;
import com.my.worldwave.exception.member.DuplicateEmailException;
import com.my.worldwave.exception.member.PasswordConfirmMismatchException;
import com.my.worldwave.exception.member.PasswordMismatchException;
import com.my.worldwave.member.dto.request.MemberSearchParam;
import com.my.worldwave.member.dto.request.MemberUpdateRequest;
import com.my.worldwave.member.dto.request.OAuth2SignUpRequest;
import com.my.worldwave.member.dto.request.SignUpRequest;
import com.my.worldwave.member.dto.response.MemberSearchResponse;
import com.my.worldwave.member.dto.response.MyInfoSummaryResponse;
import com.my.worldwave.member.entity.*;
import com.my.worldwave.member.repository.MemberRepository;
import com.my.worldwave.post.service.PostService;
import com.my.worldwave.security.JwtTokenService;
import com.my.worldwave.security.OAuth2UnlinkService;
import com.my.worldwave.util.FileUploadService;
import com.my.worldwave.util.RedisService;
import com.my.worldwave.util.dto.FileUploadResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileUploadService fileUploadService;
    private final RedisService redisService;
    private final JwtTokenService jwtTokenService;
    private final PostService postService;

    @Transactional(readOnly = true)
    public Page<MemberSearchResponse> searchMembers(Long memberId, MemberSearchParam searchParam) {
        return memberRepository.searchMembers(
                memberId,
                searchParam.getCountry(),
                searchParam.getNickname(),
                searchParam.getGender(),
                searchParam.getAgeRange(),
                searchParam.isHideFollowers(),
                searchParam.toPageable()
        );
    }

    @Transactional(readOnly = true)
    public MyInfoSummaryResponse getMyInfoSummary(Long id) {
        Member member = memberRepository.findMemberWithProfileImg(id).orElseThrow(EntityNotFoundException::new);
        return MyInfoSummaryResponse.from(member);
    }

    @Transactional(readOnly = true)
    public MemberSearchResponse getMemberInfo(Long memberId, Long myId) {
        return memberRepository.findMemberWithFollowState(memberId, myId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void updateMemberInfo(Long id, MemberUpdateRequest request) {
        Member member = memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        member.updateInfo(request.getNickname(), request.getAgeRange(), request.getGender(), request.getCountry());
    }

    // 이메일 인증 회원가입
    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        validateSignUpForm(signUpRequest);
        Member newMember = Member.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .nickname(signUpRequest.getNickname())
                .gender(signUpRequest.getGender())
                .ageRange(signUpRequest.getAgeRange())
                .country(signUpRequest.getCountry())
                .role(Role.USER)
                .registerType(RegisterType.FORM)
                .profileImage(new ProfileImage("default-profile-image.png"))
                .build();

        memberRepository.save(newMember);
    }

    // OAuth2 회원가입
    @Transactional
    public Member signUpWithOAuth2(OAuth2SignUpRequest signUpRequest) {
        String email = jwtTokenService.extractUsernameFromToken(signUpRequest.getEmailToken());

        Member newMember = Member.builder()
                .email(email)
                .nickname(signUpRequest.getNickname())
                .gender(signUpRequest.getGender())
                .ageRange(signUpRequest.getAgeRange())
                .country(signUpRequest.getCountry())
                .role(Role.USER)
                .registerType(RegisterType.GOOGLE)
                .profileImage(new ProfileImage("default-profile-image.png"))
                .build();

        return memberRepository.save(newMember);
    }

    // 일반 가입자 회원 탈퇴
    @Transactional
    public void formUserWithdraw(Long memberId, String password, String accessToken, String refreshToken) {
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        validatePassword(password, member.getPassword());
        deleteAuthData(member, accessToken, refreshToken);
        deleteMemberData(member);
        member.inactivate();
        memberRepository.save(member);
    }

    // OAuth2 가입자 회원 탈퇴
    @Transactional
    public void oAuth2UserWithdraw(Long memberId, String accessToken, String refreshToken) {
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        deleteAuthData(member, accessToken, refreshToken);
        deleteMemberData(member);
        member.inactivate();
        memberRepository.save(member);
    }

    // CASCADE 속성이 없는 연관 엔티티 벌크 삭제
    private void deleteMemberData(Member member) {
        ProfileImage profileImage = member.getProfileImage();
        if (profileImage != null) {
            profileImage.markDeleted();
        }
        memberRepository.deleteActivitiesByMember(member);
        memberRepository.deleteFollowsByMember(member);
        memberRepository.deleteLikesByMember(member);
        memberRepository.deleteCommentsByMember(member);
        postService.deletePostsByMember(member);
    }

    @Transactional
    public String uploadProfileImg(Long memberId, MultipartFile file) {
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);

        // 기존 프로필 이미지가 존재하면 연관 관계 해제
        if (member.getProfileImage() != null) {
            member.getProfileImage().markDeleted();
        }

        FileUploadResult result = fileUploadService.uploadFile(file);
        ProfileImage newProfileImage = ProfileImage.builder()
                .originalFileName(result.getOriginalFileName())
                .storedFileName(result.getStoredFileName())
                .storedFilePath(result.getStoredFilePath())
                .extension(result.getExtension())
                .fileSize(result.getFileSize())
                .build();

        member.updateProfileImage(newProfileImage);
        memberRepository.save(member);
        return newProfileImage.getStoredFileName();
    }

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    // 비밀번호 초기화 후 새로운 비밀번호로 변경
    @Transactional
    public void changePassword(String email, String password, String passwordConfirm) {
        validatePasswordConfirm(password, passwordConfirm);
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        member.updatePassword(passwordEncoder.encode(password));
    }

    // 기존의 비밀번호를 이용해 새로운 비밀번호로 변경
    @Transactional
    public void changePassword(Member member, String currentPassword, String newPassword, String newPasswordConfirm) {
        validatePasswordConfirm(newPassword, newPasswordConfirm);
        validatePassword(currentPassword, member.getPassword());
        member.updatePassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }

    @Transactional
    public void validateSignUpForm(SignUpRequest signUpRequest) {
        validatePasswordConfirm(signUpRequest.getPassword(), signUpRequest.getPasswordConfirm());
        if (isEmailExists(signUpRequest.getEmail())) {
            throw new DuplicateEmailException();
        }

    }

    @Transactional(readOnly = true)
    public boolean isEmailExists(String email) {
        return memberRepository.existsByEmail(email);
    }

    private void deleteAuthData(Member member, String accessToken, String refreshToken) {
        redisService.addToBlacklist(accessToken, member.getEmail());
        redisService.deleteRefreshToken(refreshToken);
        member.inactivate();
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new PasswordMismatchException();
        }
    }

    public void validatePasswordConfirm(String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new PasswordConfirmMismatchException();
        }
    }

    /* 배포 시 파일 직접 삭제 요청 -> 참조 끊기로 전환, 모아서 정기 삭제
    public ProfileImgDto uploadProfileImg(Member member, MultipartFile file) throws IOException {
        FileUploadResult result = fileUploadService.uploadFile(file);

        if (member.getProfileImg() != null) {
            fileUploadService.deleteFile(member.getProfileImg().getStoredFileName());
        }

        ProfileImg profileImg = ProfileImg.builder()
                .originalFileName(result.getOriginalFileName())
                .storedFileName(result.getStoredFileName())
                .storedFilePath(result.getStoredFilePath())
                .extension(result.getExtension())
                .fileSize(result.getFileSize())
                .build();

        ProfileImg savedProfileImg = profileImgRepository.save(profileImg);
        member.updateProfileImg(savedProfileImg);
        memberRepository.save(member);
        return ProfileImgDto.toDto(savedProfileImg);
    }
    */

}