package com.soothee.member.service;

import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import com.soothee.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    public Optional<Member> getMemberForOAuth2(String oauth2ClientId, SnsType snsType) {
        return memberRepository.findByOauth2ClientIdAndSnsType(oauth2ClientId, snsType);
    }

    public void saveMember(Member member) {
        memberRepository.save(member);
    }
}
