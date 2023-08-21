package com.my.worldwave.member.service;

import com.my.worldwave.exception.BadRequestException;
import com.my.worldwave.exception.member.DuplicateEmailException;
import com.my.worldwave.exception.member.DuplicateNicknameException;
import com.my.worldwave.exception.member.PasswordMismatchException;
import com.my.worldwave.member.dto.LoginDto;
import com.my.worldwave.member.dto.MemberInfoDto;
import com.my.worldwave.member.dto.SignUpDto;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.member.entity.Role;
import com.my.worldwave.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static com.my.worldwave.member.dto.MemberInfoDto.convertToDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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
