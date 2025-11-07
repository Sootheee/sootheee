package com.soothee.oauth2.service;

import com.soothee.custom.exception.IncorrectParameterException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DelegatingOAuth2Service extends DefaultOAuth2UserService {
    private final List<CustomOAuth2UserService> oauth2UserServices;

    /**
     * OAuth2 로그인 회원 정보 로드
     *
     * @param request OAuth2 로그인 요청
     * @return 요청한 회원 정보
     */
    public OAuth2User loadUserFromParent(OAuth2UserRequest request) {
        return super.loadUser(request);
    }

    /**
     * 각 SNS 로그인 서비스의 지원 여부 확인 후, OAuth2 로그인 요청한 회원 정보 로드
     *
     * @param request OAuth2 로그인 요청
     * @return 요청한 회원 정보
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws IncorrectParameterException, OAuth2AuthenticationException {
        for (CustomOAuth2UserService oauth2UserService : oauth2UserServices) {
            if (oauth2UserService.supports(request)) {
                OAuth2User oauth2User = loadUserFromParent(request);
                return oauth2UserService.createOrLoadUser(oauth2User);
            }
        }
        throw new IncorrectParameterException(request);
    }
}
