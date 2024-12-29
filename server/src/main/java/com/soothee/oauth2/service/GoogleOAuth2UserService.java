package com.soothee.oauth2.service;

import com.soothee.common.constants.SnsType;
import com.soothee.oauth2.domain.AuthenticatedUser;
import com.soothee.oauth2.domain.GoogleUser;
import com.soothee.member.domain.Member;
import com.soothee.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class GoogleOAuth2UserService implements CustomOAuth2UserService {
    private static final String REGISTRATION_ID = SnsType.GOOGLE.toString();
    private final MemberService memberService;

    /**
     * 해당 SNS OAuth2을 지원하는지 확인
     * "google"인 경우에만 true
     *
     * @param request OAuth2 로그인 요청
     * @return 맞으면 true / 아니면 false
     */
    @Override
    public boolean supports(OAuth2UserRequest request) {
        String registrationId = request.getClientRegistration().getRegistrationId();
        return StringUtils.equals(REGISTRATION_ID, registrationId);
    }

    /**
     * 인증된 회원 엔티티 조회 및 생성
     * 기존 회원 -> 조회
     * 신규 회원 -> 생성
     *
     * @param authenticatedUser 인증된 회원
     * @return 인증된 회원 정보를 담은 토큰 반환
     */
    @Override
    public AuthenticatedUser createOrLoadUser(OAuth2User authenticatedUser) {
        String oauth2ClientId = authenticatedUser.getName();
        Optional<Member> optional = memberService.getMemberForOAuth2(oauth2ClientId, SnsType.GOOGLE);
        Member member;
        if (optional.isPresent()) {
            member = optional.get();
        } else {
            member = new GoogleUser(authenticatedUser).toMember();
            memberService.saveMember(member);
        }
        return AuthenticatedUser.of(member, authenticatedUser);
    }
}
