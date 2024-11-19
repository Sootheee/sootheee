package com.soothee.common.service;

import com.soothee.common.constants.SnsType;
import com.soothee.common.domain.AuthenticatedUser;
import com.soothee.common.domain.GoogleUser;
import com.soothee.common.domain.KakaoUser;
import com.soothee.member.domain.Member;
import com.soothee.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class GoogleOAuth2UserService implements CustomOAuth2UserService {
    private static final String REGISTRATION_ID = SnsType.GOOGLE.getSnsType();
    private final MemberService memberService;

    @Override
    public boolean supports(OAuth2UserRequest request) {
        String registrationId = request.getClientRegistration().getRegistrationId();
        return StringUtils.equals(REGISTRATION_ID, registrationId);
    }

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
