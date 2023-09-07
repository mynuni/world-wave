package com.my.worldwave.member.service;

import com.my.worldwave.exception.member.DuplicateEmailException;
import com.my.worldwave.exception.member.DuplicateNicknameException;
import com.my.worldwave.exception.member.PasswordMismatchException;
import com.my.worldwave.member.dto.*;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.member.entity.ProfileImg;
import com.my.worldwave.member.entity.Role;
import com.my.worldwave.member.repository.MemberRepository;
import com.my.worldwave.member.repository.ProfileImgRepository;
import com.my.worldwave.post.repository.PostRepository;
import com.my.worldwave.util.FileUploadService;
import com.my.worldwave.util.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.my.worldwave.member.dto.MemberInfoDto.toDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ProfileImgRepository profileImgRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final FileUploadService fileUploadService;

    @Transactional
    public void signUp(SignUpDto signUpDto) {
        validateSignUpDto(signUpDto);
        Member newMember = Member.builder()
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .nickname(signUpDto.getNickname())
                .country(signUpDto.getCountry())
                .role(Role.USER)
                .build();

        memberRepository.save(newMember);
    }

    public List<MemberInfoDto> getMemberList(Member member, MemberSearchDto searchDto) {
        return memberRepository.searchMembers(
                        searchDto.getCountry(),
                        searchDto.getGender(),
                        searchDto.getMinAge(),
                        searchDto.getMaxAge(),
                        searchDto.toPageable())
                .stream()
                .map(MemberInfoDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberInfoDto getMemberInfo(Long id) {
        Member foundMember = memberRepository.findMemberWithProfileImg(id).orElseThrow(() -> new EntityNotFoundException());
        return toDto(foundMember);
    }

    @Transactional
    public ProfileImgDto uploadProfileImg(Member member, MultipartFile file) throws IOException {
        FileUploadResponse result = fileUploadService.uploadFile(file);

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

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
    }

    @Transactional(readOnly = true)
    public List<FollowResponse> findAllByCountry(String country) {
        List<Member> members = memberRepository.findAllByCountry(country);

        return members.stream()
                .map(FollowResponse::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMember(Long id, WithdrawalRequest withdrawalRequest) {
        if (!withdrawalRequest.isAgreedToPolicy()) {
            throw new IllegalArgumentException("탈퇴 약관에 동의해야 합니다.");
        }

        Member foundMember = findById(id);
        validatePassword(withdrawalRequest.getPassword(), foundMember.getPassword());
        postRepository.deleteAllByAuthor(foundMember);
        memberRepository.delete(foundMember);
    }

    @Transactional(readOnly = true)
    public List<FollowResponse> getSuggestedMembers(Long id, SuggestedMembersRequest request) {
        Page<Member> suggestedMembers = memberRepository.findSuggestedMembers(id, request.getCountry(), request.toPageable());
        return suggestedMembers.getContent().stream()
                .map(FollowResponse::toDto)
                .collect(Collectors.toList());
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new PasswordMismatchException();
        }
    }

    @Transactional(readOnly = true)
    private void validateSignUpDto(SignUpDto signUpDto) {
        if (!signUpDto.getPassword().equals(signUpDto.getPasswordCheck())) {
            throw new PasswordMismatchException();
        }

        if (memberRepository.existsByEmail(signUpDto.getEmail())) {
            throw new DuplicateEmailException();
        }

        if (memberRepository.existsByNickname(signUpDto.getNickname())) {
            throw new DuplicateNicknameException();
        }
    }

}
