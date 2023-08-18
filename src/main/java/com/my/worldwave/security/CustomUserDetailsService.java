package com.my.worldwave.security;

import com.my.worldwave.member.entity.Member;
import com.my.worldwave.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member foundMember = memberRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException());

        return new CustomUserDetails(foundMember);
    }
}
