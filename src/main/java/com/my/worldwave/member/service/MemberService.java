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
import com.my.worldwave.util.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ProfileImgRepository profileImgRepository;
    private final PasswordEncoder passwordEncoder;
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

    @Transactional(readOnly = true)
    public MemberInfoDto getMemberInfo(Long id) {
        Member foundMember = findById(id);
        return MemberInfoDto.toDto(foundMember);
    }

    @Transactional
    public ProfileImgDto uploadProfileImg(Long id, MultipartFile file) {
        String filePath = fileUploadService.uploadFile(file);
        ProfileImg profileImg = ProfileImg.builder()
                .originalFileName(file.getOriginalFilename())
                .filePath(filePath)
                .fileSize(file.getSize())
                .build();

        ProfileImg savedProfileImg = profileImgRepository.save(profileImg);
        Member member = findById(id);
        member.updateProfileImg(savedProfileImg);
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

    public void deleteMember(Long id, Member member) {
        // 회원 탈퇴
    }

    @Transactional(readOnly = true)
    public List<FollowResponse> getSuggestedMembers(Long id, SuggestedMembersRequest request) {
        Page<Member> suggestedMembers = memberRepository.findSuggestedMembers(id, request.getCountry(), request.toPageable());
        log.info("Service SIZE:{}", suggestedMembers.getContent().size());
        return suggestedMembers.getContent().stream()
                .map(FollowResponse::toDto)
                .collect(Collectors.toList());
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
