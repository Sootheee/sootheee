package com.soothee.member.service;

import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;

import java.util.Optional;

public interface MemberService {

    /** OAuth2 로그인 시, 받은 정보로 회원 조회</hr>
     *
     * @param oauth2ClientId String : OAuth2 로그인용 ID
     * @param snsType SnsType : OAuth2 로그인한 SNS 종류(GOOGLE or KAKAO)
     * @return Optional<Member> : 해당 정보의 맴버(Null 일 수도 있음)
     */
    Optional<Member> getMemberForOAuth2(String oauth2ClientId, SnsType snsType);

    /** 회원 가입</hr>
     *
     * @param member Member : 가입할 회원의 정보
     */
    void saveMember(Member member);
}
