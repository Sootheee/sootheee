package com.soothee.common.service;

import com.soothee.common.domain.AuthenticatedUser;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface CustomOAuth2UserService {
    boolean supports(OAuth2UserRequest request);
    AuthenticatedUser createOrLoadUser(OAuth2User authenticatedUser);
}
