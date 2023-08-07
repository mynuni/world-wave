package com.my.worldwave.member.service;

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
@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpDto signUpDto) {
        Member newUser = Member.builder()
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .nickname(signUpDto.getNickname())
                .role(Role.MEMBER)
                .build();

        memberRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public MemberInfoDto login(LoginDto loginDto) {
        Member foundMember = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("MEMBER NOT FOUND"));

        validatePassword(loginDto.getPassword(), foundMember.getPassword());
        return convertToDto(foundMember);

    }

    @Transactional(readOnly = true)
    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MEMBER NOT FOUND"));
    }

    private void validatePassword(String inputPassword, String storedPassword) {
        if (!passwordEncoder.matches(inputPassword, storedPassword)) {
            throw new RuntimeException("WRONG INFORMATION");
        }
    }

}
