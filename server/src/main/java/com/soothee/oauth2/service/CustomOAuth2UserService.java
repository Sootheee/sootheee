package com.soothee.oauth2.service;

import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.oauth2.domain.AuthenticatedUser;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface CustomOAuth2UserService {
    /**
     * 해당 SNS OAuth2을 지원하는지 확인
     *
     * @param request
     * @return
     */
    boolean supports(OAuth2UserRequest request);

    /**
     * 인증된 회원 엔티티 조회 및 생성
     * 기존 회원 -> 조회
     * 신규 회원 -> 생성
     *
     * @param authenticatedUser 인증된 회원
     * @return 인증된 회원 정보를 담은 토큰 반환
     */
    AuthenticatedUser createOrLoadUser(OAuth2User authenticatedUser) throws IncorrectValueException, NullValueException;
}
