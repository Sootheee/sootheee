package com.soothee.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OAuth2Service extends DefaultOAuth2UserService {
    private final List<CustomOAuth2UserService> oauth2UserServices;

    public OAuth2User loadUserFromParent(OAuth2UserRequest request) {
        return super.loadUser(request);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        for (CustomOAuth2UserService oauth2UserService : oauth2UserServices) {
            if (!oauth2UserService.supports(userRequest)) {
                continue;
            }
            OAuth2User oauth2User = loadUserFromParent(userRequest);
            return oauth2UserService.createOrLoadUser(oauth2User);
        }
        throw new RuntimeException("지원하지 않는 플랫폼입니다.");
    }
}
