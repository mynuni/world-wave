package com.my.worldwave.member.service;

import com.my.worldwave.exception.member.DuplicateEmailException;
import com.my.worldwave.exception.member.DuplicateNicknameException;
import com.my.worldwave.exception.member.PasswordMismatchException;
import com.my.worldwave.member.dto.MemberInfoDto;
import com.my.worldwave.member.dto.ProfileImgDto;
import com.my.worldwave.member.dto.SignUpDto;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.member.entity.ProfileImg;
import com.my.worldwave.member.entity.Role;
import com.my.worldwave.member.repository.MemberRepository;
import com.my.worldwave.member.repository.ProfileImgRepository;
import com.my.worldwave.util.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    @PersistenceContext
    private EntityManager entityManager;
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

    @Transactional
    public ProfileImgDto uploadProfileImg(String email, MultipartFile file) {
        String filePath = fileUploadService.uploadFile(file);
        ProfileImg profileImg = ProfileImg.builder()
                .originalFileName(file.getOriginalFilename())
                .filePath(filePath)
                .fileSize(file.getSize())
                .build();

        ProfileImg savedProfileImg = profileImgRepository.save(profileImg);
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException());
        member.updateProfileImg(savedProfileImg);
        return ProfileImgDto.toDto(savedProfileImg);
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

    public MemberInfoDto findByEmail(String email) {
        Member foundMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException());
        return MemberInfoDto.toDto(foundMember);
    }
}
