package com.soothee.member.service;

import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;

import java.util.Optional;

public interface MemberService {

    Optional<Member> getMemberForOAuth2(String oauth2ClientId, SnsType snsType);

    void saveMember(Member member);
}
